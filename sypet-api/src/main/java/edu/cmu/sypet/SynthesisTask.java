package edu.cmu.sypet;

import java.util.Collections;
import java.util.List;
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
  public abstract List<String> paramNames();

  /** The types of the parameters of the method we want to synthesize. */
  public abstract List<String> paramTypes();

  /** The return type of the method we want to synthesize. */
  public abstract String returnType();

  /** The names of the packages the synthesized program is allowed to import. */
  public abstract List<String> packages();

  /**
   * Libraries where the packages that the synthesized program is allowed to import can be found.
   */
  public abstract List<String> libs();

  /** The Java test code that the method we want to synthesize must satisfy. */
  public abstract String testCode();

  /** Lower bound on the number of lines of code of the method we want to synthesize. */
  @Value.Default
  public int locLowerBound() {
    return 1;
  }

  /** Upper bound on the number of lines of code of the method we want to synthesize. */
  @Value.Default
  public int locUpperBound() {
    return 10;
  }

  /** TODO Explain this. */
  @Value.Default
  public List<String> hints() {
    return Collections.emptyList();
  }

  /** TODO */
  @Value.Default
  public List<String> localSuperClasses() {
    return Collections.emptyList();
  }

  /** TODO */
  @Deprecated
  @Value.Default
  public List<List<String>> globalSuperClasses() {
    return Collections.emptyList();
  }

  /** TODO */
  @Value.Default
  public List<String> blacklist() {
    return Collections.emptyList();
  }

  /** TODO */
  @Value.Default
  public List<String> noSideEffects() {
    return Collections.emptyList();
  }
}
