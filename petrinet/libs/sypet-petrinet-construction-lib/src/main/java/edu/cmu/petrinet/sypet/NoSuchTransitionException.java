package edu.cmu.petrinet.sypet;

public class NoSuchTransitionException extends PNBInternalException {

  public NoSuchTransitionException(MethodSignature signature) {
    super("Transition \"" + signature + "\" does not exist in the net.");
  }
}
