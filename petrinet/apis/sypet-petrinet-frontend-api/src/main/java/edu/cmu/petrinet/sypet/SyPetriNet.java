package edu.cmu.petrinet.sypet;

public interface SyPetriNet<T, U> {

  boolean contains(Type<T> type);

  boolean contains(MethodSignature<T, U> signature);

  boolean containsArc(Type<T> type, MethodSignature<T, U> signature);

  boolean containsArc(MethodSignature<T, U> signature, Type<T> type);

  int getArcWeight(Type<T> type, MethodSignature<T, U> signature);

  int getArcWeight(MethodSignature<T, U> signature, Type<T> type);
}
