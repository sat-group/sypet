package edu.cmu.sypet.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** This class represents the input for SyPet. */
public final class SyPetInput {
  // TODO Give better names to the fields.

  /** The name of the method we want to synthesize. */
  public final String methodName;

  /** The names of the parameters of the method we want to synthesize. */
  public final List<String> paramNames;

  /** The types of the parameters of the method we want to synthesize. */
  public final List<String> srcTypes;

  /** The return type of the method we want to synthesize. */
  public final String tgtType;

  /** The names of the packages the synthesized program is allowed to import. */
  public final List<String> packages;

  /**
   * Libraries where the packages that the synthesized program is allowed to import can be found.
   */
  public final List<String> libs;

  /** The Java test code that the method we want to synthesize must satisfy. */
  public final String testBody;

  /** Lower bound on the number of lines of code of the method we want to synthesize. */
  public final int lb;

  /** Upper bound on the number of lines of code of the method we want to synthesize. */
  public final int ub;

  /** TODO Explain this. */
  public final List<String> hints;

  private SyPetInput(Builder builder) {
    this.methodName = builder.methodName;
    this.paramNames = builder.paramNames;
    this.srcTypes = builder.paramTypes;
    this.tgtType = builder.returnType;
    this.packages = builder.packages;
    this.libs = builder.libs;
    this.testBody = builder.testCode;
    this.lb = builder.locLowerBound;
    this.ub = builder.locUpperBound;
    this.hints = builder.hints;
  }

  /** TODO */
  @SuppressWarnings("UnusedReturnValue")
  public static class Builder {
    // The Builder pattern. Refer to Effective Java, 3rd edition, Item 2, for an overview of this
    // pattern.
    // The required fields are the parameters of the Builder constructor. The optional fields can be
    // set on a case-by-case basis using setters. A SyPetInput instance can be obtained by invoking
    // Builder::build.

    // NOTE There are many required fields. Consider applying the Step Builder pattern instead.

    private final String methodName;
    private final List<String> paramNames;
    private final List<String> paramTypes;
    private final String returnType;
    private final List<String> packages;
    private final List<String> libs;
    private final String testCode;

    // Optional fields -- initialized to default values.
    private int locLowerBound = 1;
    private int locUpperBound = 10;
    private List<String> hints = new ArrayList<>();

    // TODO Check if testBody is valid

    /** TODO */
    public Builder(
        String methodName,
        List<String> paramNames,
        List<String> paramTypes,
        String returnType,
        List<String> packages,
        List<String> libs,
        String testCode) {
      Objects.requireNonNull(methodName);
      Objects.requireNonNull(paramNames);
      Objects.requireNonNull(paramTypes);
      Objects.requireNonNull(returnType);
      Objects.requireNonNull(packages);
      Objects.requireNonNull(libs);
      Objects.requireNonNull(testCode);

      // Required fields.
      this.methodName = methodName;
      this.paramNames = paramNames;
      this.paramTypes = paramTypes;
      this.returnType = returnType;
      this.packages = packages;
      this.libs = libs;
      this.testCode = testCode;
    }

    /** TODO */
    public Builder locLowerBound(int val) {
      this.locLowerBound = val;
      return this;
    }

    /** TODO */
    public Builder locUpperBound(int val) {
      this.locUpperBound = val;
      return this;
    }

    /** TODO */
    public Builder hints(List<String> val) {
      Objects.requireNonNull(val);
      this.hints = val;
      return this;
    }

    /** TODO */
    public SyPetInput build() {
      return new SyPetInput(this);
    }
  }
}
