package edu.cmu.sypet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Point2DSynthesisTests {

  private static final ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
  private static final String TEST_LIB = ctxClassLoader.getResource("sypet-test.jar").getPath();
  private static final String RT8_LIB = ctxClassLoader.getResource("rt8.jar").getPath();

  private static final String testPackage = "edu.cmu.sypet.test";
  private static final String pointPackage = testPackage + ".point";

  private static final String pointClass = pointPackage + ".Point2D";
  private static final String anotherPointClass = pointPackage + ".AnotherPoint2D";
  private static final String vectorClass = pointPackage + ".Vector2D";

  void testTemplate(SynthesisTask task) {
    final Optional<String> maybeCode;
    try {
      maybeCode = SyPetAPI.synthesize(task);
    } catch (SyPetException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertTrue(maybeCode.isPresent());
  }

  @Disabled // Takes too long or GC exceeds memory limit.
  @Test
  void shouldSynthesizeAddPoints() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("addPoints")
            .paramNames(new ArrayList<>(Arrays.asList("p", "q")))
            .paramTypes(new ArrayList<>(Arrays.asList(pointClass, pointClass)))
            .returnType(pointClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage, testPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                "public boolean test1() throws Throwable {\n"
                    + "    Point2D point1 = new Point2D(2, 3);\n"
                    + "    Point2D point2 = new Point2D(-1, 4);\n"
                    + "    Point2D result = new Point2D(1, 7);\n"
                    + "    return result.equals(addPoints(point1, point2));\n"
                    + "  }")
            .build();

    testTemplate(task);
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
  }

  /** TODO */
  @Test
  void shouldSynthesizePointSetToSameX() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("setToSameX")
            .paramNames(new ArrayList<>(Arrays.asList("p", "q")))
            .paramTypes(new ArrayList<>(Arrays.asList(pointClass, pointClass)))
            .returnType("void")
            //                    .returnType(pointClass)  // This works, but I really want void.
            .packages(new ArrayList<>(Arrays.asList(pointPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "    %1$s.Point2D point1 = new %1$s.Point2D(2, 3);\n"
                        + "    %1$s.Point2D point2 = new %1$s.Point2D(-1, 4);\n"
                        + "    %1$s.Point2D target = new %1$s.Point2D(-1, 3);\n"
                        + "\n"
                        + "    setToSameX(point1, point2);\n"
                        + "    return point1.equals(target);\n"
                        + "  }",
                    pointPackage))
            .build();

    testTemplate(task);
    // Solution:
    //   public static void setToSameX(Point2D p, Point2D q) {
    //     double qX = q.getX();
    //     p.setX(qX);
    //   }
  }

  /** TODO */
  @Disabled
  @Test
  void shouldSynthesizePointConvert() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("convert")
            .paramNames(new ArrayList<>(Arrays.asList("p")))
            .paramTypes(new ArrayList<>(Arrays.asList(pointClass)))
            .returnType(anotherPointClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "  %1$s.Point2D mp = new %1$s.Point2D(0, 0);\n"
                        + "  %1$s.AnotherPoint2D p = convert(mp);\n"
                        + "\n"
                        + "  return mp.equals(p);\n"
                        + "}",
                    pointPackage))
            .build();

    testTemplate(task);
    // Solution:
    //   public static AnotherPoint2D convert(Point2D p) {
    //     double pX = p.getX();
    //     double pY = p.getY();
    //     AnotherPoint2D q = new AnotherPoint2D(pX, pY);
    //
    //     return q;
    //   }
  }

  /** TODO */
  @Test
  void shouldSynthesizePointFlip() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("pointFlip")
            .paramNames(new ArrayList<>(Arrays.asList("p")))
            .paramTypes(new ArrayList<>(Arrays.asList(pointClass)))
            .returnType(pointClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "    %1$s.Point2D point1 = new %1$s.Point2D(0, 1);\n"
                        + "    %1$s.Point2D point2 = pointFlip(point1);\n"
                        + "    %1$s.Point2D target = new %1$s.Point2D(1, 0);\n"
                        + "\n"
                        + "    return point2.equals(target);\n"
                        + "  }",
                    pointPackage))
            .build();

    testTemplate(task);
    // Solution:
    //   public static Point2D pointFlip(Point2D p) {
    //     double pX = p.getX();
    //     double pY = p.getY();
    //     Point2D q = new Point2D(pY, pX);
    //
    //     return q;
    //   }
  }

  /** TODO */
  @Test
  void shouldSynthesizeVectorFlip() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("vectorFlip")
            .paramNames(new ArrayList<>(Arrays.asList("p")))
            .paramTypes(new ArrayList<>(Arrays.asList(vectorClass)))
            .returnType(vectorClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "    %1$s point1 = new %1$s(0, 1);\n"
                        + "    %1$s point2 = vectorFlip(point1);\n"
                        + "    %1$s target = new %1$s(1, 0);\n"
                        + "\n"
                        + "    return point2.equals(target);\n"
                        + "  }",
                    vectorClass))
            .localSuperClasses(Arrays.asList(new String[] {pointClass}))
            .build();

    testTemplate(task);
    // Solution: same as pointFlip
  }

  /** TODO */
  @Test
  void shouldSynthesizePolymorphicVectorFlip() {
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName("vectorFlip")
            .paramNames(new ArrayList<>(Arrays.asList("p")))
            .paramTypes(new ArrayList<>(Arrays.asList(vectorClass)))
            .returnType(vectorClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "    %1$s.Vector2D point1 = new %1$s.Vector2D(0, 1);\n"
                        + "    %1$s.Point2D point2 = vectorFlip(point1);\n"
                        + "    %1$s.Point2D target = new %1$s.Vector2D(1, 0);\n"
                        + "\n"
                        + "    return point2.equals(target);\n"
                        + "  }",
                    pointPackage))
            .localSuperClasses(Arrays.asList(new String[] {pointClass}))
            .build();

    testTemplate(task);
    // Solution: same as pointFlip
  }

  /** TODO */
  @Test
  void shouldSynthesizePurePointTranslateX() {
    final String methodName = "pointTranslateX";
    final SynthesisTask task =
        ImmutableSynthesisTask.builder()
            .methodName(methodName)
            .paramNames(new ArrayList<>(Arrays.asList("p", "x")))
            .paramTypes(new ArrayList<>(Arrays.asList(pointClass, "double")))
            .returnType(pointClass)
            .packages(new ArrayList<>(Arrays.asList(pointPackage, testPackage)))
            .libs(new ArrayList<>(Arrays.asList(TEST_LIB)))
            .testCode(
                String.format(
                    "public boolean test() throws Throwable {\n"
                        + "    %1$s %3$s = new %1$s(1, 1);\n"
                        + "    %1$s %4$s = %2$s(%3$s, 2);\n"
                        + "    %1$s %5$s = new %1$s(3, 1);\n"
                        + "\n"
                        + "    return %4$s.equals(%5$s);\n"
                        + "  }",
                    pointClass, methodName, "point1", "point2", "target"))
            .build();

    testTemplate(task);
    // Solution:
    //   public static Point2D pointTranslateX(Point2D p, double x) {
    //     double pX = p.getX();
    //     double pY = p.getY();
    //     double newX = pX + x;
    //     Point2D q = new Point2D(newX, pY);
    //
    //     return q;
    //   }
  }
}
