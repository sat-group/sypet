package edu.cmu.petrinet.sypet;

public interface CloneTransition<T> extends Transition<T> {
  Type<T> type();
}
