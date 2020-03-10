package edu.cmu.petrinet.sypet;

public class TransitionAlreadyExistsException extends PNBInternalException {

  public TransitionAlreadyExistsException(final BackendTransition transition) {
    super("Transition \"" + transition + "\" already exists in the net.");
  }
}
