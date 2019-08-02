/*
 * BSD 3-Clause License
 *
 *
 * Copyright (c) 2018, SyPet 2.0 - Ruben Martins, Yu Feng, Isil Dillig All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.cmu.sypet.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// TODO Use Collection instead of List.

/** This class represents the configuration options for SyPet. */
public final class SyPetConfig {

  /** TODO */
  public final List<String> localSuperClasses;

  /** TODO */
  public final List<List<String>> globalSuperClasses;

  /** TODO */
  public final List<String> blacklist; // TODO Rename this?

  /** TODO */
  public final List<String> noSideEffects; // TODO Rename this?

  private SyPetConfig(Builder builder) {
    this.localSuperClasses = builder.localSuperClasses;
    this.globalSuperClasses = builder.globalSuperClasses;
    this.blacklist = builder.blacklist;
    this.noSideEffects = builder.noSideEffects;
  }

  /** TODO */
  @SuppressWarnings("UnusedReturnValue")
  public static class Builder {
    // The Builder pattern. Refer to Effective Java, 3rd edition, Item 2, for an overview of this
    // pattern.
    // The required fields are the parameters of the Builder constructor. The optional fields can be
    // set on a case-by-case basis using setters. A SyPetConfig instance can be obtained by invoking
    // Builder::build.

    // Optional fields -- initialized to default values.
    private List<String> localSuperClasses = new ArrayList<>();
    private List<List<String>> globalSuperClasses = new ArrayList<>();
    private List<String> blacklist = new ArrayList<>();
    private List<String> noSideEffects = new ArrayList<>();

    /** TODO */
    public Builder() {}

    /** TODO */
    public Builder localSuperClasses(List<String> val) {
      this.localSuperClasses = val;
      return this;
    }

    /** TODO */
    public Builder globalSuperClasses(List<List<String>> val) {
      Objects.requireNonNull(val);
      this.globalSuperClasses = val;

      // TODO Explain this
      final List<String> poly1 =
          new ArrayList<>(Arrays.asList("java.lang.CharSequence", "java.lang.String"));
      this.globalSuperClasses.add(poly1);

      return this;
    }

    /** TODO */
    public Builder blacklist(List<String> val) {
      Objects.requireNonNull(val);
      this.blacklist = val;
      return this;
    }

    /** TODO */
    public Builder noSideEffects(List<String> val) {
      Objects.requireNonNull(val);
      this.noSideEffects = val;
      return this;
    }

    /** TODO */
    public SyPetConfig build() {
      return new SyPetConfig(this);
    }
  }
}
