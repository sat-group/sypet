package edu.cmu.sypet.java;

import org.immutables.value.Value;

@Value.Immutable
public interface TestProgram {

  @Value.Parameter
  String code();
}
