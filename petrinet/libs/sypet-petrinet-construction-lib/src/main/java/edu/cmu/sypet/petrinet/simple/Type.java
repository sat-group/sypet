package edu.cmu.sypet.petrinet.simple;

public interface Type {

  String name();

  boolean isCastableTo(Type type);
}
