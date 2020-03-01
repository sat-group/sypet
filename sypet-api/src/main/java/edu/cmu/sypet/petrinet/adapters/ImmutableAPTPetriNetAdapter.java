package edu.cmu.sypet.petrinet.adapters;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;
import uniol.apt.adt.exception.NoSuchEdgeException;
import uniol.apt.adt.exception.NoSuchNodeException;

/**
 * Adapter over the Petri net implementation from the APT library in order to make it work with the
 * {@link edu.cmu.sypet.petrinet.PetriNet} interface.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
public abstract class ImmutableAPTPetriNetAdapter implements PetriNet {

  /**
   * The adaptee.
   */
  @Value.Parameter
  abstract uniol.apt.adt.pn.PetriNet petriNet();

  /**
   * Get all the transitions in the Petri net.
   */
  @Override
  public Set<PetriNet.Transition> getTransitions() {
    return petriNet().getTransitions().stream()
        .map(edu.cmu.sypet.petrinet.adapters.Transition::of)
        .collect(Collectors.toSet());
  }

  /**
   * Creates a new transition with id {@code transitionId}, and places it in the cache.
   */
  @Override
  public PetriNet.Transition createTransition(final String transitionId) {
    return edu.cmu.sypet.petrinet.adapters.Transition.of(petriNet().createTransition(transitionId));
  }

  /**
   * Get all the places in the Petri net.
   */
  @Override
  public Set<PetriNet.Place> getPlaces() {
    return petriNet().getPlaces().stream()
        .map(edu.cmu.sypet.petrinet.adapters.Place::of)
        .collect(Collectors.toSet());
  }

  /**
   * Get the place with id {@code placeId} in the Petri net, or throw an exception if it cannot be
   * found.
   */
  @Override
  public PetriNet.Place getPlace(final String placeId) {
    try {
      return edu.cmu.sypet.petrinet.adapters.Place.of(petriNet().getPlace(placeId));
    } catch (NoSuchNodeException e) {
      throw new NoSuchPlaceException(e);
    }
  }

  /**
   * Check whether there's a place with id {@code placeId} in the Petri net.
   */
  @Override
  public boolean containsPlace(final String placeId) {
    return petriNet().containsPlace(placeId);
  }

  /**
   * Creates a new place with id {@code placeId}, and places it in the cache.
   */
  @Override
  public void createPlace(final String placeId) {
    petriNet().createPlace(placeId);
  }

  /**
   * Creates a new flow with weight {@code weight} from the node with id {@code from} to the node
   * with id {@code to}, and places it in the cache.
   */
  @Override
  public void createFlow(final String from, final String to, final int weight) {
    petriNet().createFlow(from, to, weight);
  }

  /**
   * Returns the flow from the node with id {@code id1} to the node with id {@code id2}, or throws
   * an exception if it cannot be found. A node is either a place or a transition in the Petri net.
   */
  @Override
  public PetriNet.Flow getFlow(final String id1, final String id2) {
    try {
      return edu.cmu.sypet.petrinet.adapters.Flow.of(petriNet().getFlow(id1, id2));
    } catch (NoSuchEdgeException e) {
      throw new NoSuchFlowException(e);
    }
  }

}

/**
 * This class is an adapter over the Petri net transition implementation from the APT library.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutableTransition implements PetriNet.Transition {

  /**
   * The adaptee.
   */
  @Value.Parameter
  abstract uniol.apt.adt.pn.Transition transition();

  @Override
  public String getId() {
    return transition().getId();
  }

  @Override
  public String getLabel() {
    return transition().getLabel();
  }

  /**
   * Returns the flows coming in to this transition.
   */
  @Override
  public Set<PetriNet.Flow> getPresetEdges() {
    return transition().getPresetEdges().stream()
        .map(Flow::of)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<PetriNet.Flow> getPostsetEdges() {
    return transition().getPostsetEdges().stream()
        .map(Flow::of)
        .collect(Collectors.toSet());
  }

}

/**
 * This class is an adapter over the Petri net flow implementation from the APT library.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutableFlow implements PetriNet.Flow {

  /**
   * The adaptee.
   */
  @Value.Parameter
  abstract uniol.apt.adt.pn.Flow flow();

  /**
   * Returns the place of this flow.
   */
  @Override
  public PetriNet.Place getPlace() {
    return Place.of(flow().getPlace());
  }

  /**
   * Returns the weight of this flow.
   */
  @Override
  public Integer getWeight() {
    return flow().getWeight();
  }

  /**
   * Sets the weight of this flow.
   */
  @Override
  public void setWeight(final int i) {
    flow().setWeight(i);
  }
}

/**
 * This class is an adapter over the Petri net place implementation from the APT library.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutablePlace implements PetriNet.Place {

  /**
   * The adaptee.
   */
  @Value.Parameter
  abstract uniol.apt.adt.pn.Place place();

  /**
   * Returns the id of this place.
   */
  @Override
  public String getId() {
    return place().getId();
  }

  @Override
  public int getMaxToken() {
    return place().getMaxToken();
  }

  @Override
  public void setMaxToken(final int i) {
    place().setMaxToken(i);
  }

  @Override
  public Set<PetriNet.Transition> getPostset() {
    return place().getPostset().stream()
        .map(Transition::of)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<PetriNet.Transition> getPreset() {
    return place().getPreset().stream()
        .map(Transition::of)
        .collect(Collectors.toSet());
  }
}
