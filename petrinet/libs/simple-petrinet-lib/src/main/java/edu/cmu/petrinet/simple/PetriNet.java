package edu.cmu.petrinet.simple;

import java.util.Map;
import java.util.Set;

final class PetriNet<Place, Transition> implements SimplePetriNet<Place, Transition> {

  private final Set<Place> places;
  private final Set<Transition> transitions;
  private final Map<Place, Transition> placeToTransitionFlows;
  private final Map<Transition, Place> transitionToPlaceFlows;

  PetriNet(
      Set<Place> places,
      Set<Transition> transitions,
      Map<Place, Transition> placeToTransitionFlows,
      Map<Transition, Place> transitionToPlaceFlows) {
    this.places = places;
    this.transitions = transitions;
    this.placeToTransitionFlows = placeToTransitionFlows;
    this.transitionToPlaceFlows = transitionToPlaceFlows;
  }

  @Override
  public void addPlace(Place place) {
    places.add(place);
  }

  @Override
  public void addTransition(Transition transition) {
    transitions.add(transition);
  }

  @Override
  public boolean containsPlace(Place place) {
    return places.contains(place);
  }

  @Override
  public boolean containsTransition(Transition transition) {
    return transitions.contains(transition);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PetriNet<?, ?> that = (PetriNet<?, ?>) o;

    if (!places.equals(that.places)) {
      return false;
    }
    if (!transitions.equals(that.transitions)) {
      return false;
    }
    if (!placeToTransitionFlows.equals(that.placeToTransitionFlows)) {
      return false;
    }
    return transitionToPlaceFlows.equals(that.transitionToPlaceFlows);
  }

  @Override
  public int hashCode() {
    int result = places.hashCode();
    result = 31 * result + transitions.hashCode();
    result = 31 * result + placeToTransitionFlows.hashCode();
    result = 31 * result + transitionToPlaceFlows.hashCode();
    return result;
  }
}
