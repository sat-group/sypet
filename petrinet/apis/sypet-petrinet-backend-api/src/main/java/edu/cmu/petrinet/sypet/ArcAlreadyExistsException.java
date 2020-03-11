package edu.cmu.petrinet.sypet;

public final class ArcAlreadyExistsException extends Exception {
  public final BackendPlace place;
  public final BackendTransition transition;

  public ArcAlreadyExistsException(final BackendPlace place, final BackendTransition transition) {
    this.place = place;
    this.transition = transition;
  }
}
