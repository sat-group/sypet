package edu.cmu.petrinet.sypet;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public interface Library {
  Collection<Type> types();

  Collection<MethodSignature> signatures();

  Set<Entry<Type, Type>> subtypeRelation();
}
