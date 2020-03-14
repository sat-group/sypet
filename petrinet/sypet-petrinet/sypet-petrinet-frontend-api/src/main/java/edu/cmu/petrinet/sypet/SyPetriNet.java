package edu.cmu.petrinet.sypet;

public interface SyPetriNet {

  boolean contains(Type type);

  boolean contains(MethodTransition signature);

  boolean containsArc(Type type, MethodTransition signature);

  boolean containsArc(MethodTransition signature, Type type);

//  int getArcWeight(Type type, MethodTransition signature);

//  int getArcWeight(MethodTransition signature, Type type);
}
