package edu.cmu.sypet.test.shop;

import java.util.Arrays;
import java.util.List;

public class Shop {

  private String name;
  private List<Customer> customers;

  public Shop(String name, List<Customer> customers) {
    this.name = name;
    this.customers = customers;
  }

  public Shop(String name, Customer... customers) {
    this(name, Arrays.asList(customers));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Customer> getCustomers() {
    return customers;
  }

  public void setCustomers(List<Customer> customers) {
    this.customers = customers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Shop shop = (Shop) o;

    if (!name.equals(shop.name)) {
      return false;
    }
    return customers.equals(shop.customers);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + customers.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Shop{" +
        "name='" + name + '\'' +
        ", customers=" + customers +
        '}';
  }
}
