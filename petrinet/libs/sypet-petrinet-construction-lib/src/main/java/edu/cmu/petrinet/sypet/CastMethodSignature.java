package edu.cmu.petrinet.sypet;

import java.util.ArrayList;
import java.util.Collection;

public class CastMethodSignature implements MethodSignature {
  private final Type subtype;
  private final Type supertype;

  public CastMethodSignature(Type subtype, Type supertype) {
    this.subtype = subtype;
    this.supertype = supertype;
  }

  @Override
  public Collection<Type> parametersTypes() {
    Collection<Type> list = new ArrayList<>();
    list.add(subtype);
    list.add(supertype);
    return list;
  }

  @Override
  public String name() {
    return "(" + subtype.name() + " --> " + supertype.name() + ")##Cast";
  }

  @Override
  public Type returnType() {
    return supertype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CastMethodSignature that = (CastMethodSignature) o;

    if (!subtype.equals(that.subtype)) {
      return false;
    }
    return supertype.equals(that.supertype);
  }

  @Override
  public int hashCode() {
    int result = subtype.hashCode();
    result = 31 * result + supertype.hashCode();
    return result;
  }
}
