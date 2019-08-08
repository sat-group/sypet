package edu.cmu.sypet.java;

import org.immutables.value.Value;

@Value.Immutable
public interface Package {

  @Value.Parameter
  String name();
}
