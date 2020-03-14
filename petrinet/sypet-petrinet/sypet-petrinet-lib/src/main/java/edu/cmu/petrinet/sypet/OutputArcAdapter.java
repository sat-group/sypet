package edu.cmu.petrinet.sypet;

public class OutputArcAdapter extends BackendOutputArc {

  OutputArcAdapter(final BackendTransition from, final BackendPlace to, final Integer weight) {
    super(from, to, weight);
  }

  @Override
  public BackendTransition getFrom() {
    return this.getFrom();
  }

  @Override
  public BackendPlace getTo() {
    return this.getTo();
  }

  @Override
  public Integer  getWeight() {
    return this.getWeight();
  }
}