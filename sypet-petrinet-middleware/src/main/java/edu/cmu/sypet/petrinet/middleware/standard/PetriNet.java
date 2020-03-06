package edu.cmu.sypet.petrinet.middleware.standard;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.backend.BackendPetriNet;
import edu.cmu.sypet.petrinet.middleware.MiddlewarePetriNet;

final class PetriNet<T extends Type, MS extends MethodSignature<T>> implements
    MiddlewarePetriNet<T, MS> {

  private final BackendPetriNet<T, MS> net;

  public PetriNet(BackendPetriNet<T, MS> net) {
    this.net = net;
  }

  @Override
  public boolean containsPlace(final T type) {
    return net.containsPlace(type);
  }

  @Override
  public boolean containsTransition(final MS signature) {
    return net.containsTransition(signature);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PetriNet<?, ?> that = (PetriNet<?, ?>) o;

    return net.equals(that.net);
  }

  @Override
  public int hashCode() {
    return net.hashCode();
  }
}
