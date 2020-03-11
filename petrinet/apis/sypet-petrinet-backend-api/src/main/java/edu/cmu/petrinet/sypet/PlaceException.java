package edu.cmu.petrinet.sypet;

abstract class PlaceException extends Exception {
  public BackendPlace place;

  public PlaceException(final BackendPlace place) {
    this.place = place;
  }
}
