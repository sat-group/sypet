package edu.cmu.sypet.petrinet;

import com.google.common.collect.ImmutableMultimap;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Build a petri net from a set of libraries.
 */
public final class BuildNet {

  //region Fields
  /**
   * The Petri net that is to be built.
   */
  private final edu.cmu.sypet.petrinet.PetriNet petriNet;

  /**
   * A map from a transition name to a method signature.
   */
  private final Map<String, MethodSignature> dict;

  /**
   * Direct correspondent to {@code edu.cmu.sypet.SyPetAPI#superclassMap} but only with the keys
   * (types) that are places in the petri net.
   */
  private final Map<String, List<String>> superDict;

  /**
   * Direct correspondent to {@code edu.cmu.sypet.SyPetAPI#subclassMap}, but only with the keys
   * (types) that are places in the petri net.
   */
  private final Map<String, List<String>> subDict;

  /**
   * Methods for which we can ignore the return type.
   */
  private final List<String> noSideEffects;
  //endregion

  //region Constructors and Build Methods
  BuildNet(
      final PetriNet petriNet,
      final Map<String, MethodSignature> dict,
      final Map<String, List<String>> superDict,
      final Map<String, List<String>> subDic,
      final List<String> noSideEffects
  ) {
    this.petriNet = petriNet;
    this.dict = dict;
    this.superDict = superDict;
    this.subDict = subDic;
    this.noSideEffects = noSideEffects;
  }

  /**
   * @param petriNet        the empty Petri net on which to build
   * @param noSideEffects   the methods for which we can ignore the return type
   */
  public BuildNet(
      final PetriNet petriNet,
      final List<String> noSideEffects
  ) {
    this(
        petriNet,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        noSideEffects);
  }

  public PetriNet build(
      final List<MethodSignature> result,
      final ImmutableMultimap<String, String> superClassMap,
      final ImmutableMultimap<String, String> subClassMap,
      final List<String> inputs
  ) {
    for (final MethodSignature methodSignature : result) {
      addTransition(methodSignature);
    }

    getPolymorphismInformation(superClassMap, subClassMap);
    addUpCastTransitions();
    setMaxTokens(inputs);

    return petriNet;
  }
  //endregion

  //region Getters
  public Map<String, MethodSignature> dict() {
    return dict;
  }
  //endregion

  //region Methods Used to Retrieve Polymorphism Information

  /**
   * Adds new transitions in {@link BuildNet#petriNet} for each class to each of its super classes.
   */
  private void addUpCastTransitions() {
    for (final Map.Entry<String, List<String>> entry : superDict.entrySet()) {
      final String subclass = entry.getKey();
      addPlace(subclass);

      final List<String> superclasses = entry.getValue();
      for (final String superclass : superclasses) {
        addPlace(superclass);

        final String methodName = subclass + "IsPolymorphicTo" + superclass;

        petriNet.createTransition(methodName);
        petriNet.createFlow(subclass, methodName, 1);
        petriNet.createFlow(methodName, superclass, 1);
      }
    }
  }

  /**
   * Populates {@code superDict} and {@code subDict} with the entries of {@code superClassMap} and
   * {@code subClassMap}, respectively, but only with the types that are places in the petri net.
   */
  private void getPolymorphismInformation(
      ImmutableMultimap<String, String> superClassMap,
      ImmutableMultimap<String, String> subClassMap
  ) {
    // Filter keys (types) that correspond to places in the petri net.
    // Apart from that, `superDict` is exactly the same as `superclassMap` in `SyPetAPI`.
    for (final String type : superClassMap.keySet()) {
      if (!petriNet.containsPlace(type)) {
        continue;
      }

      final Collection<String> superClasses = superClassMap.get(type);
      if (superClasses.size() != 0) {
        superDict.put(type, new ArrayList<>(superClasses));
      }
    }

    // Exactly the same as above, but for `subclassMap`.
    for (String s : subClassMap.keySet()) {
      if (!petriNet.containsPlace(s)) {
        continue;
      }

      Collection<String> subClasses = subClassMap.get(s);
      if (subClasses.size() != 0) {
        List<String> subClassList = new ArrayList<>(subClasses);
        subDict.put(s, subClassList);
      }
    }
  }
  //endregion

  //region Methods for Petri Net Construction
  /**
   * Adds a new place to {@link BuildNet#petriNet} with id {@code placeID} if one does not exist
   * already.
   */
  private void addPlace(final String placeID) {
    try {
      petriNet.getPlace(placeID);
    } catch (PetriNet.NoSuchPlaceException e) {
      petriNet.createPlace(placeID);
      addCloneTransition(placeID);
    }
  }

  /**
   * Adds a clone transition to the place with id {@code placeID} in {@link BuildNet#petriNet}.
   */
  private void addCloneTransition(final String placeID) {
    final String transitionID = placeID + "Clone";

    petriNet.createTransition(transitionID);
    petriNet.createFlow(placeID, transitionID, 1);
    petriNet.createFlow(transitionID, placeID, 2);
  }

  /**
   * Adds a new flow in {@link BuildNet#petriNet} between the place (resp. transition) {@code id1}
   * and the transition (resp. place) {@code id2}, if one does not exist already. If a flow already
   * exists, it adds {@code weight} to the weight of that flow.
   */
  private void addFlow(final String id1, final String id2, final int weight) {
    try {
      final edu.cmu.sypet.petrinet.PetriNet.Flow f = petriNet.getFlow(id1, id2);
      f.setWeight(f.getWeight() + weight);
    } catch (PetriNet.NoSuchFlowException e) {
      petriNet.createFlow(id1, id2, weight);
    }
  }

