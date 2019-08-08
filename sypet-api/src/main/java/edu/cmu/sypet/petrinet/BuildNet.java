package edu.cmu.sypet.petrinet;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import edu.cmu.sypet.java.Method;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import uniol.apt.adt.exception.NoSuchEdgeException;
import uniol.apt.adt.exception.NoSuchNodeException;
import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;

/**
 * Build petri net from a set of libraries.
 *
 * @author Ruben Martins
 * @author Anlun Xu
 */
public class BuildNet {
  private final PetriNet petrinet;
  // A map from transition name to a method signature
  public final Map<String, MethodSignature> dict;

  private final Map<Type, List<Type>> superDict;
  private final Map<Type, List<Type>> subDict;

  private final ImmutableSet<Method> noSideEffects;

  public BuildNet(final ImmutableSet<Method> noSideEffects) {
    this.petrinet = new PetriNet("net");
    this.dict = new HashMap<>();
    this.superDict = new HashMap<>();
    this.subDict = new HashMap<>();
    this.noSideEffects = noSideEffects;
  }

  private void generatePolymophism(
      final Transition t,
      final int count,
      final List<Place> inputs,
      final Stack<Place> polyInputs
  ) {
    if (inputs.size() == count) {
      boolean skip = true;
      for (int i = 0; i < inputs.size(); i++) {
        if (!inputs.get(i).equals(polyInputs.get(i))) {
          skip = false;
        }
      }
      if (skip) {
        return;
      }

      StringBuilder newTransitionName = new StringBuilder(t.getId() + "Poly:(");
      for (Place p : polyInputs) {
        newTransitionName.append(p.getId()).append(" ");
      }
      newTransitionName.append(")");

      if (petrinet.containsTransition(newTransitionName.toString())) {
        return;
      }

      boolean polymorphicOutput = false;
      for (Flow f : t.getPostsetEdges()) {
        Place p = f.getPlace();

        List<Type> subClasses = subDict.get(p.getId());

        if (subClasses != null) {
          polymorphicOutput = true;
          break;
        }
        //noinspection ConstantConditions
        if (polymorphicOutput) break;
      }

      Transition newTransition = petrinet.createTransition(newTransitionName.toString());
      for (Place p : polyInputs) {
        // NOTE: why is the weight of the flow restricted to 1?
        addFlow(p.getId(), newTransitionName.toString(), 1);
      }

      for (Flow f : t.getPostsetEdges()) {
        Place p = f.getPlace();
        int w = f.getWeight();
        petrinet.createFlow(newTransition, p, w);
      }
      dict.put(newTransitionName.toString(), dict.get(t.getId()));

      if (polymorphicOutput) {

        for (Flow f : t.getPostsetEdges()) {
          Place p = f.getPlace();

          List<Type> subClasses = subDict.get(p.getId());

          for (Type subclass : subClasses) {
            if (!petrinet.containsPlace(subclass.name())) continue;

            String newPolyTransitionName = newTransitionName + "(" + subclass + ")";

            assert (!petrinet.containsTransition(newPolyTransitionName));

            newTransition = petrinet.createTransition(newPolyTransitionName);

            for (Place p2 : polyInputs) {
              addFlow(p2.getId(), newPolyTransitionName, 1);
            }

            int w = f.getWeight();

            petrinet.createFlow(newTransition, petrinet.getPlace(subclass.name()), w);
            dict.put(newPolyTransitionName, dict.get(t.getId()));
          }
        }
      }

    } else {
      Place p = inputs.get(count);

      final List<Type> subClasses = subDict.get(p.getId());

      if (subClasses == null) { // No possible polymophism
        polyInputs.push(p);
        generatePolymophism(t, count + 1, inputs, polyInputs);
        polyInputs.pop();
      } else {
        for (final Type subclass : subClasses) {
          addPlace(subclass.name());

          Place polyClass = petrinet.getPlace(subclass.name());
          polyInputs.push(polyClass);

          generatePolymophism(t, count + 1, inputs, polyInputs);
          polyInputs.pop();
        }
      }
    }
  }

