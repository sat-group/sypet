package edu.cmu.sypet.java;

import org.immutables.value.Value;

@Value.Immutable
public interface Type {

  @Value.Parameter
  String name();
}
