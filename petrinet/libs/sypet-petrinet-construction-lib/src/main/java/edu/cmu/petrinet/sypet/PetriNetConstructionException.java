package edu.cmu.petrinet.sypet;

public class PetriNetConstructionException extends RuntimeException {

  public PetriNetConstructionException(NoSuchPlaceException e) {
    super("Internal error while constructing Petri net: " + e.getMessage());
  }
}
