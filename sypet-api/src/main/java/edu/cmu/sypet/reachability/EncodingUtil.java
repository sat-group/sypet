package edu.cmu.sypet.reachability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;

public class EncodingUtil {

  /*
   * Given petrinet and input, create a set of <Place, Integer> pair that
   * represents the initial state
   */
  public static Set<Pair<Place, Integer>> setInitialState(PetriNet pnet, List<String> inputs) {
    // Initial state
    HashSet<Pair<Place, Integer>> initial = new HashSet<>();
    HashMap<Place, Integer> count = new HashMap<>();
    // Count the number of inputs
    for (String input : inputs) {
      Place p;
      p = pnet.getPlace(input);
      if (count.containsKey(p)) {
        count.put(p, count.get(p) + 1);
      } else {
        count.put(p, 1);
      }
    }
    // Add inputs into initial state
    for (Place key : count.keySet()) {
      initial.add(new ImmutablePair<>(key, count.get(key)));
    }

    // Add non-input places into initial states
    Set<Place> ps = pnet.getPlaces();
    for (Place p : ps) {
      boolean isInput = false;
      for (String input : inputs) {
        if (p.getId().equals(input)) {
          isInput = true;
        }
      }
      if (p.getId().equals("void")) {
        initial.add(new ImmutablePair<>(p, 1));
      } else if (!isInput) {
        initial.add(new ImmutablePair<>(p, 0));
      }
    }
    return initial;
  }

  /*
   * Given petrinet and output , create a set of <Place, Integer> pair that
   * represents the goal state
   */
  public static Set<Pair<Place, Integer>> setGoalState(PetriNet pnet, String retType) {

    // Final state
    HashSet<Pair<Place, Integer>> initial = new HashSet<>();
    Set<Place> pl = pnet.getPlaces();
    for (Place p : pl) {
      if (p.getId().equals("void")) {
      } else if (p.getId().equals(retType)) {
        initial.add(new ImmutablePair<>(p, 1));
      } else {
        initial.add(new ImmutablePair<>(p, 0));
      }
    }
    return initial;
  }
}
