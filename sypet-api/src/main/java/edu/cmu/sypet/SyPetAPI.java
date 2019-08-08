package edu.cmu.sypet;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import edu.cmu.sypet.codeformer.CodeFormer;
import edu.cmu.sypet.compilation.Test;
import edu.cmu.sypet.java.ClassgraphTypeFinder;
import edu.cmu.sypet.java.Jar;
import edu.cmu.sypet.java.Method;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.java.TypeFinder;
import edu.cmu.sypet.petrinet.BuildNet;
import edu.cmu.sypet.reachability.Encoding;
import edu.cmu.sypet.reachability.EncodingUtil;
import edu.cmu.sypet.reachability.SequentialEncoding;
import edu.cmu.sypet.reachability.Variable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.sat4j.specs.TimeoutException;
import uniol.apt.adt.pn.PetriNet;

/**
 * This class represents the SyPet library API.
 */
@SuppressWarnings("WeakerAccess")
public final class SyPetAPI {
  // TODO Use Collection instead of List.

  private final SynthesisTask task;

  private final ImmutableMultimap<Type, Type> superclassMap;
  private final ImmutableMultimap<Type, Type> subclassMap;
  private final ImmutableMap<String, MethodSignature> signatureMap;

  private final PetriNet net;

  public SyPetAPI(final SynthesisTask task) throws SyPetException {
    this.task = task;

    final ImmutableCollection<MethodSignature> signatures;
    try (final TypeFinder typeFinder = new ClassgraphTypeFinder(getLibs(), task.packages())) {
      final ImmutableSet<Type> localSuperClasses = task.localSuperClasses();

      this.superclassMap = typeFinder.getSuperClasses(localSuperClasses, task.packages());
      this.subclassMap = getSuperclassMap().inverse();

      signatures = typeFinder.getSignatures(task.methodBlacklist());
    } catch (Exception e) {
      throw new SyPetException(e);
    }

    final BuildNet buildNet = new BuildNet(task.noSideEffects());
    this.net = buildNet.build(
        signatures,
        getSuperclassMap(),
        getSubclassMap(),
        new ArrayList<>(),
        true);
    this.signatureMap = ImmutableMap.copyOf(buildNet.dict);
  }

  /**
   * Synthesize a program from a <code>SynthesisTask</code>.
   *
   * @param synthesisTask the parameters to the synthesis task
   * @return optionally a program, if one can be synthesized
   * @see SynthesisTask
   */
  public static Optional<String> synthesize(final SynthesisTask synthesisTask) throws SyPetException {
    final SyPetAPI sypet = new SyPetAPI(synthesisTask);
    return sypet.synthesize(synthesisTask.locLowerBound(), synthesisTask.locUpperBound());
  }

  private Optional<String> synthesize(final int min_loc, final int max_loc) {

    int loc = min_loc;
    boolean solution = false;
    String synthesizedCode = "";
    String code;
    int paths = 0;
    int programs = 0;

    while (!solution && loc <= max_loc) {
      System.out.println("c LOC = " + loc);
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(getNet(), loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(getNet(), getParamTypes()), 0);
      encoding.setState(EncodingUtil.setGoalState(getNet(), getReturnType()), loc);
      encoding.setHints(getHints());

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      paths++;

      while (!result.isEmpty() && !solution) {
        final ImmutableList<MethodSignature> signatures = result.stream()
            .map(variable -> getSignatureMap().get(variable.getName()))
            .filter(Objects::nonNull)
            .collect(ImmutableList.toImmutableList());

        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former =
            new CodeFormer(
                signatures,
                getParamTypes(),
                getReturnType(),
                getParamNames(),
                getMethodName(),
                getSubclassMap(),
                getSuperclassMap());
        while (sat) {
          try {
            code = former.solve();
            programs++;
          } catch (TimeoutException e) {
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre;
          // System.out.println("code = " + code);
          // System.out.println("testCode = " + testCode);
          compre = Test.runTest(code, getTestCode(), getLibs());
          if (compre) {
            solution = true;
            synthesizedCode = code;
            break;
          }
        }
        // the current path did not result in a program that passes all test cases find the next
        // path
        paths++;
        result = Encoding.solver.findPath(loc);
      }

      // we did not find a program of length = loc
      loc++;
    }
    System.out.println("c #Programs explored = " + programs);
    System.out.println("c #Paths explored = " + paths);

    return Optional.of(synthesizedCode).filter(s -> !s.isEmpty());
  }

  public List<String> synthesizeAll(final int max_loc) {
    ArrayList<String> allCode = new ArrayList<>();

    int loc = 1;
    String code;

    while (loc <= max_loc) {
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(getNet(), loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(getNet(), getParamTypes()), 0);
      encoding.setState(EncodingUtil.setGoalState(getNet(), getReturnType()), loc);
      encoding.setHints(getHints());

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      while (!result.isEmpty()) {
        final ImmutableList<MethodSignature> signatures = result.stream()
            .map(variable -> getSignatureMap().get(variable.getName()))
            .filter(Objects::nonNull)
            .collect(ImmutableList.toImmutableList());

        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former =
            new CodeFormer(
                signatures,
                getParamTypes(),
                getReturnType(),
                getParamNames(),
                getMethodName(),
                getSubclassMap(),
                getSuperclassMap());
        while (sat) {
          try {
            code = former.solve();
          } catch (TimeoutException e) {
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre;
          compre = Test.runTest(code, getTestCode(), this.getLibs());

          if (compre) {
            allCode.add(code);
          }

          // the current path did not result in a program that passes all test cases find
          // the next path
          result = Encoding.solver.findPath(loc);
        }
      }

      // we did not find a program of length = loc
      loc++;
    }
    return allCode;
  }

  public ImmutableMultimap<Type, Type> getSuperclassMap() {
    return superclassMap;
  }

  public ImmutableMultimap<Type, Type> getSubclassMap() {
    return subclassMap;
  }

  public PetriNet getNet() {
    return net;
  }

  public ImmutableMap<String, MethodSignature> getSignatureMap() {
    return signatureMap;
  }

  public String getMethodName() {
    return this.task.methodName();
  }

  public ImmutableList<String> getParamNames() {
    return this.task.paramNames();
  }

  public ImmutableList<Type> getParamTypes() {
    return this.task.paramTypes();
  }

  public Type getReturnType() {
    return this.task.returnType();
  }

  public ImmutableSet<Jar> getLibs() {
    return this.task.jars();
  }

  public String getTestCode() {
    return this.task.testCode();
  }

  public ImmutableSet<Method> getHints() {
    return this.task.hints();
  }
}
