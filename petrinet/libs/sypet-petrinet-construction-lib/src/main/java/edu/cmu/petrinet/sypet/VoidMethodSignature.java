package edu.cmu.petrinet.sypet;

import java.util.Collection;

final class VoidMethodSignature implements MethodSignature {
  private final MethodSignature signature;

  public VoidMethodSignature(MethodSignature signature) {
    this.signature = signature;
  }

  @Override
  public Collection<Type> parametersTypes() {
    return signature.parametersTypes();
  }

  @Override
  public String name() {
    return signature.name();
  }

  @Override
  public Type returnType() {
    return new TypeFactory().createVoidType();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VoidMethodSignature that = (VoidMethodSignature) o;

    return signature.equals(that.signature);
  }

  @Override
  public int hashCode() {
    return signature.hashCode();
  }
}