package edu.cmu.petrinet.sypet;

import static edu.cmu.petrinet.sypet.AdapterExtensions.newPlaceAdapter;
import static edu.cmu.petrinet.sypet.AdapterExtensions.newTransitionAdapter;

final class PetriNet<T, U> implements SyPetriNet<T, U> {

  private final BackendPetriNet<Type<T>, MethodSignature<T, U>> net;

  public PetriNet(BackendPetriNet<Type<T>, MethodSignature<T, U>> net) {
    this.net = net;
  }

  @Override
  public boolean contains(final Type<T> type) {
    return this.net.containsNode(newPlaceAdapter(type));
  }

  @Override
  public boolean contains(final MethodSignature<T, U>  signature) {
    return this.net.containsNode(newTransitionAdapter(signature));
  }

  @Override
  public boolean containsArc(Type<T> type, MethodSignature<T, U> signature) {
    return this.net.containsArc(newPlaceAdapter(type), newTransitionAdapter(signature));
  }

  @Override
  public boolean containsArc(MethodSignature<T, U> signature, Type<T> type) {
    return this.net.containsArc(newTransitionAdapter(signature), newPlaceAdapter(type));
  }

  @Override
  public int getArcWeight(Type<T> type, MethodSignature<T, U>  signature) {
    try {
      return this.net.getArcWeight(newPlaceAdapter(type), newTransitionAdapter(signature));
    } catch (NoSuchArcException | NoSuchPlaceException | NoSuchTransitionException e) {
      throw new PetriNetBuildException(e);
    }
  }

  @Override
  public int getArcWeight(MethodSignature<T, U>  signature, Type<T> type) {
    try {
      return this.net.getArcWeight(newTransitionAdapter(signature), newPlaceAdapter(type));
    } catch (NoSuchArcException | NoSuchPlaceException | NoSuchTransitionException e) {
      throw new PetriNetBuildException(e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PetriNet petriNet = (PetriNet) o;

    return net.equals(petriNet.net);
  }

  @Override
  public int hashCode() {
    return net.hashCode();
  }
}
