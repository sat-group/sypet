package edu.cmu.petrinet.sypet;

final class InputArcAdapter extends BackendInputArc {
  InputArcAdapter(final BackendPlace from, final BackendTransition to, final Integer weight) {
    super(from, to, weight);
  }

  @Override
  public BackendPlace getFrom() {
    return this.getFrom();
  }

  @Override
  public BackendTransition getTo() {
    return this.getTo();
  }

  @Override
  public Integer getWeight() {
    return this.getWeight();
  }
}
