/*
 * BSD 3-Clause License
 *
 * <p>Copyright (c) 2018, SyPet 2.0 - Ruben Martins, Yu Feng, Isil Dillig All rights reserved.
 *
 * <p>Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * <p>* Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * <p>* Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * <p>* Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cmu.edu.parser;

// TODO Use Collection instead of List.

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the input for SyPet.
 */
public final class SyPetInput {
  // TODO Give better names to the fields.

  /**
   * TODO Explain why is this needed.
   */
  public final int id;

  /**
   * The name of the method we want to synthesize.
   */
  public final String methodName;

  /**
   * The names of the parameters of the method we want to synthesize.
   */
  public final List<String> paramNames;

  /**
   * The types of the parameters of the method we want to synthesize.
   */
  public final List<String> srcTypes;

  /**
   * The return type of the method we want to synthesize.
   */
  public final String tgtType;

  /**
   * The names of the packages the synthesized program is allowed to import.
   */
  public final List<String> packages;

  /**
   * Libraries where the packages that the synthesized program is allowed to import can be found.
   */
  public final List<String> libs;

  /**
   * The Java test code that the method we want to synthesize must satisfy.
   */
  public final String testBody;

  /**
   * Lower bound on the number of lines of code of the method we want to synthesize.
   */
  public final int lb;

  /**
   * Upper bound on the number of lines of code of the method we want to synthesize.
   */
  public final int ub;

  /**
   * TODO Explain this.
   */
  public final List<String> hints;

  private SyPetInput(Builder builder) {
    this.id = builder.id;
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

  /**
   * TODO
   */
  public static class Builder {
    // The Builder pattern. Refer to Effective Java, 3rd edition, Item 2, for an overview of this
    // pattern.
    // The required fields are the parameters of the Builder constructor. The optional fields can be
    // set on a case-by-case basis using setters. A SyPetInput instance can be obtained by invoking
    // Builder::build.

    // NOTE There are many required fields. Consider applying the Step Builder pattern instead.

    // Required fields.
    private final int id;
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

    /**
     * TODO
     */
    public Builder(
        int id,
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

      this.id = id;
      this.methodName = methodName;
      this.paramNames = paramNames;
      this.paramTypes = paramTypes;
      this.returnType = returnType;
      this.packages = packages;
      this.libs = libs;
      this.testCode = testCode;
    }

    /**
     * TODO
     */
    public Builder locLowerBound(int val) {
      this.locLowerBound = val;
      return this;
    }

    /**
     * TODO
     */
    public Builder locUpperBound(int val) {
      this.locUpperBound = val;
      return this;
    }

    /**
     * TODO
     */
    public Builder hints(List<String> val) {
      Objects.requireNonNull(val);
      this.hints = val;
      return this;
    }

    /**
     * TODO
     */
    public SyPetInput build() {
      return new SyPetInput(this);
    }
  }
}
