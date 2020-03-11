package edu.cmu.petrinet.sypet;

public final class NoSuchTransitionException extends TransitionException {
  public NoSuchTransitionException(BackendTransition transition) {
    super(transition);
  }
}
