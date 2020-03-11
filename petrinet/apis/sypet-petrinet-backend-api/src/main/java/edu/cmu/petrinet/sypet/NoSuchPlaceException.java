package edu.cmu.petrinet.sypet;

public final class NoSuchPlaceException extends Exception {
  public final BackendPlace place;

  public NoSuchPlaceException(final BackendPlace place) {
    this.place = place;
  }
}
