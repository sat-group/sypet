package edu.cmu.petrinet.sypet;

public interface CastTransition extends Transition {
  Type subtype();
  Type supertype();
}
