package edu.cmu.petrinet.sypet;

public final class ArcAlreadyExistsException extends ArcException {

  public ArcAlreadyExistsException(BackendPlace place, BackendTransition transition) {
    super(place, transition);
  }

  public ArcAlreadyExistsException(BackendTransition transition, BackendPlace place) {
    super(transition, place);
  }
}
