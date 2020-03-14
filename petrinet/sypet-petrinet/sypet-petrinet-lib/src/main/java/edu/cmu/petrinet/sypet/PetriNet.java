package edu.cmu.petrinet.sypet;

import static edu.cmu.petrinet.sypet.AdapterExtensions.newPlaceAdapter;
import static edu.cmu.petrinet.sypet.AdapterExtensions.newTransitionAdapter;

final class PetriNet implements SyPetriNet {

  private final BackendPetriNet net;

  public PetriNet(BackendPetriNet net) {
    this.net = net;
  }

  @Override
  public boolean contains(final Type type) {
    return this.net.containsNode(newPlaceAdapter(type));
  }

  @Override
  public boolean contains(final MethodTransition signature) {
    return this.net.containsNode(newTransitionAdapter(signature));
  }

  @Override
  public boolean containsArc(Type type, MethodTransition signature) {
    return this.net.containsArc(newPlaceAdapter(type), newTransitionAdapter(signature));
  }

  @Override
  public boolean containsArc(MethodTransition signature, Type type) {
    return this.net.containsArc(newTransitionAdapter(signature), newPlaceAdapter(type));
  }

  @Override
  public int getArcWeight(Type type, MethodTransition signature) {
    try {
      return this.net.getArcWeight(newPlaceAdapter(type), newTransitionAdapter(signature));
    } catch (NoSuchArcException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchPlaceException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchTransitionException e) {
      throw new PetriNetBuildException(e);
    }
  }

  @Override
  public int getArcWeight(MethodTransition signature, Type type) {
    try {
      return this.net.getArcWeight(newTransitionAdapter(signature), newPlaceAdapter(type));
    } catch (NoSuchArcException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchPlaceException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchTransitionException e) {
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
