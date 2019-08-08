package edu.cmu.sypet.java;

import org.immutables.value.Value;

@Value.Immutable
public interface Program {

  @Value.Parameter
  String code();
}
