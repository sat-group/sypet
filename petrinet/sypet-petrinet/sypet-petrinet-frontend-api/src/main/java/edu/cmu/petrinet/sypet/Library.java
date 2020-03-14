package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;
import java.util.Set;

public interface Library {
  Set<Type> types();

  VoidType voidType();

  Set<MethodTransition> signatures();

  Set<Entry<Type, Type>> castRelation();
}
