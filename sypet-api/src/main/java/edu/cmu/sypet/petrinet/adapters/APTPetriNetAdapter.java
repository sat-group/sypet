package edu.cmu.sypet.petrinet.adapters;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.Set;
import java.util.stream.Collectors;
import uniol.apt.adt.exception.NoSuchEdgeException;
import uniol.apt.adt.exception.NoSuchNodeException;

/**
 * Adapter over the Petri net implementation from the APT library in order to make it work with the
 * {@link edu.cmu.sypet.petrinet.PetriNet} interface.
 */
public class APTPetriNetAdapter implements PetriNet {

  /**
   * @param petriNet  the adaptee
   */
  public APTPetriNetAdapter(uniol.apt.adt.pn.PetriNet petriNet) {
    this.petriNet = petriNet;
  }

  private final uniol.apt.adt.pn.PetriNet petriNet;

  /**
   * Get all the transitions in the Petri net.
   */
  @Override
  public Set<PetriNet.Transition> getTransitions() {
    return petriNet.getTransitions().stream()
        .map(Transition::new)
        .collect(Collectors.toSet());
  }

  /**
   * Creates a new transition with id {@code transitionId}, and places it in the cache.
   */
  @Override
  public PetriNet.Transition createTransition(final String transitionId) {
    return new Transition(petriNet.createTransition(transitionId));
  }

  /**
   * Get all the places in the Petri net.
   */
  @Override
  public Set<PetriNet.Place> getPlaces() {
    return petriNet.getPlaces().stream()
        .map(Place::new)
        .collect(Collectors.toSet());
  }

  /**
   * Get the place with id {@code placeId} in the Petri net, or throw an exception if it cannot be
   * found.
   */
  @Override
  public PetriNet.Place getPlace(final String placeId) {
    try {
      return new Place(petriNet.getPlace(placeId));
    } catch (NoSuchNodeException e) {
      throw new NoSuchPlaceException(e);
    }
  }

  /**
   * Check whether there's a place with id {@code placeId} in the Petri net.
   */
  @Override
  public boolean containsPlace(final String placeId) {
    return petriNet.containsPlace(placeId);
  }

  /**
   * Creates a new place with id {@code placeId}, and places it in the cache.
   */
  @Override
  public void createPlace(final String placeId) {
    petriNet.createPlace(placeId);
  }

  /**
   * Creates a new flow with weight {@code weight} from the node with id {@code from} to the node
   * with id {@code to}, and places it in the cache.
   */
  @Override
  public void createFlow(final String from, final String to, final int weight) {
    petriNet.createFlow(from, to, weight);
  }

  /**
   * Returns the flow from the node with id {@code id1} to the node with id {@code id2}, or throws
   * an exception if it cannot be found. A node is either a place or a transition in the Petri net.
   */
  @Override
  public PetriNet.Flow getFlow(final String id1, final String id2) {
    try {
      return new Flow(petriNet.getFlow(id1, id2));
    } catch (NoSuchEdgeException e) {
      throw new NoSuchFlowException(e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    APTPetriNetAdapter that = (APTPetriNetAdapter) o;

    return petriNet.equals(that.petriNet);
  }

  @Override
  public int hashCode() {
    return petriNet.hashCode();
  }

  /**
   * This class is an adapter over the Petri net transition implementation from the APT library.
   */
  class Transition implements PetriNet.Transition {

    /**
     * @param transition  the adaptee
     */
    Transition(uniol.apt.adt.pn.Transition transition) {
      this.transition = transition;
    }

    private final uniol.apt.adt.pn.Transition transition;

    @Override
    public String getId() {
      return transition.getId();
    }

    @Override
    public String getLabel() {
      return transition.getLabel();
    }

    /**
     * Returns the flows coming in to this transition.
     */
    @Override
    public Set<PetriNet.Flow> getPresetEdges() {
      return transition.getPresetEdges().stream()
          .map(Flow::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<PetriNet.Flow> getPostsetEdges() {
      return transition.getPostsetEdges().stream()
          .map(Flow::new)
          .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Transition that = (Transition) o;

      return transition.equals(that.transition);
    }

    @Override
    public int hashCode() {
      return transition.hashCode();
    }
  }

  /**
   * This class is an adapter over the Petri net flow implementation from the APT library.
   */
  class Flow implements PetriNet.Flow {

    /**
     * @param flow  the adaptee
     */
    Flow(uniol.apt.adt.pn.Flow flow) {
      this.flow = flow;
    }

    private final  uniol.apt.adt.pn.Flow flow;

    /**
     * Returns the place of this flow.
     */
    @Override
    public PetriNet.Place getPlace() {
      return new Place(flow.getPlace());
    }

    /**
     * Returns the weight of this flow.
     */
    @Override
    public Integer getWeight() {
      return flow.getWeight();
    }

    /**
     * Sets the weight of this flow.
     */
    @Override
    public void setWeight(final int i) {
      flow.setWeight(i);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Flow flow1 = (Flow) o;

      return flow.equals(flow1.flow);
    }

    @Override
    public int hashCode() {
      return flow.hashCode();
    }
  }

  /**
   * This class is an adapter over the Petri net place implementation from the APT library.
   */
  class Place implements PetriNet.Place {

    /**
     * @param place the adaptee
     */
    Place(uniol.apt.adt.pn.Place place) {
      this.place = place;
    }

    private final  uniol.apt.adt.pn.Place place;

    /**
     * Returns the id of this place.
     */
    @Override
    public String getId() {
      return place.getId();
    }

    @Override
    public int getMaxToken() {
      return place.getMaxToken();
    }

    @Override
    public void setMaxToken(final int i) {
      place.setMaxToken(i);
    }

    @Override
    public Set<PetriNet.Transition> getPostset() {
      return place.getPostset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<PetriNet.Transition> getPreset() {
      return place.getPreset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Place place1 = (Place) o;

      return place.equals(place1.place);
    }

    @Override
    public int hashCode() {
      return place.hashCode();
    }
  }
}