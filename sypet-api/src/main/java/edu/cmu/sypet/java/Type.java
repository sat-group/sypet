package edu.cmu.sypet.java;

public interface Type {

  boolean isCastableTo(Type type);

  String name();
}
