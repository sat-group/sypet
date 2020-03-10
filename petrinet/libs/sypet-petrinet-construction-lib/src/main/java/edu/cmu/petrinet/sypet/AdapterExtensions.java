package edu.cmu.petrinet.sypet;

class AdapterExtensions {

  public AdapterExtensions() {
  }

  static <T> BackendPlace<Type<T>> newPlace(Type<T> type) {
    return (BackendPlace<Type<T>>) new PlaceAdapter<>(type);
  }

  static <T, U> BackendTransition<MethodSignature<T, U>> newTransition(MethodSignature<T, U> signature) {
    return (BackendTransition<MethodSignature<T, U>>) new TransitionAdapter<>(signature);
  }
}
