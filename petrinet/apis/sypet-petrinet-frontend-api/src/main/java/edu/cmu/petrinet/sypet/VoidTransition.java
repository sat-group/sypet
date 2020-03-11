package edu.cmu.petrinet.sypet;

import java.util.Collection;

interface VoidTransition extends Transition {
  Collection<Type> parametersTypes();
}