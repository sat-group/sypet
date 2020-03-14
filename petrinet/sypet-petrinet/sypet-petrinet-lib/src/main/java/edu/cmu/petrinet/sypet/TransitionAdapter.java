package edu.cmu.petrinet.sypet;

final class TransitionAdapter extends BackendTransition {
  private final Transition transition;

  TransitionAdapter(final Transition transition) {
    this.transition = transition;
  }

  @Override
  public String getId() {
    return this.transition.getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TransitionAdapter that = (TransitionAdapter) o;

    return transition.equals(that.transition);
  }

  @Override
  public int hashCode() {
    return transition.hashCode();
  }
}
