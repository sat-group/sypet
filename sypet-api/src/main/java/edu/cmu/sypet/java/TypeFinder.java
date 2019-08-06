package edu.cmu.sypet.java;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TypeFinder extends AutoCloseable {

  // TODO Provide a better typed interface, instead of returning just strings

  /**
   * A method that provides the super classes of all application classes.
   *
   * @param acceptableSuperClasses the set of classes that can be considered super classes. In order
   * to reduce the unnecessary super classes (e.g. Object).
   * @return the map mapping each class name, to the subset of its super classes that is in {@code
   * acceptableSuperClasses}.
   */
  Map<String, Set<String>> getSuperClasses(Set<String> acceptableSuperClasses,
      Collection<String> packages);

  List<MethodSignature> getSignatures(List<String> blacklist);
}
