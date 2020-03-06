package edu.cmu.sypet.petrinet.frontend.standard;

import edu.cmu.sypet.java.Library;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;

// Dual to the old `BuildNet`
// `PetriNetBuilder` director
public class PetriNetFactory<T extends Type, MS extends MethodSignature<T>> implements
    edu.cmu.sypet.petrinet.frontend.PetriNetFactory<FrontendPetriNet<T, MS>> {

  private final PetriNetBuilder<T, MS> builder;

  public PetriNetFactory(final PetriNetBuilder<T, MS> builder) {
    this.builder = builder;
  }

  @Override
  public FrontendPetriNet<T, MS> createFrom(final Library library) {
    throw new UnsupportedOperationException();
  }
}
