import tester.Tester;

// to represent a bagel recipe 
class BagelRecipe {
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;

  // main constructor that takes in all fields and enforces all constraints to
  // ensure a perfect bagel recipe
  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    this.flour = new Utils().checkWeight(flour, water, "Invalid weight:" + Double.toString(flour));

    this.water = new Utils().checkWeight(water, flour, "Invalid weight:" + Double.toString(water));

    this.yeast = new Utils().checkWeight(yeast, malt, "Invalid weight:" + Double.toString(yeast));

    this.malt = new Utils().checkWeight(malt, yeast, "Invalid weight:" + Double.toString(malt));

    this.salt = new Utils().checkEquals(flour, yeast, salt);
  }

  // new constructor that only requires the weights of flour and yeast, and
  // produces a perfect bagel recipe
  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, (flour * 0.05 - yeast), yeast);
  }

  // new constructor that takes in flour, yeast, and salt as volumes rather than
  // weights, and tries to produce a perfect recipe
  BagelRecipe(double flour, double yeast, double salt) {
    this(flour * 4.25, flour * 4.25, (yeast * 5) / 48, (salt * 10) / 48, (yeast * 5) / 48);
  }

  // returns true if the same ingredients have the same weights to within 0.001
  // ounces
  boolean sameRecipe(BagelRecipe other) {
    return (Math.abs(this.flour - other.flour) <= 0.001)
        && (Math.abs(this.water - other.flour) <= 0.001)
        && (Math.abs(this.yeast - other.yeast) <= 0.001)
        && (Math.abs(this.salt - other.salt) <= 0.001)
        && (Math.abs(this.malt - other.malt) <= 0.001);
  }
}

// to represent a utility class 
class Utils {
  // checks if the ingredients have the same weight
  double checkWeight(double ingredient1, double ingredient2, String msg) {
    if ((Math.abs(ingredient1 - ingredient2)) <= 0.001) {
      return ingredient1;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }

  // checks if the weight of the salt is correct (salt + yeast = flour * 0.05)
  double checkEquals(double flour, double yeast, double salt) {
    if (Math.abs((flour * 0.05) - (salt + yeast)) <= 0.001) {
      return salt;
    }
    else {
      throw new IllegalArgumentException("Invalid weight:" + Double.toString(salt));
    }
  }
}

// to represent examples and tests for bagel recipes
class ExamplesBagelRecipe {
  ExamplesBagelRecipe() {
  }

  BagelRecipe perfect = new BagelRecipe(25.5, 25.5, 1.25, 0.025, 1.25);
  BagelRecipe perfect2 = new BagelRecipe(25.5, 1.25);
  BagelRecipe perfect3 = new BagelRecipe(5, 2, 4.1);
  BagelRecipe perfect4 = new BagelRecipe(20, 0.8);
  BagelRecipe perfect5 = new BagelRecipe(5.0, 1.0, 4.6);

  // test the method sameRecipe for the class BagelRecipe
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(this.perfect.sameRecipe(this.perfect2), true)
        && t.checkExpect(this.perfect.sameRecipe(this.perfect3), false)
        && t.checkExpect(this.perfect3.sameRecipe(this.perfect4), false);
  }

  // test the method checkWeight for the class Utils
  boolean testCheckWeight(Tester t) {
    return t.checkExpect(
        new Utils().checkWeight(1.5, 1.5, "Invalid weight:" + Double.toString(1.5)), 1.5)
        && t.checkException(new IllegalArgumentException("Invalid weight:" + Double.toString(1.25)),
            new Utils(), "checkWeight", 1.25, 1.5, "Invalid weight:" + Double.toString(1.25));
  }

  // test the method checkEquals for the class Utils
  boolean testCheckEquals(Tester t) {
    return t.checkExpect(new Utils().checkEquals(25.5, 1.25, 0.025), 0.025)
        && t.checkException(new IllegalArgumentException("Invalid weight:" + Double.toString(1.25)),
            new Utils(), "checkEquals", 25.5, 1.25, 1.25);
  }
}