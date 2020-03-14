package edu.cmu.petrinet.sypet;

final class TransitionAdapter extends NodeAdapter<Transition> implements BackendTransition {

  TransitionAdapter(Transition identifiable) {
    super(identifiable);
  }
}
