package edu.cmu.petrinet.sypet;

public interface BackendPetriNet<T extends Type, MS extends MethodSignature> {

  void addPlace(T type);

  void addTransition(MS signature);

  void addArcFromPlaceToTransition(T type, MS transition, Integer weight);

  void addArcFromTransitionToPlace(MS transition, T type, Integer weight);

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  boolean isPlaceAdjacentToTransition(T type, MS signature);

  boolean isTransitionAdjacentToPlace(MS signature, T type);

  int getArcWeightFromTypeToSignature(T type, MS signature);

  int getArcWeightFromSignatureToType(MS signature, T type);
}