  /**
   * Creates a transition in the petri net corresponding to the {@code methodSignature}. Also
   * creates places in the petri net for the types of the parameters and the return type, in case
   * they do not exist yet, as well as for the declaring class, in case the method is not static and
   * not a constructor.
   */
  private void addTransition(final MethodSignature methodSignature) {
    final String className = methodSignature.declaringClass().name();
    final List<Type> args = methodSignature.parameterTypes();

    final String transitionName = makeTransitionName(methodSignature, className, args);
    petriNet.createTransition(transitionName);

    if (!methodSignature.isConstructor() && !methodSignature.isStatic()) {
      addPlace(className);
      addFlow(className, transitionName, 1);
    }

    // Put the signature in the map.
    dict.put(transitionName, methodSignature);

    for (final Type type : args) {
      addPlace(type.name());
      addFlow(type.name(), transitionName, 1);
    }

    // Add place for the return type.
    final Type returnType = methodSignature.returnType();
    if (returnType.toString() != "void") {
      addPlace(returnType.name());
      addFlow(transitionName, returnType.name(), 1);
    } else { // Return type is `void`.
      if (noSideEffects.contains(methodSignature.name())) {
        addVoidTransition(methodSignature);
      }

      addPlace(className);
      addFlow(transitionName, className, 1);
    }
  }

  // TODO: clean the code in this method
  // This is almost a copy of `addTransition` and needs explanation.
  private void addVoidTransition(MethodSignature methodSig) {
    final String className = methodSig.declaringClass().name();
    final List<Type> args = methodSig.parameterTypes();

    final String transitionName = "(Void)" +
        makeTransitionName(methodSig, className, args);
    petriNet.createTransition(transitionName);

    if (!methodSig.isConstructor() && !methodSig.isStatic()) {
      addPlace(className);
      addFlow(className, transitionName, 1);
    }

    // add signature into map
    dict.put(transitionName, methodSig);

    for (Type t : args) {
      addPlace(t.toString());
      addFlow(t.toString(), transitionName, 1);
    }

    // add place for the return type
    Type retType = methodSig.returnType();

    assert (retType.toString() == "void");

    if (retType.toString() != "void") {
      addPlace(retType.toString());
      addFlow(transitionName, retType.toString(), 1);
    } else {
      addPlace("void");
      addFlow(transitionName, "void", 1);
    }
  }

  /**
   * Creates an appropriate transition name, depending on whether the {@code methodSig} is a
   * constructor, static or a non-static method.
   */
  private String makeTransitionName(
      final MethodSignature methodSig,
      final String className,
      final List<Type> args
  ) {
    final String methodName = methodSig.name();
    final StringBuilder transitionNameBuilder = new StringBuilder();

    if (methodSig.isConstructor()) {
      transitionNameBuilder
          .append(methodName)
          .append("(Constructor)")
          .append("(");
    } else if (methodSig.isStatic()) {
      transitionNameBuilder
          .append("(static)")
          .append(className)
          .append(".")
          .append(methodName)
          .append("(");
    } else { // The method is not static, so it has an extra argument
      transitionNameBuilder
          .append(className)
          .append(".")
          .append(methodName)
          .append("(")
          .append(className)
          .append(" ");
    }

    for (final Type type : args) {
      transitionNameBuilder
          .append(type.name())
          .append(" ");
    }

    transitionNameBuilder
        .append(")")
        .append(methodSig.returnType().name());

    return transitionNameBuilder.toString();
  }

  /**
   * Sets the number of tokens for each place in {@link BuildNet#petriNet}.
   */
  private void setMaxTokens(final List<String> inputs) {
    final Map<String, Integer> maxTokenMap = new HashMap<>();

    // Map each place in the petri net to 1.
    for (final PetriNet.Place place : petriNet.getPlaces()) {
      maxTokenMap.put(place.getId(), 1);
    }

    // For each input type, compute the maximum weight on any of its outgoing flows. This
    // corresponds to the maximum number of arguments of that type that any method (transition) may
    // require.
    for (final PetriNet.Transition transition : petriNet.getTransitions()) {
      for (final PetriNet.Flow flow : transition.getPresetEdges()) {
        final String inputType = flow.getPlace().getId();
        final int flowWeight = flow.getWeight();

        if (flowWeight > maxTokenMap.get(inputType)) {
          maxTokenMap.put(inputType, flowWeight);
        }
      }
    }

    // Set max token of each place in the petri net to their correspondent value in the map plus 1.
    // See an explanation for this on section 6.2 ("Ensuring Termination") of the paper.
    for (final PetriNet.Place place : petriNet.getPlaces()) {
      place.setMaxToken(maxTokenMap.get(place.getId()) + 1);
    }

    // Update the maximum number of tokens for each input type.
    final Map<PetriNet.Place, Integer> placeCount = inputs.stream()
        .collect(Collectors.groupingBy(petriNet::getPlace, Collectors.summingInt(x -> 1)));

    for (final PetriNet.Place place : placeCount.keySet()) {
      // TODO Ruben Why is this set to the maximum and not to placeCount.get(place)?
      place.setMaxToken(Math.max(placeCount.get(place), place.getMaxToken()));
    }
  }
  //endregion

}
