package edu.cmu.sypet.java;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
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
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;


public class ClassgraphTypeFinder implements TypeFinder {

  private final ScanResult scanResult;
  private final Collection<String> packages;

  public ClassgraphTypeFinder(
      final Collection<String> jars,
      final Collection<String> packages
  ) throws MalformedURLException {

    final ImmutableCollection.Builder<URL> urlsBuilder = ImmutableList.builder();
    for (String jar : jars) {
      urlsBuilder.add(new URL("https", "localhost", jar));
    }

    final URL[] urls = urlsBuilder.build().toArray(new URL[0]);

    this.scanResult = new ClassGraph()
//        .enableAllInfo()
        .enableClassInfo()
        .enableMethodInfo()
        .whitelistPackages(packages.toArray(new String[0]))
        .overrideClassLoaders(new URLClassLoader(urls))
        .scan();

    this.packages = packages;
  }

  @Override
  public ImmutableMultimap<String, String> getSuperClasses(
      Set<String> acceptableSuperClasses,
      Collection<String> packages
  ) {
    final Iterable<ClassInfo> acceptableFilteredClasses = acceptableSuperClasses.stream()
        .map(clazz -> this.scanResult.getClassInfo(clazz))
        .filter(Objects::nonNull)
        ::iterator;

    final ImmutableMultimap.Builder<String, String> subClassMapBuilder =
        new ImmutableMultimap.Builder<>();

    for (ClassInfo classInfo : acceptableFilteredClasses) {
      Iterable<String> subClassNames = classInfo.getSubclasses().stream()
          .map(ClassInfo::getName)
          ::iterator;

      subClassMapBuilder.putAll(classInfo.getName(), subClassNames);
    }

    // TODO It is really stupid to go back and forth like this...
    final ImmutableMultimap<String, String> subClassMap = subClassMapBuilder.build();
    final ImmutableMultimap<String, String> superClassMap = subClassMap.inverse();

    return superClassMap;
  }

  @Override
  public List<MethodSignature> getSignatures(List<String> blacklist) {
    return this.scanResult.getAllClasses().stream()
        .flatMap(clazz -> clazz.getDeclaredMethodAndConstructorInfo().stream())
        .map(ClassgraphMethodSignature::of)
        .collect(Collectors.toList());
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

  private static String name(MethodInfo methodInfo) {
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
        .name(methodInfo
            .getTypeSignatureOrTypeDescriptor()
            .getResultType()
            .toString())
        .build();
  }

  private static Type declaringClass(final MethodInfo methodInfo) {
    return ImmutableClassgraphType.builder()
        .name(methodInfo.getClassInfo().getName())
        .build();
  }

  private static List<Type> parameterTypes(final MethodInfo methodInfo) {
    return Arrays.stream(methodInfo.getParameterInfo())
        .map(MethodParameterInfo::getTypeSignatureOrTypeDescriptor)
        .map(typeSignature -> ImmutableClassgraphType.builder()
            .name(typeSignature.toString())
            .build())
        .collect(Collectors.toList());
  }

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}

@Value.Immutable
abstract class ClassgraphType implements Type {

  @Value.Parameter
  @Override
  public abstract String name();

  // TODO XXX
  @Override
  public String toString() {
    return name();
  }
}

