package edu.cmu.petrinet.sypet;

final class PlaceAdapter<T> extends NodeAdapter<Type<T>, T> implements BackendPlace<Type<T>> {

  PlaceAdapter(Type<T> identifiable) {
    super(identifiable);
  }
}
