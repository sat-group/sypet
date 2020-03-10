package edu.cmu.petrinet.sypet;

public class NoSuchTransitionException extends PNBInternalException {

  public NoSuchTransitionException(final BackendTransition transition) {
    super("Transition \"" + transition + "\" does not exist in the net.");
  }
}
