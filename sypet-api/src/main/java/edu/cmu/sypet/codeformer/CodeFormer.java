package edu.cmu.sypet.codeformer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

/**
 * Given a sequence of method calls, this class will produce a string containing the corresponding
 * Java code.
 */
public class CodeFormer {
  private final List<MethodSignature> sigs;
  private int slotNumber = 0;
  private int retNumber = 0;
  private final VarTable slotTypes = new VarTable();
  private final VarTable returnedValTypes = new VarTable();
  private boolean unsat = false;
  private final ImmutableList<Type> paramTypes;
  private final Type returnType;
  private final List<String> varNames;
  private final String methodName;
  private final Map<Integer, Integer> lastValueOfSlot = new HashMap<>();
  private final ImmutableMultimap<Type, Type> subclassMap;
  private final ImmutableMultimap<Type, Type> superclassMap;
  private final ISolver solver = SolverFactory.newDefault();

  /**
   * The initial setup for the class.
   * @param sigs requires a sequence of signatures in the expected order.
   * @param paramTypes
   * @param returnType
   * @param varNames parameter names of the method
   * @param methodName method name of the method
   * @param subclassMap
   * @param superclassMap
   */
  public CodeFormer(
      ImmutableList<MethodSignature> sigs,
      ImmutableList<Type> paramTypes,
      Type returnType,
      ImmutableList<String> varNames,
      String methodName,
      ImmutableMultimap<Type, Type> subclassMap,
      ImmutableMultimap<Type, Type> superclassMap
  ) {
    this.sigs = sigs;
    this.paramTypes = paramTypes;
    this.returnType = returnType;
    this.varNames = varNames;
    this.methodName = methodName;
    this.subclassMap = subclassMap;
    this.superclassMap = superclassMap;
    // solver.setTimeout(1000000);
    // Setup
    // Add method input

    for (final Type paramType : paramTypes) {
      returnedValTypes.addEntry(paramType, retNumber);
      retNumber += 1;
    }

    // Add slots and variables to the signatures table
    for (MethodSignature sig : sigs) {
      if (sig.isConstructor()) {

      } else if (!sig.isStatic()) {
        slotTypes.addEntry(sig.declaringClass(), slotNumber);
        lastValueOfSlot.put(slotNumber, retNumber);
        slotNumber += 1;
      }
      for (Type type : sig.parameterTypes()) {
        slotTypes.addEntry(type, slotNumber);
        lastValueOfSlot.put(slotNumber, retNumber);
        slotNumber += 1;
      }

      if (!sig.returnType().toString().equals("void")) {
        returnedValTypes.addEntry(sig.returnType(), retNumber);
        retNumber += 1;
      }
    }
    // Add method return value
    if (returnType != null) {
      slotTypes.addEntry(returnType, slotNumber);
      lastValueOfSlot.put(slotNumber, retNumber);
      slotNumber += 1;
    }

    // Setup constrains
    addSingleVariableConstrains();
    addAtLeastOneSlot();
  }

  /**
   * Each call to solve will produce one extra solution.
   *
   * @return one solution to the programming (Java code)
   * @throws TimeoutException Iff there is no solution available
   */
  public String solve() throws TimeoutException {
    // Solve
    int[] satResult;
    try {
      if (solver.isSatisfiable()) {
        satResult = solver.model();
      } else {
        unsat = true;
        throw new TimeoutException();
      }
    } catch (TimeoutException e) {
      unsat = true;
      throw new TimeoutException();
    }

    // A list only with filtered positive elements in the result.
    List<Integer> satList = new ArrayList<>();

    // Block this version, and filter the result with only positive ones.
    VecInt block = new VecInt();
    for (Integer id : satResult) {
      block.push(-id);
      if (id > 0) satList.add(id);
    }
    try {
      solver.addClause(block);
    } catch (ContradictionException e) {
      unsat = true;
    }

    // formCode
    return formCode(satList);
  }

  /** @return true iff the problem is no longer solvable. */
  public boolean isUnsat() {
    return unsat;
  }

