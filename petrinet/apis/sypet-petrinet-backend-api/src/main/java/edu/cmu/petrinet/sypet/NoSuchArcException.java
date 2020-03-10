package edu.cmu.petrinet.sypet;

public class NoSuchArcException extends PNBInternalException {

  private static <T, U> String message(final T source, final U target) {
    return "No arc from \"" + source + "\" to target \"" + target + "\" exists.";
  }

  public NoSuchArcException(final BackendPlace place, final BackendTransition transition) {
    super(message(place, transition));
  }

  public NoSuchArcException(final BackendTransition transition, final BackendPlace place) {
    super(message(transition, place));
  }
}
