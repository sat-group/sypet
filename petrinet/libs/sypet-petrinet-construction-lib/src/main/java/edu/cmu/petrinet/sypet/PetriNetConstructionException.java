package edu.cmu.petrinet.sypet;

public class PetriNetConstructionException extends RuntimeException {

  public PetriNetConstructionException(BadCastException e) {

  }

  public PetriNetConstructionException(NoSuchPlaceException e) {
    super("Internal error while constructing Petri net: " + e.getMessage());
  }
}
