package edu.cmu.petrinet.sypet;

public final class NoSuchArcException extends ArcException {

  public NoSuchArcException(BackendPlace place, BackendTransition transition) {
    super(place, transition);
  }

  public NoSuchArcException(BackendTransition transition, BackendPlace place) {
    super(transition, place);
  }
}
