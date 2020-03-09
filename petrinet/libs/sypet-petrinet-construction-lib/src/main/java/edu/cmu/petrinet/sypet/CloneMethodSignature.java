package edu.cmu.petrinet.sypet;

import java.util.ArrayList;
import java.util.Collection;

public class CloneMethodSignature implements MethodSignature {
  private final Type type;

  public CloneMethodSignature(Type type) {
    this.type = type;
  }

  @Override
  public Collection<Type> parametersTypes() {
    final Collection<Type> list = new ArrayList<>();
    list.add(this.type);
    return list;
  }

  @Override
  public String name() {
    return type.name() + "##Clone";
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
    return name().hashCode();
  }
}
