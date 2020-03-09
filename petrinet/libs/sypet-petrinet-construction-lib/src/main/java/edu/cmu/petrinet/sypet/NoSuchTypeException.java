package edu.cmu.petrinet.sypet;

class NoSuchTypeException extends Exception {

  NoSuchTypeException(Type type, MethodSignature signature) {
    super("Error while adding signature of \"" + signature.name() + "\": type \"" + type.name()
        + "\" not found");
  }
}
