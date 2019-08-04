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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;

/** This class is used to obtain method signatures from jar files. */
public final class JarParser {

  /** TODO */
  private final Collection<String> packages;

  /** Physical addresses of the jar files, e.g., "lib/hamcrest-core-1.3.jar". */
  private final Collection<String> libs;

  /** TODO */
  public JarParser(List<String> libs, Collection<String> packages) {
    this.libs = libs;
    this.packages = packages;
  }

  /** TODO */
  private static MethodSignature getMethodSignature(SootMethod method) {
    final SootClass clazz = method.getDeclaringClass();

    if (method.isConstructor()) {
      return new MethodSignature(
          method.getName(),
          clazz.getType(),
          method.getParameterTypes(),
          method.isStatic(),
          clazz,
          true,
          method);
    } else {
      return new MethodSignature(
          method.getName(),
          method.getReturnType(),
          method.getParameterTypes(),
          method.isStatic(),
          clazz,
          false,
          method);
    }
  }

  // TODO DRY this.
  /** TODO */
  private static Set<String> getSuperClassesOfClass(
      Set<String> acceptableSuperClasses, SootClass clazz) {
    final Set<String> superClasses = new HashSet<>();

    if (clazz.hasSuperclass()) {
      final SootClass superclass = clazz.getSuperclass();
      final String name = superclass.getName();

      if (acceptableSuperClasses.contains(name)) {
        superClasses.add(name);
      }

      superClasses.addAll(getSuperClassesOfClass(acceptableSuperClasses, superclass));
    }

    for (SootClass interface_ : clazz.getInterfaces()) {
      final String name = interface_.getName();

      if (acceptableSuperClasses.contains(name)) {
        superClasses.add(name);
      }

      superClasses.addAll(getSuperClassesOfClass(acceptableSuperClasses, interface_));
    }

    return superClasses;
  }

  /**
   * A method that provides the super classes of all application classes.
   *
   * @param acceptableSuperClasses the set of classes that can be considered super classes. In order
   *     to reduce the unnecessary super classes (e.g. Object).
   * @return the map from each SootClass, to its corresponding set of super classes.
   */
  public Map<String, Set<String>> getSuperClasses(Set<String> acceptableSuperClasses) {
    // TODO Explain
    return Scene.v().getClasses().stream()
        .filter(clazz -> this.packages.stream().anyMatch(pkg -> clazz.getName().startsWith(pkg)))
        .collect(
            Collectors.toMap(
                SootClass::getName,
                clazz -> getSuperClassesOfClass(acceptableSuperClasses, clazz)));
  }

  /**
   * TODO
   *
   * <p>Produce a list of method signatures from a list of jars.
   *
   * @return the list of method signatures contained in the given libraries
   */
  public List<MethodSignature> parseJar(List<String> blacklist) {
    // TODO Explain why we have this. I guess this should be on SyPetConfig.
    final int methodMaxNumberOfParameters = 5;

    // TODO Comment this
    return libs.stream()
        .flatMap(
            jar ->
                SourceLocator.v().getClassesUnder(jar).stream()
                    .map(Scene.v()::getSootClass)
                    .filter(clazz -> this.packages.stream().anyMatch(clazz.getName()::startsWith))
                    .flatMap(
                        clazz ->
                            clazz.getMethods().stream()
                                .filter(SootMethod::isPublic)
                                .filter(
                                    method ->
                                        blacklist.stream().noneMatch(method.getName()::contains))
                                .filter(
                                    method ->
                                        method.getParameterCount() <= methodMaxNumberOfParameters)))
        .map(JarParser::getMethodSignature)
        .collect(Collectors.toList());
  }
}
