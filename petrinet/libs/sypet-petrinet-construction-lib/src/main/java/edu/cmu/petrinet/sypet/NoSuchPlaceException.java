package edu.cmu.petrinet.sypet;

public class NoSuchPlaceException extends PNBInternalException {

  public NoSuchPlaceException(final Type type) {
    super("Place \"" + type + "\" does not exist in the net.");
  }
}
