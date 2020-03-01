package edu.cmu.sypet.petrinet;

import java.util.Set;

/**
 * The Petri net interface used in this library.
 *
 * @see PetriNetFactory
 */
public interface PetriNet {

  //region Methods
  Transition createTransition(final String transitionId);

  void createPlace(final String placeId);

  void createFlow(final String from, final String to, final int weight);

  Flow getFlow(final String id1, final String id2);

  boolean containsPlace(String type) throws NoSuchFlowException;

  Place getPlace(final String id) throws NoSuchPlaceException;

  Set<Place> getPlaces();

  Set<Transition> getTransitions();
  //endregion

  //region Inner Classes
  interface Transition {

    String getId();

    String getLabel();

    Set<Flow> getPresetEdges();

    Set<Flow> getPostsetEdges();
  }

  interface Flow {

    Place getPlace();

    Integer getWeight();

    void setWeight(int i);
  }

  interface Place {

    String getId();

    int getMaxToken();

    void setMaxToken(int i);

    Set<Transition> getPostset();

    Set<Transition> getPreset();
  }

  final class NoSuchPlaceException extends RuntimeException {

    public NoSuchPlaceException() {
    }

    public NoSuchPlaceException(String message) {
      super(message);
    }

    public NoSuchPlaceException(String message, Throwable cause) {
      super(message, cause);
    }

    public NoSuchPlaceException(Throwable cause) {
      super(cause);
    }

    public NoSuchPlaceException(
        String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
  }

  final class NoSuchFlowException extends RuntimeException {

    public NoSuchFlowException() {
    }

    public NoSuchFlowException(String message) {
      super(message);
    }

    public NoSuchFlowException(String message, Throwable cause) {
      super(message, cause);
    }

    public NoSuchFlowException(Throwable cause) {
      super(cause);
    }

    public NoSuchFlowException(
        String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
  }
  //endregion

}
