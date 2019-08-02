package edu.cmu.sypet.test.shop;

public final class ShopDB {

  //products
  public static final Product idea = new Product("IntelliJ IDEA Ultimate", 199.0);
  public static final Product reSharper = new Product("ReSharper", 149.0);
  public static final Product dotTrace = new Product("DotTrace", 159.0);
  public static final Product dotMemory = new Product("DotTrace", 129.0);
  public static final Product dotCover = new Product("DotCover", 99.0);
  public static final Product appCode = new Product("AppCode", 99.0);
  public static final Product phpStorm = new Product("PhpStorm", 99.0);
  public static final Product pyCharm = new Product("PyCharm", 99.0);
  public static final Product rubyMine = new Product("RubyMine", 99.0);
  public static final Product webStorm = new Product("WebStorm", 49.0);
  public static final Product teamCity = new Product("TeamCity", 299.0);
  public static final Product youTrack = new Product("YouTrack", 500.0);
  //customers
  public static final String lucas = "Lucas";
  public static final String cooper = "Cooper";
  public static final String nathan = "Nathan";
  public static final String reka = "Reka";
  public static final String bajram = "Bajram";
  public static final String asuka = "Asuka";
  //cities
  public static final City Canberra = new City("Canberra");
  public static final City Vancouver = new City("Vancouver");
  public static final City Budapest = new City("Budapest");
  public static final City Ankara = new City("Ankara");
  public static final City Tokyo = new City("Tokyo");
  // the shop
  public static final Shop shop = new Shop("jb test shop",
      new Customer(lucas, Canberra,
          new Order(reSharper),
          new Order(reSharper, dotMemory, dotTrace)
      ),
      new Customer(cooper, Canberra),
      new Customer(nathan, Vancouver,
          new Order(rubyMine, webStorm)
      ),
      new Customer(reka, Budapest,
          new Order(false, idea),
          new Order(false, idea),
          new Order(idea)
      ),
      new Customer(bajram, Ankara,
          new Order(reSharper)
      ),
      new Customer(asuka, Tokyo,
          new Order(idea)
      )
  );

  private ShopDB() {
  }
}
