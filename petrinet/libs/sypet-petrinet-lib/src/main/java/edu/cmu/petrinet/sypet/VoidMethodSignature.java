package edu.cmu.petrinet.sypet;

import java.util.Collection;

final class VoidMethodSignature<T, U> implements MethodSignature<T, U> {
  private final MethodSignature<T, U> signature;
  private final Type<T> voidType;

  public VoidMethodSignature(MethodSignature<T, U> signature, Type<T> voidType) {
    this.signature = signature;
    this.voidType = voidType;
  }

  @Override
  public U id() {
    return this.signature.id(); // FIXME
  }

  @Override
  public Collection<Type<T>> parametersTypes() {
    return signature.parametersTypes();
  }

  @Override
  public Type returnType() {
    return this.voidType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VoidMethodSignature<?, ?> that = (VoidMethodSignature<?, ?>) o;

    if (!signature.equals(that.signature)) {
      return false;
    }
    return voidType.equals(that.voidType);
  }

  @Override
  public int hashCode() {
    int result = signature.hashCode();
    result = 31 * result + voidType.hashCode();
    return result;
  }
}