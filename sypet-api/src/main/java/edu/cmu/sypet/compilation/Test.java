package edu.cmu.sypet.compilation;

import com.google.common.collect.ImmutableSet;
import edu.cmu.sypet.java.Jar;
import edu.cmu.sypet.java.Program;
import edu.cmu.sypet.java.TestProgram;

/**
 * Write code given the tests and classes.
 *
 * @author Ruben Martins
 * @author Kaige Liu
 */
public class Test {

  private static final String CLASSNAME = "Target";

  /**
   * run test class based on the synthesized code and test code.
   *
   * @param code synthesized code
   * @param testCode test code with name "test"
   * @return whether test pasted
   */
  public static boolean runTest(
      final Program code,
      final TestProgram testCode,
      final ImmutableSet<Jar> libs
  ) {
    // Create file;
    String classCode = writeCode(code, testCode);
    Compile compile = new Compile(CLASSNAME);
    return compile.runTest(classCode, libs);
  }

  private static String writeCode(
      final Program code,
      final TestProgram testProgram
  ) {
    String testCode = testProgram.code();

    StringBuilder builder = new StringBuilder()
        .append("public class " + CLASSNAME + " {\n")
        .append(code.code());

    // test cases need to be static for compilation to succeed
    if (!testCode.contains("public static")) {
      testCode = testCode.replace("public", "public static");
    }

    String[] tokens = testCode.split(" ");
    for (int l = 0; l < tokens.length; l++) {
      if (tokens[l].equals("static")) {
        String name = tokens[l + 2];

        if (!name.equals("test()")) {
          testCode = testCode.replace(name, "test()");
        }
        break;
      }
    }

    builder.append(testCode).append("}\n");
    return builder.toString();
  }
}
