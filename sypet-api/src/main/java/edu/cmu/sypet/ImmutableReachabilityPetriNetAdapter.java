package edu.cmu.sypet;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;

/**
 * Adapter over {@link PetriNet} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.PetriNet} interface.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutableReachabilityPetriNetAdapter implements edu.cmu.reachability.petrinet.PetriNet {
  /** The adaptee. */
  @Value.Parameter
  public abstract PetriNet petriNet();

  @Override
  public Set<edu.cmu.reachability.petrinet.Transition> getTransitions() {
    return petriNet().getTransitions().stream()
        .map(Transition::of)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<edu.cmu.reachability.petrinet.Place> getPlaces() {
    return petriNet().getPlaces().stream()
        .map(Place::of)
        .collect(Collectors.toSet());
  }

  @Override
  public edu.cmu.reachability.petrinet.Place getPlace(String id)
      throws edu.cmu.reachability.petrinet.NoSuchPlaceException {
    return Place.of(petriNet().getPlace(id));
  }
}

/**
 * Adapter over {@link PetriNet.Transition} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.Transition} interface.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutableTransition implements edu.cmu.reachability.petrinet.Transition {
  /** The adaptee. */
  @Value.Parameter
  public abstract PetriNet.Transition transition();

  @Override
  public String getId() {
    return this.transition().getId();
  }

  @Override
  public Set<edu.cmu.reachability.petrinet.Flow> getPostsetEdges() {
    return this.transition().getPostsetEdges().stream()
        .map(Flow::of)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<edu.cmu.reachability.petrinet.Flow> getPresetEdges() {
    return this.transition().getPresetEdges().stream()
        .map(Flow::of)
        .collect(Collectors.toSet());
  }

  @Override
  public String getLabel() {
    return this.transition().getLabel();
  }
}

/**
 * Adapter over {@link PetriNet.Flow} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.Flow} interface.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutableFlow implements edu.cmu.reachability.petrinet.Flow {
  /** The adaptee. */
  @Value.Parameter
  public abstract PetriNet.Flow flow();

  @Override
  public edu.cmu.reachability.petrinet.Place getPlace() {
    return Place.of(flow().getPlace());
  }

  @Override
  public Integer getWeight() {
    return flow().getWeight();
  }
}

/**
 * Adapter over {@link PetriNet.Place} in order to make it work with the
 * {@link edu.cmu.reachability.petrinet.Place} interface.
 */
@Value.Immutable
@Value.Style(
    typeAbstract = "Immutable*",
    typeImmutable = "*"
)
abstract class ImmutablePlace implements edu.cmu.reachability.petrinet.Place {
  /** The adaptee. */
  @Value.Parameter
  public abstract PetriNet.Place place();

  @Override
  public String getId() {
    return place().getId();
  }

  @Override
  public int getMaxToken() {
    return place().getMaxToken();
  }

  @Override
  public Collection<? extends edu.cmu.reachability.petrinet.Transition> getPostset() {
    return place().getPostset().stream()
        .map(Transition::of)
        .collect(Collectors.toSet());
  }

  @Override
  public Collection<? extends edu.cmu.reachability.petrinet.Transition> getPreset() {
    return place().getPreset().stream()
        .map(Transition::of)
        .collect(Collectors.toSet());
  }
}

