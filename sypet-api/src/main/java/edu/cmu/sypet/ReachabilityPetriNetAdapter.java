package edu.cmu.sypet;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter over {@link PetriNet} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.PetriNet} interface.
 */
final class ReachabilityPetriNetAdapter implements edu.cmu.reachability.petrinet.PetriNet {

  //region Fields
  /** The adaptee. */
  private final PetriNet petriNet;
  //endregion

  //region Constructors
  public ReachabilityPetriNetAdapter(edu.cmu.sypet.petrinet.PetriNet petriNet) {
    this.petriNet = petriNet;
  }
  //endregion

  //region Methods
  @Override
  public Set<edu.cmu.reachability.petrinet.Transition> getTransitions() {
    return this.petriNet.getTransitions().stream()
        .map(Transition::new)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<edu.cmu.reachability.petrinet.Place> getPlaces() {
    return this.petriNet.getPlaces().stream()
        .map(Place::new)
        .collect(Collectors.toSet());
  }

  @Override
  public edu.cmu.reachability.petrinet.Place getPlace(String id)
      throws edu.cmu.reachability.petrinet.NoSuchPlaceException {
    return new Place(this.petriNet.getPlace(id));
  }
  //endregion

  //region Inner Classes
  // These are adapters over Petri net transitions, flows and places.

  /**
   * Adapter over {@link PetriNet.Transition} in order to make it work with the
   * {@link edu.cmu.reachability.petrinet.Transition} interface.
   */
  final class Transition implements edu.cmu.reachability.petrinet.Transition {
    private final PetriNet.Transition transition;

    public Transition(PetriNet.Transition transition) {
      this.transition = transition;
    }

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
  }

  /**
   * Adapter over {@link PetriNet.Flow} in order to make it work with the
   * {@link edu.cmu.reachability.petrinet.Flow} interface.
   */
  final class Flow implements edu.cmu.reachability.petrinet.Flow {
    private final PetriNet.Flow flow;

    public Flow(PetriNet.Flow flow) {
      this.flow = flow;
    }

    @Override
    public edu.cmu.reachability.petrinet.Place getPlace() {
      return new Place(flow.getPlace());
    }

    @Override
    public Integer getWeight() {
      return flow.getWeight();
    }
  }

  /**
   * Adapter over {@link PetriNet.Place} in order to make it work with the
   * {@link edu.cmu.reachability.petrinet.Place} interface.
   */
  final class Place implements edu.cmu.reachability.petrinet.Place {
    private final PetriNet.Place place;

    public Place(PetriNet.Place place) {
      this.place = place;
    }

    @Override
    public String getId() {
      return place.getId();
    }

    @Override
    public int getMaxToken() {
      return place.getMaxToken();
    }

    @Override
    public Collection<? extends edu.cmu.reachability.petrinet.Transition> getPostset() {
      return place.getPostset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends edu.cmu.reachability.petrinet.Transition> getPreset() {
      return place.getPreset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }
  }
  //endregion
}
