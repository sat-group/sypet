package edu.cmu.petrinet.sypet;

public interface SyPetriNet {

  boolean contains(Type type);

  boolean contains(MethodSignature signature);

  boolean containsArc(Type type, MethodSignature signature);

  boolean containsArc(MethodSignature signature, Type type);

  int getArcWeight(Type type, MethodSignature signature);

  int getArcWeight(MethodSignature signature, Type type);
}
