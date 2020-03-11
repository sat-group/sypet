package edu.cmu.petrinet.sypet;

abstract class ArcException extends Exception {
  public BackendNode source;
  public BackendNode target;

  public ArcException(final BackendPlace place, final BackendTransition transition) {
    this.source = place;
    this.target = transition;
  }

  public ArcException(final BackendTransition transition, final BackendPlace place) {
    this.source = transition;
    this.target = place;
  }
}
