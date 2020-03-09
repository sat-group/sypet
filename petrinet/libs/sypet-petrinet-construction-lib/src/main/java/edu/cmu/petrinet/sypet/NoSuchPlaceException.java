package edu.cmu.petrinet.sypet;

class NoSuchPlaceException extends Exception {

  NoSuchPlaceException(Type type, MethodSignature signature) {
    super("Error while adding signature of " + signature.name() + ": type " + type.name()
        + " not found");
  }
}
