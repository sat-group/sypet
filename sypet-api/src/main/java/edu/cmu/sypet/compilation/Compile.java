package edu.cmu.sypet.compilation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.cmu.sypet.java.Jar;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * This represents the compilation of the test cases synthesized by SyPet.
 *
 * @author Ruben Martins
 * @author Yu Feng
 */
class Compile {

  private static final boolean DISPLAY_ERROR = false;
  private final String className;

  Compile(final String classname) {
    className = classname;
  }

  @SuppressWarnings("rawtypes")
  private Class compileClass(final String program, final ImmutableSet<Jar> libs) {
    if (DISPLAY_ERROR) {
      System.out.println(program);
    }
    String classpath = genClassPath(libs);
    try {
      JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

      MyDiagnosticListener c = new MyDiagnosticListener();
      StandardJavaFileManager sjfm = javac.getStandardFileManager(c, Locale.ENGLISH, null);
      SpecialClassLoader cl = new SpecialClassLoader(libs);
      SpecialJavaFileManager fileManager = new SpecialJavaFileManager(sjfm, cl);

      List<String> options = new ArrayList<>();
      options.add("-cp");
      options.add(classpath);
      List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, program));
      Writer out = DISPLAY_ERROR ? new PrintWriter(System.err) : null;
      JavaCompiler.CompilationTask compile =
          javac.getTask(out, fileManager, c, options, null, compilationUnits);
      boolean mCompilationSuccess = compile.call();
      if (mCompilationSuccess) {
        return cl.findClass(className);
      }
    } catch (Exception e) {
      if (DISPLAY_ERROR) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private static String genClassPath(final ImmutableSet<Jar> libs) {
    StringBuilder builder = new StringBuilder();
    for (final Jar lib : libs) {
      builder.append(lib.name());
      builder.append(':');
    }
    builder.append('.');
    return builder.toString();
  }

  public boolean runTest(final String code, final ImmutableSet<Jar> libs) {
    Class<?> compiledClass = compileClass(code, libs);
    if (compiledClass == null) {
      return false;
    }

    boolean success = false;
    try {
      Method method = compiledClass.getMethod("test");
      success = (boolean) method.invoke(null);
    } catch (Exception e) {
      if (DISPLAY_ERROR) {
        e.printStackTrace();
      }
    }

    return success;
  }

  static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
    }
  }
}

class MemorySource extends SimpleJavaFileObject {

  private final String src;

  public MemorySource(final String name, final String src) {
    super(URI.create("file:///" + name + ".java"), Kind.SOURCE);
    this.src = src;
  }

  public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
    return src;
  }

  public OutputStream openOutputStream() {
    throw new IllegalStateException();
  }

  public InputStream openInputStream() {
    return new ByteArrayInputStream(src.getBytes());
  }
}

class SpecialJavaFileManager extends ForwardingJavaFileManager {

  private final SpecialClassLoader xcl;

  @SuppressWarnings("unchecked")
  public SpecialJavaFileManager(
      final StandardJavaFileManager sjfm,
      final SpecialClassLoader xcl
  ) {
    super(sjfm);
    this.xcl = xcl;
  }

  public JavaFileObject getJavaFileForOutput(
      final Location location,
      final String name,
      final JavaFileObject.Kind kind,
      final FileObject sibling
  ) {
    MemoryByteCode mbc = new MemoryByteCode(name);
    xcl.addClass(name, mbc);
    return mbc;
  }

  public ClassLoader getClassLoader(final Location location) {
    return xcl;
  }
}

class MemoryByteCode extends SimpleJavaFileObject {

  private ByteArrayOutputStream baos;

  public MemoryByteCode(final String name) {
    super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
  }

  public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
    throw new IllegalStateException();
  }

  public OutputStream openOutputStream() {
    baos = new ByteArrayOutputStream();
    return baos;
  }

  public InputStream openInputStream() {
    throw new IllegalStateException();
  }

  public byte[] getBytes() {
    return baos.toByteArray();
  }
}

class SpecialClassLoader extends ClassLoader {

  private final Map<String, MemoryByteCode> map = new HashMap<>();
  private final ImmutableSet<Jar> libs;
  private URLClassLoader cl = null;

  public SpecialClassLoader(final ImmutableSet<Jar> libs) {
    this.libs = libs;
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    MemoryByteCode mbc = map.get(name);
    if (mbc == null) {
      URL[] urls = getUrls(libs);
      if (cl == null) {
        cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
      }
      return cl.loadClass(name);
    } else {
      return defineClass(name, mbc.getBytes(), 0, mbc.getBytes().length);
    }
  }

  public void addClass(final String name, final MemoryByteCode mbc) {
    map.put(name, mbc);
  }

  private URL[] getUrls(final ImmutableSet<Jar> libs) {
    final ImmutableList.Builder<URL> urlsBuilder = ImmutableList.builder();

    try {
      for (final Jar jar : libs) {
        urlsBuilder.add(new File(jar.name()).toURI().toURL());
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return urlsBuilder.build().toArray(new URL[0]);
  }
}
