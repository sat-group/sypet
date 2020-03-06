package edu.cmu.sypet.petrinet.standard;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;

public class PetriNetRead implements edu.cmu.sypet.petrinet.PetriNetRead {

  public PetriNetRead() {
  }

  @Override
  public boolean contains(final Type type) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean contains(final MethodSignature signature) {
    throw new UnsupportedOperationException();
  }
}
