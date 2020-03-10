package edu.cmu.petrinet.sypet;

public class NoSuchArcException extends PNBInternalException {

  private static <T, U> String message(final T source, final U target) {
    return "No arc from \"" + source + "\" to target \"" + target + "\" exists.";
  }

  public NoSuchArcException(final Type type, final MethodSignature signature) {
    super(message(type, signature));
  }

  public NoSuchArcException(final MethodSignature signature, final Type type) {
    super(message(signature, type));
  }
}
