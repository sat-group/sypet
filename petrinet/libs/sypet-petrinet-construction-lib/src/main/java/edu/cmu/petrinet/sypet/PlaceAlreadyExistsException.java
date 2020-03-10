package edu.cmu.petrinet.sypet;

public class PlaceAlreadyExistsException extends PNBInternalException {

  public PlaceAlreadyExistsException(final Type type) {
    super("Place \"" + type + "\" already exists in the net.");
  }
}
