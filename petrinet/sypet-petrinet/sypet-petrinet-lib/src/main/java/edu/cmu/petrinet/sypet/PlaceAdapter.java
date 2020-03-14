package edu.cmu.petrinet.sypet;

final class PlaceAdapter extends BackendPlace {
  private final Type type;

  PlaceAdapter(final Type type) {
    this.type = type;
  }

  @Override
  public String getId() {
    return this.type.getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlaceAdapter that = (PlaceAdapter) o;

    return type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return type.hashCode();
  }
}
