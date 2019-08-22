package edu.cmu.reachability;

import edu.cmu.reachability.petrinet.Place;
import edu.cmu.reachability.petrinet.Transition;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public interface Encoding {

  // Maps the pair <transition in the petri-net, timestamp> to variable
  HashMap<Pair<Transition, Integer>, Variable> transition2variable = new HashMap<>();

  // Maps the triple <place in the petri-net, timestamp, value> to variale
  HashMap<Triple<Place, Integer, Integer>, Variable> place2variable = new HashMap<>();

  // To be used with flow encoding
  HashMap<Pair<Place, Triple<Integer, Integer, Integer>>, Variable> quad2variable = new HashMap<>();

  SATSolver solver = new SATSolver();

  void setState(Set<Pair<Place, Integer>> state, int timestep);

  void setHints(List<String> atLeastK);

  void createConstraints();

  void createVariables();
}