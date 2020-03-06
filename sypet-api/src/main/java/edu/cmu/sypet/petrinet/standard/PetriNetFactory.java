package edu.cmu.sypet.petrinet.standard;

import edu.cmu.sypet.java.Library;

// Dual to the old `BuildNet`
// `PetriNetBuilder` director
public class PetriNetFactory implements edu.cmu.sypet.petrinet.PetriNetFactory<PetriNetRead> {

  private final PetriNetBuilder builder;

  public PetriNetFactory(final PetriNetBuilder builder) {
    this.builder = builder;
  }

  @Override
  public PetriNetRead createFrom(final Library library) {
    throw new UnsupportedOperationException();
  }
}
