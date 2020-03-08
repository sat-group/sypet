package edu.cmu.sypet.petrinet.backend;

public interface BackendPetriNetFactory<Place, Transition> {

  BackendPetriNet<Place, Transition> create();
}
