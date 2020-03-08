package edu.cmu.petrinet.sypet;

public interface Type {

  String name();

  boolean isCastableTo(Type type);
}
