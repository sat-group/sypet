package edu.cmu.petrinet.sypet;

public final class NoSuchPlaceException extends PlaceException {
  public NoSuchPlaceException(BackendPlace place) {
    super(place);
  }
}
