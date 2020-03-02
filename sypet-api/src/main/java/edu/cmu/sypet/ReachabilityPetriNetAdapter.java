package edu.cmu.sypet;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter over {@link PetriNet} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.PetriNet} interface.
 */
class ReachabilityPetriNetAdapter implements edu.cmu.reachability.petrinet.PetriNet {

  /**
   * @param petriNet  the adaptee
   */
  ReachabilityPetriNetAdapter(PetriNet petriNet) {
    this.petriNet = petriNet;
  }

  private final PetriNet petriNet;

  @Override
  public Set<edu.cmu.reachability.petrinet.Transition> getTransitions() {
    return petriNet.getTransitions().stream()
        .map(Transition::new)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<edu.cmu.reachability.petrinet.Place> getPlaces() {
    return petriNet.getPlaces().stream()
        .map(Place::new)
        .collect(Collectors.toSet());
  }

  @Override
  public edu.cmu.reachability.petrinet.Place getPlace(String id)
      throws edu.cmu.reachability.petrinet.NoSuchPlaceException {
    return new Place(petriNet.getPlace(id));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReachabilityPetriNetAdapter that = (ReachabilityPetriNetAdapter) o;

    return petriNet.equals(that.petriNet);
  }

  @Override
  public int hashCode() {
    return petriNet.hashCode();
  }

  /**
   * Adapter over {@link PetriNet.Transition} in order to make it work with the {@link
   * edu.cmu.reachability.petrinet.Transition} interface.
   */
  class Transition implements edu.cmu.reachability.petrinet.Transition {

    /**
     * @param transition  the adaptee
     */
    Transition(PetriNet.Transition transition) {
      this.transition = transition;
    }

    private final PetriNet.Transition transition;

    @Override
    public String getId() {
      return this.transition.getId();
    }

    @Override
    public Set<edu.cmu.reachability.petrinet.Flow> getPostsetEdges() {
      return this.transition.getPostsetEdges().stream()
          .map(Flow::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<edu.cmu.reachability.petrinet.Flow> getPresetEdges() {
      return this.transition.getPresetEdges().stream()
          .map(Flow::new)
          .collect(Collectors.toSet());
    }

    @Override
    public String getLabel() {
      return this.transition.getLabel();
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
   * Adapter over {@link PetriNet.Flow} in order to make it work with the {@link
   * edu.cmu.reachability.petrinet.Flow} interface.
   */
  class Flow implements edu.cmu.reachability.petrinet.Flow {

    /**
     * @param flow  the adaptee
     */
    Flow(PetriNet.Flow flow) {
      this.flow = flow;
    }

    private final PetriNet.Flow flow;

    @Override
    public edu.cmu.reachability.petrinet.Place getPlace() {
      return new Place(flow.getPlace());
    }

    @Override
    public Integer getWeight() {
      return flow.getWeight();
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
   * Adapter over {@link PetriNet.Place} in order to make it work with the {@link
   * edu.cmu.reachability.petrinet.Place} interface.
   */
  class Place implements edu.cmu.reachability.petrinet.Place {

    /**
     * @param place   the adaptee
     */
    Place(PetriNet.Place place) {
      this.place = place;
    }

    private final PetriNet.Place place;

    @Override
    public String getId() {
      return place.getId();
    }

    @Override
    public int getMaxToken() {
      return place.getMaxToken();
    }

    @Override
    public Set<edu.cmu.reachability.petrinet.Transition> getPostset() {
      return place.getPostset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<edu.cmu.reachability.petrinet.Transition> getPreset() {
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