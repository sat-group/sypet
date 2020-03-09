package edu.cmu.petrinet.sypet;

class BadCastException extends RuntimeException {

  BadCastException(Type from, Type to) {
    super("From: " + from + "; To: " + to);
  }
}
