package edu.cmu.petrinet.sypet;

final class PetriNet implements SyPetriNet {

  private final BackendPetriNet<Type, MethodSignature> net;

  public PetriNet(BackendPetriNet<Type, MethodSignature> net) {
    this.net = net;
  }

  @Override
  public boolean containsPlace(final Type type) {
    return net.containsPlace(type);
  }

  @Override
  public boolean containsTransition(final MethodSignature signature) {
    return net.containsTransition(signature);
  }

  @Override
  public boolean isTypeAdjacentToSignature(Type type, MethodSignature signature) {
    return this.net.isPlaceAdjacentToTransition(type, signature);
  }

  @Override
  public boolean isSignatureAdjacentToType(MethodSignature signature, Type type) {
    return this.net.isTransitionAdjacentToPlace(signature, type);
  }

  @Override
  public int getArcWeightFromTypeToSignature(Type type, MethodSignature signature) {
    return this.net.getArcWeightFromTypeToSignature(type, signature);
  }

  @Override
  public int getArcWeightFromSignatureToType(MethodSignature signature, Type type) {
    return this.net.getArcWeightFromSignatureToType(signature, type);
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
