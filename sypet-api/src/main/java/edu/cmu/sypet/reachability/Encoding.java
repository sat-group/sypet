package edu.cmu.sypet.reachability;

import com.google.common.collect.ImmutableSet;
import edu.cmu.sypet.java.Method;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;

public interface Encoding {

  // Maps the pair <transition in the petri-net, timestamp> to variable
  HashMap<Pair<Transition, Integer>, Variable> transition2variable = new HashMap<>();

  // Maps the triple <place in the petri-net, timestamp, value> to variale
  HashMap<Triple<Place, Integer, Integer>, Variable> place2variable = new HashMap<>();

  // To be used with flow encoding
  HashMap<Pair<Place, Triple<Integer, Integer, Integer>>, Variable> quad2variable = new HashMap<>();

  SATSolver solver = new SATSolver();

  void setState(final ImmutableSet<Pair<Place, Integer>> state, final int timestep);

  void setHints(final ImmutableSet<Method> atLeastK);

  void createConstraints();

  void createVariables();
}
