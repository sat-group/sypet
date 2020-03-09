package edu.cmu.petrinet.sypet;

final class TypeFactory {
  Type createVoidType() {
    return new VoidType();
  }
}

final class VoidType implements Type {
  private final String name = "void";
  private final boolean isCastableTo = false;

  VoidType() {
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public boolean isCastableTo(Type type) {
    return isCastableTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VoidType voidType = (VoidType) o;

    if (isCastableTo != voidType.isCastableTo) {
      return false;
    }
    return name.equals(voidType.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (isCastableTo ? 1 : 0);
    return result;
  }
}
