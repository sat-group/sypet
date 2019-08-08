package edu.cmu.sypet.java;

import org.immutables.value.Value;

@Value.Immutable
public interface Jar {

  @Value.Parameter
  String name();
}
