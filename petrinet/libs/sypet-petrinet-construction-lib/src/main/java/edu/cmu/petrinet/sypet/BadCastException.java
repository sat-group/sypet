package edu.cmu.petrinet.sypet;

class BadCastException extends Exception {

  BadCastException(Type from, Type to) {
    super("Error while adding cast transition: type " + from.name() + "cannot be cast to type "
        + to.name());
  }
}
