package edu.cmu.petrinet.sypet;

public class ArcAlreadyExistsException extends PNBInternalException {
  public ArcAlreadyExistsException(Type type, MethodSignature signature) {
    super("Arc from \"" + type + "\" to \"" + signature + "\" already exists");
  }

  public ArcAlreadyExistsException(MethodSignature signature, Type type) {
    super("Arc from \"" + signature + "\" to \"" + type + "\" already exists");
  }
}
