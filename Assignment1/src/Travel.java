// to represent types of housing
interface IHousing {
}

// to represent a hut
class Hut implements IHousing {
  int capacity;
  int population;

  // the constructor
  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.capacity ...          -- int
   * ... this.population ...        -- int
   */
}

// to represent an inn
class Inn implements IHousing {
  String name;
  int capacity;
  int population;
  int stalls;

  // the constructor
  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.name ...          -- String
   * ... this.capacity ...      -- int
   * ... this.population ...    -- int
   * ... this.stalls ...        -- int    
   */
}

// to represent a castle
class Castle implements IHousing {
  String name;
  String familyName;
  int population;
  int carriageHouse;

  // the constructor
  Castle(String name, String familyName, int population, int carriageHouse) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriageHouse = carriageHouse;
  }
  
  /* TEMPLATE
   * Fields:
   * ... this.name ...            -- String
   * ... this.familyName ...      -- String
   * ... this.population ...      -- int
   * ... this.carriageHouse ...   -- int
   */
}

// to represent types of transportation
interface ITransportation {
}

// to represent a horse 
class Horse implements ITransportation {
  IHousing from;
  IHousing to;
  String name;
  String color;

  // the constructor
  Horse(IHousing from, IHousing to, String name, String color) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.color = color;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.from ...          -- IHousing
   * ... this.to ...            -- IHousing
   * ... this.name ...          -- String
   * ... this.color ...         -- String
   */
}

// to represent a carriage 
class Carriage implements ITransportation {
  IHousing from;
  IHousing to;
  int tonnage;

  // the constructor
  Carriage(IHousing from, IHousing to, int tonnage) {
    this.from = from;
    this.to = to;
    this.tonnage = tonnage;
  }
  
  /* TEMPLATE:
   * Fields:
   * ... this.from ...          -- IHousing
   * ... this.to ...            -- IHousing
   * ... this.tonnage ...       -- int
   */
}

// to represent examples and tests for housing and transportation
class ExamplesTravel {
  ExamplesTravel() {
  }

  IHousing hovel = new Hut(5, 1);
  IHousing chalet = new Hut(6, 3);

  IHousing winterfell = new Castle("Winterfell", "Stark", 500, 6);
  IHousing dover = new Castle("Dover", "Tudors", 600, 12);

  IHousing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  IHousing fadedeagle = new Inn("The Faded Eagle Tavern", 60, 30, 6);

  ITransportation horse1 = new Horse(this.hovel, this.winterfell, "Spirit", "brown");
  ITransportation horse2 = new Horse(this.crossroads, this.chalet, "Rocket", "white");

  ITransportation carriage1 = new Carriage(this.crossroads, this.winterfell, 40);
  ITransportation carriage2 = new Carriage(this.dover, this.fadedeagle, 30);
}
