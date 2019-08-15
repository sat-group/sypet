package edu.cmu.reachability.petrinet;

public interface Flow {

  Place getPlace();

  Integer getWeight();

  void setWeight(int i);
}
