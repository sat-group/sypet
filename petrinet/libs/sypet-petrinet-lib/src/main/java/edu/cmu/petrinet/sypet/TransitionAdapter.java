package edu.cmu.petrinet.sypet;

final class TransitionAdapter<T, U>
    extends NodeAdapter<MethodSignature<T, U>, U>
    implements BackendTransition<MethodSignature<T, U>> {

  TransitionAdapter(MethodSignature<T, U> identifiable) {
    super(identifiable);
  }
}
