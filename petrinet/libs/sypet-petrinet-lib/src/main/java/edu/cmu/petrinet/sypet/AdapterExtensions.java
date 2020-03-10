package edu.cmu.petrinet.sypet;

class AdapterExtensions {

  public AdapterExtensions() {
  }

  static <T> BackendPlace<Type<T>> newPlaceAdapter(Type<T> type) {
    return (BackendPlace<Type<T>>) new PlaceAdapter<>(type);
  }

  static <T, U> BackendTransition<MethodSignature<T, U>> newTransitionAdapter(MethodSignature<T, U> signature) {
    return (BackendTransition<MethodSignature<T, U>>) new TransitionAdapter<>(signature);
  }
}
