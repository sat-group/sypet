package edu.cmu.sypet.petrinet;

import java.util.Set;

/**
 * The Petri net interface used in this library.
 *
 * @see PetriNetFactory
 */
public interface PetriNet {

  /**
   * Returns all the transitions in the Petri net.
   *
   * @return all the transitions in the Petri net
   */
  Set<Transition> getTransitions();

  /**
   * Creates a new transition in the Petri net.
   *
   * @param transitionId the id of the transition to be created
   * @return the created transition
   */
  Transition createTransition(final String transitionId);

  /**
   * Returns all the places in the Petri net.
   *
   * @return all the places in the Petri net
   */
  Set<Place> getPlaces();

  /**
   * Returns a place in the Petri net.
   *
   * @param id the id of the place to be retrieved
   * @return the place identified by the provided id
   * @throws NoSuchPlaceException if the place does not exist in the Petri net
   */
  Place getPlace(final String id) throws NoSuchPlaceException;

  /**
   * Checks the presence of a place in the Petri net.
   *
   * @param id the id of the place
   * @return whether the place identified by the provided id exists in the Petri net
   */
  boolean containsPlace(String id) throws NoSuchFlowException;

  /**
   * Creates a new place in the Petri net.
   *
   * @param id the id of the place to be created
   */
  void createPlace(final String id);

  /**
   * Creates a new flow in the Petri net.
   *
   * @param from the id of the source node
   * @param to the id of the target node
   * @param weight value associated with the flow
   */
  void createFlow(final String from, final String to, final int weight);

  /**
   * Returns the flow from the source node to the target node. A node is either a place or a
   * transition in the Petri net.
   *
   * @param sourceId the id of the source node
   * @param targetId the id of the target node
   * @return the flow from the source node to the target node
   * @throws NoSuchFlowException if the flow does not exist in the Petri net
   */
  Flow getFlow(final String sourceId, final String targetId);

  /**
   * Represents a transition in the Petri net.
   */
  interface Transition {

    String getId();

    /**
     * Returns the flows coming in to the transition.
     *
     * @return the flows coming in to the transition
     */
    Set<Flow> getPresetEdges();

    /**
     * Returns the flows coming out from the transition.
     *
     * @return the flows coming out from the transition
     */
    Set<Flow> getPostsetEdges();
  }

  /**
   * Represents a flow in the Petri net.
   */
  interface Flow {

    /**
     * Returns the place of the flow.
     *
     * @return the place of the flow
     */
    Place getPlace();

    /**
     * Returns the weight of the flow.
     *
     * @return the weight of the flow
     */
    Integer getWeight();

    /**
     * Sets the weight of the flow.
     *
     * @param weight the value to be set
     */
    void setWeight(int weight);
  }

  /**
   * Represents a place in the Petri net.
   */
  interface Place {

    /**
     * Returns the id of the place.
     *
     * @return the id of the place
     */
    String getId();

    /**
     * Returns the postset of the place, which is the set of transitions with flows going outwards
     * from the place.
     *
     * @return the postset of the place
     */
    Set<Transition> getPostset();

    /**
     * Returns the preset of the place, which is the set of transitions with flows directed to the
     * place.
     *
     * @return the preset of the place
     */
    Set<Transition> getPreset();

    // TODO: I suspect this returns the maximum number of tokens allowed in this place, but I am
    //  not sure.
    int getMaxToken();

    void setMaxToken(int i);
  }

  /**
   * Thrown to indicate that a place does not exist in the Petri net.
   */
  final class NoSuchPlaceException extends RuntimeException {

    public NoSuchPlaceException(Throwable cause) {
      super(cause);
    }
  }

  /**
   * Thrown to indicate that a flow does not exist in the Petri net.
   */
  final class NoSuchFlowException extends RuntimeException {

    public NoSuchFlowException(Throwable cause) {
      super(cause);
    }
  }

}
