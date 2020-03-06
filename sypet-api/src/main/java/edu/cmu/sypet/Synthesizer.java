package edu.cmu.sypet;

public interface Synthesizer {

  void addConstraint(Constraint constraint);

  Program synthesize();
}
