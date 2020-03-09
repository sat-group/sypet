package edu.cmu.petrinet.sypet;

final class TypeFactory {
  Type createVoidType() {
    return new SimpleType("void", false);
  }
}

final class SimpleType implements Type {
  private final String name;
  private final boolean isCastable;

  public SimpleType(String name, boolean isCastable) {
    this.name = name;
    this.isCastable = isCastable;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SimpleType that = (SimpleType) o;

    if (isCastable != that.isCastable) {
      return false;
    }
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (isCastable ? 1 : 0);
    return result;
  }

  @Override
  public boolean isCastableTo(Type type) {
    return this.isCastable;
  }
}
