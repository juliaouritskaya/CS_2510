// to represent an ice cream
interface IIceCream {
}

// to represent an empty serving
class EmptyServing implements IIceCream {
  boolean cone;

  // the constructor
  EmptyServing(boolean cone) {
    this.cone = cone;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.cone ...          -- boolean
   */
}

// to represent a scoop
class Scooped implements IIceCream {
  IIceCream more;
  String flavor;

  // the constructor
  Scooped(IIceCream more, String flavor) {
    this.more = more;
    this.flavor = flavor;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.more ...          -- IIceCream
   * ... this.flavor ...        -- String
   */
}

class ExamplesIceCream {
  ExamplesIceCream() {
  }

  IIceCream cone = new EmptyServing(true);
  IIceCream cup = new EmptyServing(false);

  IIceCream mintchip = new Scooped(this.cup, "mint chip");
  IIceCream coffee = new Scooped(this.mintchip, "coffee");
  IIceCream blackraspberry = new Scooped(this.coffee, "black raspberry");
  IIceCream order1 = new Scooped(this.blackraspberry, "caramel swirl");

  IIceCream chocolate = new Scooped(this.cone, "chocolate");
  IIceCream vanilla = new Scooped(this.chocolate, "vanilla");
  IIceCream order2 = new Scooped(this.vanilla, "strawberry");
}
