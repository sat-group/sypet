package edu.cmu.petrinet.sypet;

abstract class TransitionException extends Exception {
  public BackendTransition transition;

  public TransitionException(final BackendTransition transition) {
    this.transition = transition;
  }
}
