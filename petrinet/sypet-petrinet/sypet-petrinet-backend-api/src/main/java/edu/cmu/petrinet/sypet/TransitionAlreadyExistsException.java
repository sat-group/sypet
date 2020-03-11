package edu.cmu.petrinet.sypet;

public final class TransitionAlreadyExistsException extends TransitionException {
  public TransitionAlreadyExistsException(BackendTransition transition) {
    super(transition);
  }
}
