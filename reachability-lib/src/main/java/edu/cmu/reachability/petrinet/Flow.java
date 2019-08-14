package edu.cmu.reachability.petrinet;

public interface Flow {

  Place getPlace();

  Integer getWeight();

  Place getSource();

  void setWeight(int i);
}
