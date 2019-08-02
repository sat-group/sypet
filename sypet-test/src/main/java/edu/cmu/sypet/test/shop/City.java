package edu.cmu.sypet.test.shop;

public class City {

  private String name;

  public City(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    City city = (City) o;

    return name.equals(city.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "City{" + "name='" + name + '\'' + '}';
  }
}
