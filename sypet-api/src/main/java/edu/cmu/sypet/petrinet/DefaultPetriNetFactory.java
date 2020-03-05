package edu.cmu.sypet.petrinet;

import edu.cmu.sypet.java.Library;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;

public final class DefaultPetriNetFactory<PNR extends PetriNetRead, PNW extends PetriNetWrite> implements
    PetriNetFactory<PNR, PNW> {

  private final PNW net;

  private DefaultPetriNetFactory(PNW net) {
    this.net = net;
  }

  @Override
  public PNR createFrom(Library library) {
    throw new UnsupportedOperationException();
  }

  final void addPlace(Type type) {
    throw new UnsupportedOperationException();
  }

  final void addTransition(MethodSignature signature) {
    throw new UnsupportedOperationException();
  }

  final void addVoidTransition(MethodSignature signature) {
    throw new UnsupportedOperationException();
  }

  final void addCloneTransition(MethodSignature signature) {
    throw new UnsupportedOperationException();
  }

  final void addCastTransition(MethodSignature signature) {
    throw new UnsupportedOperationException();
  }
}
