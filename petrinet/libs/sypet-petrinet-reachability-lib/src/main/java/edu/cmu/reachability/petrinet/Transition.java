package edu.cmu.reachability.petrinet;

import java.util.Set;

public interface Transition {
  String getId();

  Set<Flow> getPostsetEdges();

  Set<Flow> getPresetEdges();
}
