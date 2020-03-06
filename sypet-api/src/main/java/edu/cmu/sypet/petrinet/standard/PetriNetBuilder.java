package edu.cmu.sypet.petrinet.standard;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.PetriNetWrite;

final class PetriNetBuilder {

  private final PetriNetWrite net;

  public PetriNetBuilder(final PetriNetWrite net) {
    this.net = net;
  }

  public final PetriNetBuilder addPlace(final Type type) {
    throw new UnsupportedOperationException();
  }

  public PetriNetBuilder addTransition(final MethodSignature signature) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder addVoidTransition(final MethodSignature signature) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder addCloneTransition(final Type type) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder addCastTransition(final Type from, final Type to) {
    throw new UnsupportedOperationException();
  }

  // this method should return a new copy of the private net
  public final PetriNetRead build() {
    throw new UnsupportedOperationException();
  }
}
