package edu.cmu.petrinet.sypet;

public final class NoSuchTransitionException extends Exception {
  public final BackendTransition transition;

  public NoSuchTransitionException(final BackendTransition transition) {
    this.transition = transition;
  }
}
