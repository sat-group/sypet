package edu.cmu.sypet;

import edu.cmu.sypet.parser.SyPetConfig;
import edu.cmu.sypet.parser.SyPetInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// TODO Refactor test code duplication by using JUnit parameterized tests with <code>SynthesisTask</code>s.

class Point2DSynthesisTests {

  private static final ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
  private static final String TEST_LIB = ctxClassLoader.getResource("sypet-test.jar").getPath();
  private static final String RT8_LIB = ctxClassLoader.getResource("rt8.jar").getPath();

  private static final String testPackage = "edu.cmu.sypet.test";
  private static final String pointPackage = testPackage + ".point";

  private static final String pointClass = pointPackage + ".Point2D";
  private static final String anotherPointClass = pointPackage + ".AnotherPoint2D";
  private static final String vectorClass = pointPackage + ".Vector2D";

  @Disabled // Takes too long or GC exceeds memory limit.
  @Test
  void shouldSynthesizeAddPoints() {
    final String methodName = "addPoints";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p", "q"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(pointClass, pointClass));
    final String returnType = pointClass;
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = "public boolean test1() throws Throwable {\n"
        + "    Point2D point1 = new Point2D(2, 3);\n"
        + "    Point2D point2 = new Point2D(-1, 4);\n"
        + "    Point2D result = new Point2D(1, 7);\n"
        + "    return result.equals(addPoints(point1, point2));\n"
        + "  }";

    // Solution:
    //   public static Point2D addPoints(Point2D p, Point2D q) {
    //     double px = p.getX();
    //     double py = p.getY();
    //     double qx = q.getX();
    //     double qy = q.getY();
    //
    //     double x = px + qx;
    //     double y = py + qy;
    //
    //     Point2D point = new Point2D(x, y);
    //     return point;
    //   }

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
        .locLowerBound(7)
        .locUpperBound(7)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Test
  void shouldSynthesizePointSetToSameX() {
    final String methodName = "setToSameX";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p", "q"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(pointClass, pointClass));
    final String returnType = "void";
//    final String returnType = pointClass; // This works, but I really want void.
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = String.format("public boolean test() throws Throwable {\n"
        + "    %1$s.Point2D point1 = new %1$s.Point2D(2, 3);\n"
        + "    %1$s.Point2D point2 = new %1$s.Point2D(-1, 4);\n"
        + "    %1$s.Point2D target = new %1$s.Point2D(-1, 3);\n"
        + "\n"
        + "    setToSameX(point1, point2);\n"
        + "    return point1.equals(target);\n"
        + "  }", pointPackage);

    // Solution:
    //   public static void setToSameX(Point2D p, Point2D q) {
    //     double qX = q.getX();
    //     p.setX(qX);
    //   }

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
        .locLowerBound(2)
        .locUpperBound(2)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Test
  void shouldSynthesizePointConvert() {
    final String methodName = "convert";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(pointClass));
    final String returnType = anotherPointClass;
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = String.format("public boolean test() throws Throwable {\n"
        + "  %1$s.Point2D mp = new %1$s.Point2D(0, 0);\n"
        + "  %1$s.AnotherPoint2D p = convert(mp);\n"
        + "\n"
        + "  return mp.equals(p);\n"
        + "}", pointPackage);

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
//        .locLowerBound(1)
//        .locUpperBound(3)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Test
  void shouldSynthesizePointFlip() {
    final String methodName = "pointFlip";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(pointClass));
    final String returnType = pointClass;
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = String.format("public boolean test() throws Throwable {\n"
        + "    %1$s.Point2D point1 = new %1$s.Point2D(0, 1);\n"
        + "    %1$s.Point2D point2 = pointFlip(point1);\n"
        + "    %1$s.Point2D target = new %1$s.Point2D(1, 0);\n"
        + "\n"
        + "    return point2.equals(target);\n"
        + "  }", pointPackage);

    // Solution:
    //   public static Point2D pointFlip(Point2D p) {
    //     double pX = p.getX();
    //     double pY = p.getY();
    //     Point2D q = new Point2D(pY, pX);
    //
    //     return q;
    //   }

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
        .locLowerBound(4)
        .locUpperBound(4)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Test
  void shouldSynthesizeVectorFlip() {
    final String methodName = "vectorFlip";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(vectorClass));
    final String returnType = vectorClass;
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = String.format("public boolean test() throws Throwable {\n"
        + "    %1$s point1 = new %1$s(0, 1);\n"
        + "    %1$s point2 = vectorFlip(point1);\n"
        + "    %1$s target = new %1$s(1, 0);\n"
        + "\n"
        + "    return point2.equals(target);\n"
        + "  }", vectorClass);

    // Solution: same as pointFlip

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
        .locLowerBound(4)
        .locUpperBound(4)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Test
  void shouldSynthesizePurePointTranslateX() {
    final String methodName = "pointTranslateX";
    final List<String> paramNames = new ArrayList<>(Arrays.asList("p", "x"));
    final List<String> paramTypes = new ArrayList<>(Arrays.asList(pointClass, "double"));
    final String returnType = pointClass;
    final List<String> packages = new ArrayList<>(Arrays.asList(pointPackage, testPackage));
    final List<String> libs = new ArrayList<>(Arrays.asList(TEST_LIB));
    final String testCode = String.format("public boolean test() throws Throwable {\n"
        + "    %1$s %3$s = new %1$s(1, 1);\n"
        + "    %1$s %4$s = %2$s(%3$s, 2);\n"
        + "    %1$s %5$s = new %1$s(3, 1);\n"
        + "\n"
        + "    return %4$s.equals(%5$s);\n"
        + "  }", pointClass, methodName, "point1", "point2", "target");

    // Solution:
    //   public static Point2D pointTranslateX(Point2D p, double x) {
    //     double pX = p.getX();
    //     double pY = p.getY();
    //     double newX = pX + x;
    //     Point2D q = new Point2D(newX, pY);
    //
    //     return q;
    //   }

    final SyPetInput input = new SyPetInput.Builder(
        methodName, paramNames, paramTypes, returnType, packages, libs, testCode)
        .locLowerBound(4)
        .locUpperBound(4)
        .build();

    final SyPetConfig config = new SyPetConfig.Builder().build();
    final Optional<String> maybeCode = SyPetAPI.synthesize(input, config);

//    System.out.println(maybeCode.get());
    Assertions.assertTrue(maybeCode.isPresent());
  }
}

