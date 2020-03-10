package edu.cmu.petrinet.sypet;

public interface BackendPetriNet<T extends Type, MS extends MethodSignature> {

  void addPlace(T type) throws
      PlaceAlreadyExistsException;

  void addTransition(MS signature) throws
      TransitionAlreadyExistsException;

  void addArcFromPlaceToTransition(T type, MS signature, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  void addArcFromTransitionToPlace(MS transition, T type, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  boolean isPlaceAdjacentToTransition(T type, MS signature);

  boolean isTransitionAdjacentToPlace(MS signature, T type);

  int getArcWeightFromTypeToSignature(T type, MS signature) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  int getArcWeightFromSignatureToType(MS signature, T type) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;
}
