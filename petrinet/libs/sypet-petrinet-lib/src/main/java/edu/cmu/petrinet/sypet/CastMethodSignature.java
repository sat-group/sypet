package edu.cmu.petrinet.sypet;

import java.util.ArrayList;
import java.util.Collection;

class CastMethodSignature<T> implements MethodSignature<T, T> {
  private final Type<T> subtype;
  private final Type<T> supertype;

  CastMethodSignature(Type subtype, Type supertype) {
    this.subtype = subtype;
    this.supertype = supertype;
  }

  @Override
  public T id() {
    return subtype.id(); // FIXME
  }

  @Override
  public Collection<Type<T>> parametersTypes() {
    Collection<Type<T>> list = new ArrayList<>();
    list.add(subtype);
    list.add(supertype);
    return list;
  }

  @Override
  public Type<T> returnType() {
    return supertype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final CastMethodSignature that = (CastMethodSignature) o;

    if (!subtype.equals(that.subtype)) {
      return false;
    }
    return supertype.equals(that.supertype);
  }

  @Override
  public int hashCode() {
    final int result = subtype.hashCode();
    return 31 * result + supertype.hashCode();
  }
}
