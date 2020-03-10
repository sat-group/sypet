package edu.cmu.petrinet.sypet;

public class PlaceAlreadyExistsException extends PNBInternalException {

  public PlaceAlreadyExistsException(final BackendPlace place) {
    super("Place \"" + place + "\" already exists in the net.");
  }
}
