package edu.cmu.petrinet.sypet;

public class NoSuchPlaceException extends PNBInternalException {

  public NoSuchPlaceException(final BackendPlace place) {
    super("Place \"" + place + "\" does not exist in the net.");
  }
}
