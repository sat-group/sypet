/*
 BSD 3-Clause License


	Copyright (c) 2018, SyPet 2.0 - Ruben Martins, Yu Feng, Isil Dillig
	All rights reserved.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:

	* Redistributions of source code must retain the above copyright notice, this
	  list of conditions and the following disclaimer.

	* Redistributions in binary form must reproduce the above copyright notice,
	  this list of conditions and the following disclaimer in the documentation
	  and/or other materials provided with the distribution.

	* Neither the name of the copyright holder nor the names of its
	  contributors may be used to endorse or promote products derived from
	  this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
	FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
	DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
	CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
	OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package edu.cmu.sypet.parser;

import java.util.List;

/**
 * Data structure that describes a method signature, including 1. Method name 2. Return type 3. The
 * list of argument types in order. 4. Whether the method is static. 5. The host class of the
 * method.
 *
 * @author Kaige Liu
 */
public final class MethodSignature {
  private final String name;
  private final Type retType;
  private final List<Type> argTypes;
  private final boolean isStatic;
  private final Type hostClass;
  private final boolean isConstructor;

  MethodSignature(
      String name,
      Type retType,
      List<Type> argTypes,
      boolean isStatic,
      Type hostClass,
      boolean isConstructor) {

    this.retType = retType;
    this.argTypes = argTypes;
    this.isStatic = isStatic;
    this.hostClass = hostClass;
    this.isConstructor = isConstructor;

    if (isConstructor) {
      this.name = hostClass.name();
    } else {
      this.name = name;
    }
  }

  public String getName() {
    return name;
  }

  public Type getRetType() {
    return retType;
  }

  public List<Type> getArgTypes() {
    return argTypes;
  }

  public boolean getIsStatic() {
    return isStatic;
  }

  public boolean getIsConstructor() {
    return isConstructor;
  }

  public Type getDeclaringClass() {
    return hostClass;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(retType + " " + hostClass + "." + name + "(");
    if (isStatic) result.insert(0, "static ");
    int i = 0;
    for (Type t : argTypes) {
      if (i != argTypes.size() - 1) result.append(t).append(", ");
      else result.append(t);
      i += 1;
    }
    result.append(")");
    return result.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MethodSignature that = (MethodSignature) o;

    if (isStatic != that.isStatic) {
      return false;
    }
    if (isConstructor != that.isConstructor) {
      return false;
    }
    if (!name.equals(that.name)) {
      return false;
    }
    if (!retType.equals(that.retType)) {
      return false;
    }
    if (!argTypes.equals(that.argTypes)) {
      return false;
    }
    return hostClass.equals(that.hostClass);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + retType.hashCode();
    result = 31 * result + argTypes.hashCode();
    result = 31 * result + (isStatic ? 1 : 0);
    result = 31 * result + hostClass.hashCode();
    result = 31 * result + (isConstructor ? 1 : 0);
    return result;
  }
}