  private void copyPolymorphism() {
    // Handles polymorphism by creating copies for each method that
    // has superclass as input type
    for (Transition t : petrinet.getTransitions()) {
      List<Place> inputs = new ArrayList<>();
      Set<Flow> inEdges = t.getPresetEdges();
      for (Flow f : inEdges) {
        for (int i = 0; i < f.getWeight(); i++) {
          inputs.add(f.getPlace());
        }
      }
      Stack<Place> polyInputs = new Stack<>();
      generatePolymophism(t, 0, inputs, polyInputs);
    }
  }

  // This method handles polymorphism by creating methods that transforms each
  // subclass into its super class
  private void normalPolymorphism() {
    for (final Type subClass : superDict.keySet()) {
      addPlace(subClass.name());

      for (final Type superClass : superDict.get(subClass)) {
        addPlace(superClass.name());

        String methodName = subClass + "IsPolymorphicTo" + superClass;

        petrinet.createTransition(methodName);
        petrinet.createFlow(subClass.name(), methodName, 1);
        petrinet.createFlow(methodName, superClass.name(), 1);
      }
    }
  }

  private void getPolymorphismInformation(
      final ImmutableMultimap<Type, Type> superClassMap,
      final ImmutableMultimap<Type, Type> subClassMap) {
    for (final Type superclass : superClassMap.keySet()) {

      if (!petrinet.containsPlace(superclass.name())) {
        continue;
      }

      final Collection<Type> superClasses = superClassMap.get(superclass);

      if (superClasses.size() != 0) {
        List<Type> superClassList = new ArrayList<>(superClasses);
        superDict.put(superclass, superClassList);
      }
    }

    for (final Type subclass : subClassMap.keySet()) {
      if (!petrinet.containsPlace(subclass.name())) {
        continue;
      }

      final Collection<Type> subClasses = subClassMap.get(subclass);

      if (subClasses.size() != 0) {
        List<Type> subClassList = new ArrayList<>(subClasses);
        subDict.put(subclass, subClassList);
      }
    }
  }

  private void addPlace(final String placeID) {
    // placeID = placeID.replace("$", ".");
    try {
      petrinet.getPlace(placeID);
    } catch (NoSuchNodeException e) {
      petrinet.createPlace(placeID);
      petrinet.createTransition(placeID + "Clone");
      // petrinet.getPlace(placeID).setMaxToken(2);
      petrinet.createFlow(placeID, placeID + "Clone", 1);
      petrinet.createFlow(placeID + "Clone", placeID, 2);
    }
  }

  private void addFlow(final String ID1, final String ID2, final int weight) {
    Flow f;
    try {
      f = petrinet.getFlow(ID1, ID2);
      f.setWeight(f.getWeight() + weight);
    } catch (NoSuchEdgeException e) {
      petrinet.createFlow(ID1, ID2, weight);
    }
  }

  // TODO: clean the code in this method
  private void addVoidTransition(final MethodSignature methodSig) {
    String methodname = methodSig.name();
    boolean isStatic = methodSig.isStatic();
    boolean isConstructor = methodSig.isConstructor();
    String className = methodSig.declaringClass().name();
    StringBuilder transitionName = new StringBuilder("(Void)");
    List<Type> args = methodSig.parameterTypes();

    if (isConstructor) {
      transitionName.append(methodname).append("(Constructor)").append("(");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      petrinet.createTransition(transitionName.toString());
    } else if (isStatic) {
      transitionName
          .append("(static)")
          .append(className)
          .append(".")
          .append(methodname)
          .append("(");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      petrinet.createTransition(transitionName.toString());
    } else { // The method is not static, so it has an extra argument
      transitionName.append(className).append(".").append(methodname).append("(");
      transitionName.append(className).append(" ");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      petrinet.createTransition(transitionName.toString());

      addPlace(className);
      addFlow(className, transitionName.toString(), 1);
    }
    dict.put(transitionName.toString(), methodSig); // add signature into map

    for (Type t : args) {
      addPlace(t.toString());
      addFlow(t.toString(), transitionName.toString(), 1);
    }

    // add place for the return type
    Type retType = methodSig.returnType();
    assert (retType.toString() == "void");
    if (retType.toString() != "void") {
      addPlace(retType.toString());
      addFlow(transitionName.toString(), retType.toString(), 1);
    } else {
      addPlace("void");
      addFlow(transitionName.toString(), "void", 1);
    }
  }

