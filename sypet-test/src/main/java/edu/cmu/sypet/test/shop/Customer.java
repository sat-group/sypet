package edu.cmu.sypet.test.shop;

import java.util.Arrays;
import java.util.List;

public class Customer {

  private String name;
  private City city;
  private List<Order> orders;

  public Customer(String name, City city, List<Order> orders) {
    this.name = name;
    this.city = city;
    this.orders = orders;
  }

  public Customer(String name, City city, Order... orders) {
    this(name, city, Arrays.asList(orders));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Customer customer = (Customer) o;

    if (!name.equals(customer.name)) {
      return false;
    }
    if (!city.equals(customer.city)) {
      return false;
    }
    return orders.equals(customer.orders);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + city.hashCode();
    result = 31 * result + orders.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Customer{" +
        "name='" + name + '\'' +
        ", city=" + city +
        ", orders=" + orders +
        '}';
  }
}
