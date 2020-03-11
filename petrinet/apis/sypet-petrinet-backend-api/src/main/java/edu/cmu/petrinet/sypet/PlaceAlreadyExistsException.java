package edu.cmu.petrinet.sypet;

public final class PlaceAlreadyExistsException extends Exception {
  public final BackendPlace place;

  public PlaceAlreadyExistsException(final BackendPlace place) {
    this.place = place;
  }
}
