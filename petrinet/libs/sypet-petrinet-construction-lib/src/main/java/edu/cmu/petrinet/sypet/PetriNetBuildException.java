package edu.cmu.petrinet.sypet;

public class PetriNetBuildException extends RuntimeException {

  PetriNetBuildException(PNBInternalException e) {
    super("Internal error while building the Petri net: " + e.getMessage());
  }
}
