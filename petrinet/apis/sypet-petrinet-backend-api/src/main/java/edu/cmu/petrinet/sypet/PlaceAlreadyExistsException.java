package edu.cmu.petrinet.sypet;

public final class PlaceAlreadyExistsException extends PlaceException {
  public PlaceAlreadyExistsException(BackendPlace place) {
    super(place);
  }
}
