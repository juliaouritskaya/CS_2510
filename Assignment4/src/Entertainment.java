import tester.Tester;

interface IEntertainment {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  // is this Magazine the same as that one?
  boolean sameMagazine(Magazine that);

  // is this TVSeries the same as that one?
  boolean sameTVSeries(TVSeries that);

  // is this Podcast the same as that one?
  boolean samePodcast(Podcast that);
}

abstract class AEntertainment implements IEntertainment {
  String name;
  double price;
  int installments;

  // the constructor
  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  // computes the price of a yearly subscription to this Entertainment
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this Podcast
  public int duration() {
    return this.installments * 50;
  }

  // produce a String that shows the name and price of this Magazine
  public String format() {
    return this.name + ", " + Double.toString(this.price) + ".";
  }
  
  // is this IEntertainment the same as that one?
  abstract public boolean sameEntertainment(IEntertainment that);
  
  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine that) {
    return false;
  }
  
  // is this TVSeries the same as that one?
  public boolean sameTVSeries(TVSeries that) {
    return false;
  }
  
  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  @Override
  public int duration() {
    return this.pages * this.installments * 5;
  }

  // is this IEntertainment the same as that one?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }

  // is this Magazine the same as that one?
  @Override
  public boolean sameMagazine(Magazine that) {
    return (this.name.equals(that.name)) && (this.price == that.price)
        && (this.genre.equals(that.genre)) && (this.pages == that.pages)
        && (this.installments == that.installments);
  }
}

class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }

  // is this IEntertainment the same as that one?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }

  // is this TVSeries the same as that one?
  @Override
  public boolean sameTVSeries(TVSeries that) {
    return (this.name.equals(that.name)) && (this.price == that.price)
        && (this.installments == that.installments) && (this.corporation.equals(that.corporation));
  }
}

class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  // is this IEntertainment the same as that one?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }

  // is this Podcast the same as that one?
  @Override
  public boolean samePodcast(Podcast that) {
    return (this.name.equals(that.name)) && (this.price == that.price)
        && (this.installments == that.installments);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment vogue = new Magazine("Vogue", 8.95, "Fashion", 229, 12);

  Magazine rollingStone2 = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  Magazine vogue2 = new Magazine("Vogue", 8.95, "Fashion", 229, 12);

  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment greysAnatomy = new TVSeries("Grey's Anatomy", 6.0, 388, "Hulu");

  TVSeries houseOfCards2 = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  TVSeries greysAnatomy2 = new TVSeries("Grey's Anatomy", 6.0, 388, "Hulu");

  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment theDaily = new Podcast("The Daily", 1.0, 400);

  Podcast serial2 = new Podcast("Serial", 0.0, 8);
  Podcast theDaily2 = new Podcast("The Daily", 1.0, 400);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.vogue.totalPrice(), 8.95 * 12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.greysAnatomy.totalPrice(), 6.0 * 388, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.theDaily.totalPrice(), 1.0 * 400, .0001);
  }

  // test the method duration for the interface IEntertainment
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 60 * 12 * 5)
        && t.checkExpect(this.vogue.duration(), 229 * 12 * 5)
        && t.checkExpect(this.houseOfCards.duration(), 13 * 50)
        && t.checkExpect(this.greysAnatomy.duration(), 388 * 50)
        && t.checkExpect(this.serial.duration(), 8 * 50)
        && t.checkExpect(this.theDaily.duration(), 400 * 50);
  }

  // test the method format for the interface IEntertainment
  boolean testFormat(Tester t) {
    return t.checkExpect(this.rollingStone.format(), "Rolling Stone, 2.55.")
        && t.checkExpect(this.vogue.format(), "Vogue, 8.95.")
        && t.checkExpect(this.houseOfCards.format(), "House of Cards, 5.25.")
        && t.checkExpect(this.greysAnatomy.format(), "Grey's Anatomy, 6.0.")
        && t.checkExpect(this.serial.format(), "Serial, 0.0.")
        && t.checkExpect(this.theDaily.format(), "The Daily, 1.0.");
  }

  // test the method sameMagazine for the class Magazine
  boolean testSameMagazine(Tester t) {
    return t.checkExpect(this.rollingStone2.sameMagazine(this.vogue2), false)
        && t.checkExpect(this.vogue2.sameMagazine(this.rollingStone2), false)
        && t.checkExpect(this.vogue2.sameMagazine(this.vogue2), true)
        && t.checkExpect(this.rollingStone2.sameMagazine(this.rollingStone2), true) &&

        t.checkExpect(this.houseOfCards2.sameTVSeries(this.greysAnatomy2), false)
        && t.checkExpect(this.greysAnatomy2.sameTVSeries(this.houseOfCards2), false)
        && t.checkExpect(this.greysAnatomy2.sameTVSeries(this.greysAnatomy2), true)
        && t.checkExpect(this.houseOfCards2.sameTVSeries(this.houseOfCards2), true) &&

        t.checkExpect(this.serial2.samePodcast(this.theDaily2), false)
        && t.checkExpect(this.theDaily2.samePodcast(this.serial2), false)
        && t.checkExpect(this.theDaily2.samePodcast(this.theDaily2), true)
        && t.checkExpect(this.serial2.samePodcast(this.serial2), true) &&

        t.checkExpect(this.rollingStone.sameEntertainment(this.vogue), false)
        && t.checkExpect(this.vogue.sameEntertainment(this.rollingStone), false)
        && t.checkExpect(this.vogue.sameEntertainment(this.vogue), true) &&

        t.checkExpect(this.houseOfCards.sameEntertainment(this.greysAnatomy), false)
        && t.checkExpect(this.greysAnatomy.sameEntertainment(this.houseOfCards), false)
        && t.checkExpect(this.greysAnatomy.sameEntertainment(this.greysAnatomy), true) &&

        t.checkExpect(this.serial.sameEntertainment(this.theDaily), false)
        && t.checkExpect(this.theDaily.sameEntertainment(this.serial), false)
        && t.checkExpect(this.theDaily.sameEntertainment(this.theDaily), true) &&

        t.checkExpect(this.rollingStone.sameEntertainment(this.houseOfCards), false)
        && t.checkExpect(this.serial.sameEntertainment(this.vogue), false)
        && t.checkExpect(this.theDaily.sameEntertainment(this.greysAnatomy), false);
  }
}
