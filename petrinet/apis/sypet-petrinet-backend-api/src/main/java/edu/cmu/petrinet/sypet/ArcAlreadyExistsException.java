package edu.cmu.petrinet.sypet;

public final class ArcAlreadyExistsException extends Exception {
  public final BackendNode source;
  public final BackendNode target;

  public ArcAlreadyExistsException(final BackendPlace place, final BackendTransition transition) {
    this.source = place;
    this.target = transition;
  }

  public ArcAlreadyExistsException(final BackendTransition transition, final BackendPlace place) {
    this.source = transition;
    this.target = place;
  }
}
