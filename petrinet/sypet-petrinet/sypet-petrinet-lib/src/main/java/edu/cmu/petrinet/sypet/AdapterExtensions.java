package edu.cmu.petrinet.sypet;

class AdapterExtensions {

  public AdapterExtensions() {
  }

  static BackendPlace newPlaceAdapter(Type type) {
    return new PlaceAdapter(type);
  }

  static BackendTransition newTransitionAdapter(MethodSignature signature) {
    return new TransitionAdapter(signature);
  }
}