  // Each slot only has variable
  private void addSingleVariableConstrains() {
    for (int slotValue = 0; slotValue < slotNumber; slotValue += 1) {
      IVecInt vec = new VecInt();
      IVecInt vec0 = new VecInt();

      final Type slotType = slotTypes.getType(slotValue);

      ImmutableList.Builder<Type> possibleSlotTypesBuilder = ImmutableList.builder();

      if (subclassMap.containsKey(slotType)) {
        possibleSlotTypesBuilder.addAll(subclassMap.get(slotType));
      }
      possibleSlotTypesBuilder.add(slotType);

      final ImmutableList<Type> possibleSlotTypes = possibleSlotTypesBuilder.build();

      for (final Type curSlotType : possibleSlotTypes) {
        for (int returnedValue : returnedValTypes.getEntries(curSlotType)) {
          if (returnedValue < lastValueOfSlot.get(slotValue))
            vec.push(calculateID(returnedValue, slotValue));
          else vec0.push(calculateID(returnedValue, slotValue));
        }
      }
      try {
        solver.addExactly(vec, 1);
        solver.addExactly(vec0, 0);
      } catch (ContradictionException e) {
        unsat = true;
      }
    }
  }

  // Each returned value used at least once
  private void addAtLeastOneSlot() {
    for (int returnedValue = 0; returnedValue < retNumber; returnedValue += 1) {
      IVecInt vec = new VecInt();

      final Type returnedType = returnedValTypes.getType(returnedValue);

      ImmutableList.Builder<Type> possibleSlotTypesBuilder = ImmutableList.builder();

      if (subclassMap.containsKey(returnedType)) {
        possibleSlotTypesBuilder.addAll(subclassMap.get(returnedType));
      }
      possibleSlotTypesBuilder.add(returnedType);

      final ImmutableList<Type> possibleSlotTypes = possibleSlotTypesBuilder.build();

      for (final Type slotType : possibleSlotTypes) {
        for (int slotValue : slotTypes.getEntries(slotType)) {
          vec.push(calculateID(returnedValue, slotValue));
        }
      }
      try {
        solver.addAtLeast(vec, 1);
      } catch (ContradictionException e) {
        unsat = true;
      }
    }
  }

  private String formCode(List<Integer> satResult) {

    // FIXME: check what is causing this bug
    String error = "";

    // FormCode
    StringBuilder builder = new StringBuilder();
    int varCount = 0;
    int slotCount = 0;

    // Add method signature
    builder.append("public static ");
    if (returnType != null) {
      builder.append(returnType);
      builder.append(" ");
    } else {
      builder.append("void ");
    }
    builder.append(methodName);
    builder.append("(");
    for (int i = 0; i < paramTypes.size(); i++) {
      builder.append(paramTypes.get(i));
      builder.append(" ");
      builder.append(convVarName(varCount));
      varCount += 1;
      if (i != paramTypes.size() - 1) builder.append(", ");
    }
    builder.append(") throws Throwable{\n");

    for (MethodSignature sig : sigs) {

      if (!sig.returnType().toString().equals("void")) {
        builder.append(sig.returnType().toString().replace('$', '.'));
        builder.append(" ");
        builder.append(convVarName(varCount));
        varCount += 1;
        builder.append(" = ");
      }

      if (sig.isConstructor()) {
        builder.append(" new ");
      } else if (sig.isStatic()) {
        String hostclstr = sig.declaringClass().name();
        builder.append(hostclstr.replace('$', '.'));
        builder.append(".");
      } else {
        if (slotCount >= satResult.size()) return error;
        int id = satResult.get(slotCount);
        slotCount++;
        int returnedValue = calculateReturnedValue(id);
        builder.append(convVarName(returnedValue));
        builder.append(".");
      }

      builder.append(sig.name().replace('$', '.'));
      builder.append("(");
      for (int i = 0; i < sig.parameterTypes().size(); i++) {
        if (slotCount >= satResult.size()) return error;
        int id = satResult.get(slotCount);
        slotCount++;
        int returnedValue = calculateReturnedValue(id);
        int slotValue = calculateSlotValue(id);
        // assert (slotValue == slotCount);

        builder.append(convVarName(returnedValue));
        if (i != sig.parameterTypes().size() - 1) {
          builder.append(",");
        }
      }
      builder.append(");\n");
    }
    if (returnType != null) {
      builder.append("return ");

      if (slotCount >= satResult.size()) return error;
      int id = satResult.get(slotCount);
      slotCount++;
      int returnedValue = calculateReturnedValue(id);
      int slotValue = calculateSlotValue(id);
      // assert (slotValue == slotCount);
      builder.append(convVarName(returnedValue));
      builder.append(";\n");
    }
    builder.append("}");
    return builder.toString().replace('$', '.');
  }

  private int calculateID(int returnedValue, int slotValue) {
    return returnedValue + retNumber * slotValue + 1;
  }

  private int calculateReturnedValue(int id) {
    return (id - 1) % retNumber;
  }

  private int calculateSlotValue(int id) {
    return (id - 1) / retNumber;
  }

  private String convVarName(int val) {
    if (val < varNames.size()) return varNames.get(val);
    else return "var_" + (val - varNames.size());
  }
}
