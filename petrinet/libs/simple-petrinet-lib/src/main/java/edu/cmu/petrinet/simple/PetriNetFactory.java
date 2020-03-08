package edu.cmu.petrinet.simple;

import java.util.HashMap;
import java.util.HashSet;

public final class PetriNetFactory {

  public final <Place, Transition> SimplePetriNet<Place, Transition> create() {
    return new PetriNet<>(new HashSet<>(), new HashSet<>(), new HashMap<>(), new HashMap<>());
  }
}
