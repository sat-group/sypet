package edu.cmu.sypet.parser;

import org.immutables.value.Value;

@Deprecated
@Value.Immutable
public abstract class SootType implements Type {

  @Value.Parameter
  protected abstract soot.Type delegate_type();

  @Override
  public String name() {
    return delegate_type().toString();
  }

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}
