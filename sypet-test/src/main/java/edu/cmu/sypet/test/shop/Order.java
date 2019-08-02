package edu.cmu.sypet.test.shop;

import java.util.Arrays;
import java.util.List;

public class Order {

  private List<Product> products;
  private Boolean isDelivered;

  public Order(List<Product> products, Boolean isDelivered) {
    this.products = products;
    this.isDelivered = isDelivered;
  }

  public Order(Boolean isDelivered, Product... products) {
    this(Arrays.asList(products), isDelivered);
  }

  public Order(Product... products) {
    this(true, products);
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Boolean getDelivered() {
    return isDelivered;
  }

  public void setDelivered(Boolean delivered) {
    isDelivered = delivered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Order order = (Order) o;

    if (!products.equals(order.products)) {
      return false;
    }
    return isDelivered.equals(order.isDelivered);
  }

  @Override
  public int hashCode() {
    int result = products.hashCode();
    result = 31 * result + isDelivered.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Order{" +
        "products=" + products +
        ", isDelivered=" + isDelivered +
        '}';
  }

}
