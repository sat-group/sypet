package edu.cmu.sypet.petrinet.middleware.standard;

import edu.cmu.sypet.java.Library;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.middleware.MiddlewarePetriNet;

// `PetriNetBuilder` director
public class MiddlewarePetriNetFactory<T extends Type, MS extends MethodSignature<T>>
    implements edu.cmu.sypet.petrinet.middleware.MiddlewarePetriNetFactory<
        MiddlewarePetriNet<T, MS>> {

  private final PetriNetBuilder<T, MS> builder;

  public MiddlewarePetriNetFactory(final PetriNetBuilder<T, MS> builder) {
    this.builder = builder;
  }

  @Override
  public MiddlewarePetriNet<T, MS> createFrom(final Library library) {
    throw new UnsupportedOperationException();
  }
}
