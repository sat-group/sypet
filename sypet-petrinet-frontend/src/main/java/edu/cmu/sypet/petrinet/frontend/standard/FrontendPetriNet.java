package edu.cmu.sypet.petrinet.frontend.standard;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.backend.BackendPetriNet;

final class FrontendPetriNet<T extends Type, MS extends MethodSignature<T>> implements
    edu.cmu.sypet.petrinet.frontend.FrontendPetriNet<T, MS> {

  private final BackendPetriNet<T, MS> net;

  public FrontendPetriNet(BackendPetriNet<T, MS> net) {
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

    FrontendPetriNet<?, ?> that = (FrontendPetriNet<?, ?>) o;

    return net.equals(that.net);
  }

  @Override
  public int hashCode() {
    return net.hashCode();
  }
}
