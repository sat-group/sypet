package edu.cmu.reachability.petrinet;

import java.util.Set;

public interface Place {
  String getId();

  int getMaxToken();

  Set<Transition> getPostset();

  Set<Transition> getPreset();
}
