package edu.cmu.petrinet.sypet;

class AdapterExtensions {

  public AdapterExtensions() {
  }

  static BackendPlace newPlaceAdapter(Type type) {
    return new PlaceAdapter(type);
  }

  static BackendTransition newTransitionAdapter(Transition transition) {
    return new TransitionAdapter(transition);
  }
}
