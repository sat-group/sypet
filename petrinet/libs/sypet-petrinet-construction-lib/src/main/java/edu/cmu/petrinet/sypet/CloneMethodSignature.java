package edu.cmu.petrinet.sypet;

import java.util.ArrayList;
import java.util.Collection;

class CloneMethodSignature<T> implements MethodSignature<T, T> {
  private final Type<T> type;

  CloneMethodSignature(Type<T> type) {
    this.type = type;
  }

  @Override
  public Collection<Type<T>> parametersTypes() {
    final Collection<Type<T>> list = new ArrayList<>();
    list.add(this.type);
    return list;
  }

  @Override
  public T id() {
    return this.type.id();
  }

  @Override
  public Type returnType() {
    return this.type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CloneMethodSignature that = (CloneMethodSignature) o;

    return type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return this.type.hashCode();
  }
}
