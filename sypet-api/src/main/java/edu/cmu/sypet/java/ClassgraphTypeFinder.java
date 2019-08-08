package edu.cmu.sypet.java;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodParameterInfo;
import io.github.classgraph.ScanResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.immutables.value.Value;

public class ClassgraphTypeFinder implements TypeFinder {

  private final ScanResult scanResult;
  private final Collection<Package> packages;

  public ClassgraphTypeFinder(
      final Collection<Jar> jars,
      final Collection<Package> packages
  ) throws MalformedURLException {

    final ImmutableCollection.Builder<URL> urlsBuilder = ImmutableList.builder();
    for (Jar jar : jars) {
      urlsBuilder.add(new URL("https", "localhost", jar.name()));
    }

    final URL[] urls = urlsBuilder.build().toArray(new URL[0]);

    final String[] packageNames = packages.stream().map(Package::name).toArray(String[]::new);

    this.scanResult =
        new ClassGraph()
            //        .enableAllInfo()
            .enableClassInfo()
            .enableMethodInfo()
            .whitelistPackages(packageNames)
            .overrideClassLoaders(new URLClassLoader(urls))
            .scan();

    this.packages = packages;
  }

  @Override
  public ImmutableMultimap<Type, Type> getSuperClasses(
      final ImmutableSet<Type> acceptableSuperClasses,
      final ImmutableSet<Package> packages
  ) {
    final Iterable<ClassInfo> acceptableFilteredClasses =
        acceptableSuperClasses.stream()
                .map(type -> this.scanResult.getClassInfo(type.name()))
                .filter(Objects::nonNull)
            ::iterator;

    final ImmutableMultimap.Builder<Type, Type> subClassMapBuilder =
        new ImmutableMultimap.Builder<>();

    for (final ClassInfo classInfo : acceptableFilteredClasses) {
      final Iterable<ImmutableClassgraphType> subClassNames = classInfo.getSubclasses().stream()
          .map(ClassInfo::getName)
          .map(ImmutableClassgraphType::of)
          ::iterator;

      subClassMapBuilder.putAll(
          ImmutableClassgraphType.of(classInfo.getName()),
          subClassNames);
    }

    // TODO It is really stupid to go back and forth like this...
    final ImmutableMultimap<Type, Type> subClassMap = subClassMapBuilder.build();
    final ImmutableMultimap<Type, Type> superClassMap = subClassMap.inverse();

    return superClassMap;
  }

  @Override
  public ImmutableSet<MethodSignature> getSignatures(
      final ImmutableSet<Method> blacklist
  ) {
    return this.scanResult.getAllClasses().stream()
        .flatMap(classInfo -> classInfo.getDeclaredMethodAndConstructorInfo().stream())
        .map(ClassgraphMethodSignature::of)
        .collect(ImmutableSet.toImmutableSet());
  }

  @Override
  public void close() throws Exception {
    this.scanResult.close();
  }
}

@Value.Immutable
abstract class ClassgraphMethodSignature implements MethodSignature {

  public static ClassgraphMethodSignature of(final MethodInfo methodInfo) {
    return ImmutableClassgraphMethodSignature.builder()
        .name(name(methodInfo))
        .returnType(returnType(methodInfo))
        .addAllParameterTypes(parameterTypes(methodInfo))
        .isStatic(methodInfo.isStatic())
        .isConstructor(methodInfo.isConstructor())
        .declaringClass(declaringClass(methodInfo))
        .build();
  }

  private static String name(final MethodInfo methodInfo) {
    if (methodInfo.isConstructor()) {
      return declaringClass(methodInfo).name();
    }
    return methodInfo.getName();
  }

  private static Type returnType(final MethodInfo methodInfo) {
    if (methodInfo.isConstructor()) {
      return declaringClass(methodInfo);
    }
    return ImmutableClassgraphType.builder()
        .name(methodInfo.getTypeSignatureOrTypeDescriptor().getResultType().toString())
        .build();
  }

  private static Type declaringClass(final MethodInfo methodInfo) {
    return ImmutableClassgraphType.builder().name(methodInfo.getClassInfo().getName()).build();
  }

  private static List<Type> parameterTypes(final MethodInfo methodInfo) {
    return Arrays.stream(methodInfo.getParameterInfo())
        .map(MethodParameterInfo::getTypeSignatureOrTypeDescriptor)
        .map(
            typeSignature ->
                ImmutableClassgraphType.builder().name(typeSignature.toString()).build())
        .collect(Collectors.toList());
  }
}

@Value.Immutable
abstract class ClassgraphType implements Type {

  @Value.Parameter
  @Override
  public abstract String name();
}
