package edu.cmu.sypet.test.point;

/** A copy of {@link Point2D}. */
public class AnotherPoint2D {

  private double x;
  private double y;

  public AnotherPoint2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnotherPoint2D that = (AnotherPoint2D) o;

    if (Double.compare(that.x, x) != 0) {
      return false;
    }
    return Double.compare(that.y, y) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Podouble2D{x=" + x + ", y=" + y + '}';
  }
}
