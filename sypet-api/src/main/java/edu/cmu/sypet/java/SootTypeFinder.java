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

package edu.cmu.sypet.java;

import com.google.common.collect.ImmutableMultimap;
import edu.cmu.sypet.utils.SootUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

@Deprecated
public final class SootTypeFinder implements TypeFinder {

  // TODO Explain why we have this. I guess this should be on SyPetConfig.
  final int methodMaxNumberOfParameters = 5;

  private final Collection<String> packages;

  private final Collection<String> libs;

  public SootTypeFinder(Collection<String> libs, Collection<String> packages) {
    this.libs = libs;
    this.packages = packages;

    SootUtils.initSoot(libs);
  }

  private static Set<String> getSuperClasses(
      Set<String> acceptableSuperClasses, SootClass clazz) {
    final Set<String> superClasses = new HashSet<>();

    if (clazz.hasSuperclass()) {
      final SootClass superclass = clazz.getSuperclass();
      final String name = superclass.getName();

      if (acceptableSuperClasses.contains(name)) {
        superClasses.add(name);
      }

      superClasses.addAll(getSuperClasses(acceptableSuperClasses, superclass));
    }

    for (SootClass interface_ : clazz.getInterfaces()) {
      final String name = interface_.getName();

      if (acceptableSuperClasses.contains(name)) {
        superClasses.add(name);
      }

      superClasses.addAll(getSuperClasses(acceptableSuperClasses, interface_));
    }

    return superClasses;
  }

  @Override
  public ImmutableMultimap<String, String> getSuperClasses(Set<String> acceptableSuperClasses,
      Collection<String> packages) {
    // Select classes that belong to one of the packages.
    Iterable<SootClass> filteredClasses = Scene.v().getClasses().stream()
        .filter(type -> packages.stream().anyMatch(pkg -> type.getName().startsWith(pkg)))
        ::iterator;

    final ImmutableMultimap.Builder<String, String> subClassMapBuilder =
        new ImmutableMultimap.Builder<>();

    for (SootClass type : filteredClasses) {
      subClassMapBuilder.putAll(type.getName(), getSuperClasses(acceptableSuperClasses, type));
    }

    return subClassMapBuilder.build();
  }

  public List<MethodSignature> getSignatures(List<String> blacklist) {
    return Scene.v().getClasses().stream()
        // Select classes defined in the packages we want to analyze.
        .filter(clazz -> this.packages.stream().anyMatch(clazz.getName()::startsWith))
        // Obtain from those classes all public methods that are not blacklisted and do not have
        // too many parameters.
        .flatMap(clazz -> clazz.getMethods().stream()
            .filter(SootMethod::isPublic)
            .filter(method -> blacklist.stream().noneMatch(method.getName()::contains))
            .filter(method ->
                method.getParameterCount() <= methodMaxNumberOfParameters))
        .map(ImmutableSootMethodSignature::of)
        .collect(Collectors.toList());
  }

  @Override
  public void close() throws Exception {
    // Do nothing.
  }
}

@Value.Immutable
abstract class SootMethodSignature implements MethodSignature {

  public static SootMethodSignature of(final SootMethod sootMethod) {
    return ImmutableSootMethodSignature.builder()
        .name(name(sootMethod))
        .returnType(returnType(sootMethod))
        .addAllParameterTypes(parameterTypes(sootMethod))
        .isStatic(isStatic(sootMethod))
        .isConstructor(isConstructor(sootMethod))
        .declaringClass(declaringClass(sootMethod))
        .build();
  }

  private static String name(SootMethod sootMethod) {
    if (sootMethod.isConstructor()) {
      return sootMethod.getDeclaringClass().getName();
    }
    return sootMethod.getName();
  }

  private static Type returnType(SootMethod sootMethod) {
    if (sootMethod.isConstructor()) {
      return declaringClass(sootMethod);
    }
    return ImmutableSootType.of(sootMethod.getReturnType());
  }

  private static List<Type> parameterTypes(SootMethod sootMethod) {
    return sootMethod.getParameterTypes().stream()
        .map(ImmutableSootType::of)
        .collect(Collectors.toList());
  }

  private static boolean isStatic(SootMethod sootMethod) {
    return sootMethod.isStatic();
  }

  private static boolean isConstructor(SootMethod sootMethod) {
    return sootMethod.isConstructor();
  }

  private static Type declaringClass(SootMethod sootMethod) {
    return ImmutableSootType.of(sootMethod.getDeclaringClass().getType());
  }

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}

@Value.Immutable
abstract class SootType implements Type {

  @Value.Parameter
  protected abstract soot.Type delegate_type();

  @Override
  public String name() {
    return delegate_type().toString();
  }

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}

