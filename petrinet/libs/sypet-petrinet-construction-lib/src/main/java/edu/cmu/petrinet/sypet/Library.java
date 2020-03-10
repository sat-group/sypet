package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;
import java.util.Set;

public interface Library<T, U> {
  Set<Type<T>> types();

  Type<T> voidType();

  Set<MethodSignature<T, U>> signatures();

  Set<Entry<Type<T>, Type<T>>> castRelation();
}
