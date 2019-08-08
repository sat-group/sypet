package edu.cmu.sypet.java;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

public interface TypeFinder extends AutoCloseable {

  /**
   * A method that provides the super classes of all application classes.
   *
   * @param acceptableSuperClasses the set of classes that can be considered super classes. In order
   *     to reduce the unnecessary super classes (e.g. Object).
   * @param packages
   * @return the map mapping each class name, to the subset of its super classes that is in {@code
   *     acceptableSuperClasses}.
   */
  ImmutableMultimap<Type, Type> getSuperClasses(
      final ImmutableSet<Type> acceptableSuperClasses,
      final ImmutableSet<Package> packages);

  ImmutableSet<MethodSignature> getSignatures(
      final ImmutableSet<Method> methodBlacklist);
}
