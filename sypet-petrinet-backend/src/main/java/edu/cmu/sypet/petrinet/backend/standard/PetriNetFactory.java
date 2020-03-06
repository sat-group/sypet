package edu.cmu.sypet.petrinet.backend.standard;

import edu.cmu.sypet.petrinet.backend.BackendPetriNet;
import edu.cmu.sypet.petrinet.backend.BackendPetriNetFactory;
import java.util.HashMap;
import java.util.HashSet;

public class PetriNetFactory<Place, Transition> implements
    BackendPetriNetFactory<Place, Transition> {

  @Override
  public BackendPetriNet<Place, Transition> create() {
    return new PetriNet<>(
        new HashSet<>(),
        new HashSet<>(),
        new HashMap<>(),
        new HashMap<>());
  }
}
