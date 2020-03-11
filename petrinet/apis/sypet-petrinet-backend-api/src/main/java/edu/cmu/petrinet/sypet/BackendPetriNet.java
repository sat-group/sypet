package edu.cmu.petrinet.sypet;

public interface BackendPetriNet {

  void addNode(BackendPlace place) throws PlaceAlreadyExistsException;

  void addNode(BackendTransition transition) throws TransitionAlreadyExistsException;

  void addArc(BackendPlace place, BackendTransition transition, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  void addArc(BackendTransition transition, BackendPlace place, Integer weight) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  boolean containsNode(BackendPlace place);

  boolean containsNode(BackendTransition transition);

  boolean containsArc(BackendPlace place, BackendTransition transition);

  boolean containsArc(BackendTransition transition, BackendPlace place);

  int getArcWeight(BackendPlace place, BackendTransition transition) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;

  int getArcWeight(BackendTransition transition, BackendPlace place) throws
      NoSuchArcException,
      NoSuchPlaceException,
      NoSuchTransitionException;
}
