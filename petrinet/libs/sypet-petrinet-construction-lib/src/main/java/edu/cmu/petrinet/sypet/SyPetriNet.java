package edu.cmu.petrinet.sypet;

public interface SyPetriNet<T extends Type, MS extends MethodSignature> {

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  boolean isTypeAdjacentToSignature(T type, MS signature);

  boolean isSignatureAdjacentToType(MS signature, T type);

  int getArcWeightFromTypeToSignature(T type, MS signature);

  int getArcWeightFromSignatureToType(MS signature, T type);
}
