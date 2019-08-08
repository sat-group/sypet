package edu.cmu.sypet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.immutables.value.Value;

/** This class represents all the necessary and optional parameters that define a synthesis task. */
@Value.Immutable
public abstract class SynthesisTask {

  /** The name of the method we want to synthesize. */
  @Value.Default
  public String methodName() {
    return "method";
  }

  /** The names of the parameters of the method we want to synthesize. */
  public abstract ImmutableList<String> paramNames();

  /** The types of the parameters of the method we want to synthesize. */
  public abstract ImmutableList<String> paramTypes();

  /** The return type of the method we want to synthesize. */
  public abstract String returnType();

  /** The names of the packages the synthesized program is allowed to import. */
  public abstract ImmutableSet<String> packages();

  /**
   * Libraries where the packages that the synthesized program is allowed to import can be found.
   * @return
   */
  public abstract ImmutableSet<String> jars();

  /** The Java test code that the method we want to synthesize must satisfy. */
  public abstract String testProgram();

  // TODO False, this refers to the length of the path on the petri net.
  /** Lower bound on the number of lines of code of the method we want to synthesize. */
  @Value.Default
  public int locLowerBound() {
    return 1;
  }

  // TODO False, this refers to the length of the path on the petri net.
  /** Upper bound on the number of lines of code of the method we want to synthesize. */
  @Value.Default
  public int locUpperBound() {
    return 10;
  }

  /** TODO Explain this.
   * @return*/
  @Value.Default
  public ImmutableSet<String> hints() {
     return ImmutableSet.of();
  }

  /** TODO */
  @Value.Default
  public ImmutableSet<String> localSuperClasses() {
     return ImmutableSet.of();
  }

  /** TODO */
  @Value.Default
  public ImmutableSet<String> methodBlacklist() {
     return ImmutableSet.of();
  }

  /** TODO */
  @Value.Default
  public ImmutableSet<String> noSideEffects() {
     return ImmutableSet.of();
  }
}
