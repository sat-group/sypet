package edu.cmu.petrinet.sypet;

public final class TransitionAlreadyExistsException extends Exception {
  public final BackendTransition transition;

  public TransitionAlreadyExistsException(final BackendTransition transition) {
    this.transition = transition;
  }
}
