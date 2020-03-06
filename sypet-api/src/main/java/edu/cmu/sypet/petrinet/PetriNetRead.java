package edu.cmu.sypet.petrinet;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;

public interface PetriNetRead {

  boolean contains(Type type);

  boolean contains(MethodSignature signature);
}
