package edu.cmu.petrinet.sypet;

abstract class NodeAdapter<T extends Identifiable> implements BackendNode {
  private final T identifiable;

  NodeAdapter(T identifiable) {
    this.identifiable = identifiable;
  }

  @Override
  public String id() {
    return identifiable.id();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodeAdapter<?> that = (NodeAdapter<?>) o;

    return identifiable.equals(that.identifiable);
  }

  @Override
  public int hashCode() {
    return identifiable.hashCode();
  }
}
