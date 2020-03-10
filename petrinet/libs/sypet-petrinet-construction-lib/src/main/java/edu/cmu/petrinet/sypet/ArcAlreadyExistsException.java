package edu.cmu.petrinet.sypet;

public class ArcAlreadyExistsException extends PNBInternalException {
  public ArcAlreadyExistsException(final BackendPlace place, final BackendTransition transition) {
    super("Arc from \"" + place + "\" to \"" + transition + "\" already exists");
  }

  public ArcAlreadyExistsException(final BackendTransition transition, final BackendPlace place) {
    super("Arc from \"" + transition + "\" to \"" + place + "\" already exists");
  }
}
