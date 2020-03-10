package edu.cmu.petrinet.sypet;

public class TransitionAlreadyExistsException extends PNBInternalException {

  public TransitionAlreadyExistsException(MethodSignature signature) {
    super("Transition \"" + signature + "\" already exists in the net.");
  }
}
