package edu.cmu.sypet.java;

import java.util.List;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Deprecated
@Value.Immutable
public abstract class SootMethodSignature implements MethodSignature {

  @Value.Parameter
  protected abstract soot.SootMethod delegate_method();

  @Override
  public String name() {
    if (isConstructor()) {
      return declaringClass().name();
    }
    return delegate_method().getName();
  }

  @Override
  public Type returnType() {
    if (isConstructor()) {
      return declaringClass();
    }
    return ImmutableSootType.of(delegate_method().getReturnType());
  }

  @Override
  public List<Type> parameterTypes() {
    return delegate_method().getParameterTypes().stream()
        .map(ImmutableSootType::of)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isStatic() {
    return delegate_method().isStatic();
  }

  @Override
  public boolean isConstructor() {
    return delegate_method().isConstructor();
  }

  @Override
  public Type declaringClass() {
    return ImmutableSootType.of(delegate_method().getDeclaringClass().getType());
  }

  @Override
  public AccessModifier accessModifier() {
    if (delegate_method().isPublic()) {
      return AccessModifier.PUBLIC;
    } else if (delegate_method().isPrivate()) {
      return AccessModifier.PRIVATE;
    } else { // delegate_method().isProtected()
      return AccessModifier.PROTECTED;
    }
  }

}
