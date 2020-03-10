package edu.cmu.petrinet.sypet;

public interface CastTransition<T> extends Transition<T> {
  Type<T> subtype();
  Type<T> supertype();
}
