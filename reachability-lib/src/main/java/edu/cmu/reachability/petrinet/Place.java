package edu.cmu.reachability.petrinet;

import java.util.Collection;

public interface Place {
  String getId();

  int getMaxToken();

  Collection<? extends Transition> getPostset();

  Collection<? extends Transition> getPreset();
}
