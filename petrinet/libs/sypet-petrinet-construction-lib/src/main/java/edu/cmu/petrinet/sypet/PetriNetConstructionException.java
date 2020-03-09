package edu.cmu.petrinet.sypet;

public class PetriNetConstructionException extends RuntimeException {

  public PetriNetConstructionException(NoSuchTypeException e) {
    super("Internal error while constructing Petri net: " + e.getMessage());
  }
}
