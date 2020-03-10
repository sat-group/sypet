package edu.cmu.petrinet.sypet;

import java.util.Collection;

interface VoidTransition<T, U> extends Transition<U> {
  Collection<Type<T>> parametersTypes();
}