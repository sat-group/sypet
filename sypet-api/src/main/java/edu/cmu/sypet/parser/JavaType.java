package edu.cmu.sypet.parser;

import org.immutables.value.Value;

@Value.Immutable
public abstract class JavaType implements Type {

  @Value.Parameter
  public abstract java.lang.reflect.Type delegate_type();

  @Override
  public String name() {
    return delegate_type().getTypeName();
  }

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}
