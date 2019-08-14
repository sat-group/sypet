package edu.cmu.reachability.petrinet;

import java.util.Set;

public interface Transition {
  String getId();

  Flow[] getPostsetEdges();

  Set<Flow> getPresetEdges();

  String getLabel();
}
