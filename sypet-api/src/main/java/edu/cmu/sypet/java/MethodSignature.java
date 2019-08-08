package edu.cmu.sypet.java;

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface MethodSignature {

  String name();

  Type returnType();

  List<Type> parameterTypes();

  boolean isStatic();

  boolean isConstructor();

  Type declaringClass();
}