  private void addTransition(final MethodSignature methodSig) {
    String methodname = methodSig.name();
    boolean isStatic = methodSig.isStatic();
    boolean isConstructor = methodSig.isConstructor();
    String className = methodSig.declaringClass().name();
    StringBuilder transitionName;
    List<Type> args = methodSig.parameterTypes();

    if (isConstructor) {
      transitionName = new StringBuilder(methodname + "(Constructor)" + "(");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      // FIXME: fix this potential bug later; triggered in javax.mail
      if (!petrinet.containsTransition(transitionName.toString()))
        petrinet.createTransition(transitionName.toString());
    } else if (isStatic) {
      transitionName = new StringBuilder("(static)" + className + "." + methodname + "(");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      // FIXME: fix this potential bug later; triggered in javax.mail
      if (!petrinet.containsTransition(transitionName.toString()))
        petrinet.createTransition(transitionName.toString());
    } else { // The method is not static, so it has an extra argument
      transitionName = new StringBuilder(className + "." + methodname + "(");
      transitionName.append(className).append(" ");
      for (Type t : args) {
        transitionName.append(t.toString()).append(" ");
      }
      transitionName.append(")");
      transitionName.append(methodSig.returnType());
      // FIXME: fix this potential bug later; triggered in javax.mail
      if (!petrinet.containsTransition(transitionName.toString())) {
        petrinet.createTransition(transitionName.toString());

        addPlace(className);
        addFlow(className, transitionName.toString(), 1);
      }
    }
    dict.put(transitionName.toString(), methodSig); // add signature into map

    for (Type t : args) {
      addPlace(t.toString());
      addFlow(t.toString(), transitionName.toString(), 1);
    }

    // add place for the return type
    Type retType = methodSig.returnType();
    if (retType.toString() != "void") {
      addPlace(retType.toString());
      addFlow(transitionName.toString(), retType.toString(), 1);
    } else {

      if (noSideEffects.contains(className)) {
        addVoidTransition(methodSig);
      }

      addPlace(className);
      addFlow(transitionName.toString(), className, 1);
    }
  }

  public void setMaxTokens(final List<String> inputs) {
    Map<String, Integer> maxTokenMap = new HashMap<>();
    for (Place p : petrinet.getPlaces()) {
      maxTokenMap.put(p.getId(), 1);
    }

    // compute max.
    for (Transition t : petrinet.getTransitions()) {
      for (Flow flow : t.getPresetEdges()) {
        String argType = flow.getSource().getId();
        int num = flow.getWeight();
        assert maxTokenMap.containsKey(argType);
        int curr = maxTokenMap.get(argType);
        if (num > curr) maxTokenMap.put(argType, num);
      }
    }
    for (Place p : petrinet.getPlaces()) {
      assert (maxTokenMap.containsKey(p.getId()));
      p.setMaxToken(maxTokenMap.get(p.getId()) + 1);
    }

    // Update the maxtoken for inputs
    HashMap<Place, Integer> count = new HashMap<>();
    for (String input : inputs) {
      Place p;
      p = petrinet.getPlace(input);
      if (count.containsKey(p)) {
        count.put(p, count.get(p) + 1);
      } else {
        count.put(p, 1);
      }
    }
    for (Place p : count.keySet()) {
      if (count.get(p) > p.getMaxToken()) {
        p.setMaxToken(count.get(p));
      }
    }
  }

  public PetriNet build(
      final ImmutableCollection<MethodSignature> result,
      final ImmutableMultimap<Type, Type> superClassMap,
      final ImmutableMultimap<Type, Type> subClassMap,
      final List<String> inputs,
      final boolean copyPoly
  ) {

    for (MethodSignature k : result) {
      addTransition(k);
    }

    getPolymorphismInformation(superClassMap, subClassMap);

    if (copyPoly) {
      copyPolymorphism();
    } else {
      normalPolymorphism();
    }

    setMaxTokens(inputs);

    return petrinet;
  }
}
