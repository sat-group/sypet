package edu.cmu.petrinet.sypet;

final class PetriNet implements SyPetriNet {

  private final BackendPetriNet net;

  public PetriNet(BackendPetriNet net) {
    this.net = net;
  }

  @Override
  public boolean contains(final Type type) {
    return this.net.contains(new PlaceAdapter(type));
  }

  @Override
  public boolean contains(final MethodTransition signature) {
    return this.net.contains(new TransitionAdapter(signature));
  }

  @Override
  public boolean containsArc(Type type, MethodTransition signature) {
    final BackendPlace from = new PlaceAdapter(type);
    final BackendTransition to = new TransitionAdapter(signature);

    return this.net.contains(new InputArcAdapter(from, to, null));
  }

  @Override
  public boolean containsArc(MethodTransition signature, Type type) {
    final BackendTransition from = new TransitionAdapter(signature);
    final BackendPlace to = new PlaceAdapter(type);

    return this.net.contains(new OutputArcAdapter(from, to, null));
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
