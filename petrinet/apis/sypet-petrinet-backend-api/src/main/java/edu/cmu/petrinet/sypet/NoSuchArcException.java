package edu.cmu.petrinet.sypet;

public final class NoSuchArcException extends Exception {
  public final BackendNode source;
  public final BackendNode target;

  public NoSuchArcException(final BackendPlace place, final BackendTransition transition) {
    this.source = place;
    this.target = transition;
  }

  public NoSuchArcException(final BackendTransition transition, final BackendPlace place) {
    this.source = transition;
    this.target = place;
  }
}
