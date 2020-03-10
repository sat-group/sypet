package edu.cmu.petrinet.sypet;

public interface BackendPetriNet<T, U> {

  void addNode(BackendPlace<T> place) throws PlaceAlreadyExistsException;

  void addNode(BackendTransition<U> transition) throws TransitionAlreadyExistsException;

  void addArc(BackendPlace<T> place, BackendTransition<U> transition, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  void addArc(BackendTransition<U> transition, BackendPlace<T> place, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  boolean containsNode(BackendPlace<T> place);

  boolean containsNode(BackendTransition<U> transition);

  boolean containsArc(BackendPlace<T> place, BackendTransition<U> transition);

  boolean containsArc(BackendTransition<U> transition, BackendPlace<T> place);

  int getArcWeight(BackendPlace<T> place, BackendTransition<U> transition) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  int getArcWeight(BackendTransition<U> transition, BackendPlace<T> place) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;
}
