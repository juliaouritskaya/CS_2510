import tester.Tester;

// to represent an embroidery piece
class EmbroideryPiece {
  String name;
  IMotif motif;

  // the constructor
  EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }

  /* Template:
   * Fields:
   * this.name -- String 
   * this.motif -- IMotif 
   * 
   * Methods:
   * this.averageDifficulty() -- double 
   * this.embroideryInfo() -- String
   * 
   * Methods for fields:
   * this.motif.getDifficulty() -- double
   * this.motif.count() -- int
   * this.motif.getNameType() -- String
   */

  // Computes the average difficulty of all of the cross-stitch
  // and chain-stitch motifs in an EmbroideryPiece
  double averageDifficulty() {
    if (this.motif.count() == 0) {
      return 0;
    }
    else {
      return this.motif.getDifficulty() / this.motif.count();
    }
  }

  // Produces one string that has in it all the names of cross-stitch and
  // chain-stitch motifs in an EmbroideryPiece, their stitch types in parentheses,
  // and each motif separated by comma and space
  String embroideryInfo() {
    return this.name + ": " + this.motif.getNameType() + ".";
  }
}

// to represent a motif
interface IMotif {

  // Get the difficulty level of the motif
  double getDifficulty();

  // Count the motifs
  int count();

  // Get the name and type of the motif
  String getNameType();
}

// to represent a cross stitch motif
class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  // the constructor
  CrossStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /* Template:
   * Fields: 
   * this.description -- String 
   * this.difficulty -- double
   * 
   * Methods:
   * this.getDifficulty() -- double
   * this.count() -- int
   * this.getNameType() -- String
   */

  // Get the difficulty level of the cross stitch motif
  public double getDifficulty() {
    return this.difficulty;
  }

  // Count the motif
  public int count() {
    return 1;
  }

  // Get the name and type of the motif
  public String getNameType() {
    return this.description + " (cross stitch)";
  }
}

// to represent a chain stitch motif
class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  // the constructor
  ChainStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /* Template:
   * Fields: 
   * this.description -- String 
   * this.difficulty -- double
   * 
   * Methods:
   * this.getDifficulty() -- double
   * this.count() -- int
   * this.getNameType() -- String
   */

  // Get the difficulty level of the chain stitch motif
  public double getDifficulty() {
    return this.difficulty;
  }

  // Count the motif
  public int count() {
    return 1;
  }

  // Get the name and type of the motif
  public String getNameType() {
    return this.description + " (chain stitch)";
  }
}

// to represent a group motif 
class GroupMotif implements IMotif {
  String description;
  ILoMotif motifs;

  // the constructor
  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  /* Template:
   * Fields: 
   * this.description -- String 
   * this.motifs -- ILoMotif
   * 
   * Methods:
   * this.getDifficulty() -- double
   * this.count() -- int
   * this.getNameType() -- String
   * 
   * Methods for fields:
   * this.motifs.getLoDifficulty() -- double
   * this.motifs.count() -- int
   * this.motifs.combineNameType() -- String
   */

  // Get the difficulty level of the group motif
  public double getDifficulty() {
    return this.motifs.getLoDifficulty();
  }

  // Count the motif
  public int count() {
    return this.motifs.count();
  }

  // Get the name and type of the motif
  public String getNameType() {
    return this.motifs.combineNameType();
  }
}

// to represent a list of motifs
interface ILoMotif {

  // Get the difficulty level of the list of motifs
  double getLoDifficulty();

  // Count the motifs in the list of motifs
  int count();

  // Combine name and types of motifs
  String combineNameType();
}

// to represent an empty list of motifs
class MtLoMotif implements ILoMotif {
  MtLoMotif() {
  }

  // Get the difficulty level of the empty list of motifs
  public double getLoDifficulty() {
    return 0;
  }

  // Count the motifs in this list
  public int count() {
    return 0;
  }

  // Combine name and type of motifs in list
  public String combineNameType() {
    return "";
  }
}

// to represent a non-empty list of motifs
class ConsLoMotif implements ILoMotif {
  IMotif first;
  ILoMotif rest;

  // the constructor
  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Template
   * Fields:
   * this.first -- IMotif
   * this.rest -- ILoMotif
   * 
   * Methods:
   * this.getLoDifficulty() -- double
   * this.count() -- double
   * this.combineNameType() -- String
   * 
   * Methods for fields:
   * this.first.getDifficulty() -- double
   * this.first.count() -- int
   * this.first.getNameType() -- String
   * this.rest.getLoDifficulty() -- double
   * this.rest.count() -- int
   * this.rest.combineNameType() -- String
   */

  // Get the difficulty level of the non-empty list of motifs
  public double getLoDifficulty() {
    return this.first.getDifficulty() + this.rest.getLoDifficulty();
  }

  // Count the motifs in this list
  public int count() {
    return this.first.count() + this.rest.count();
  }

  // Combine name and type of motifs in the list
  public String combineNameType() {
    if (this.rest.combineNameType().equals("")) {
      return this.first.getNameType();
    }
    else {
      return this.first.getNameType() + ", " + this.rest.combineNameType();
    }
  }
}

// to represent examples and tests for embroidery
class ExamplesEmbroidery {
  ExamplesEmbroidery() {
  }

  // examples of motifs
  IMotif daisy = new CrossStitchMotif("daisy", 3.2);
  IMotif poppy = new ChainStitchMotif("poppy", 4.75);
  IMotif rose = new CrossStitchMotif("rose", 5.0);

  ILoMotif mtlist = new MtLoMotif();

  ILoMotif listFlowers = new ConsLoMotif(this.rose,
      new ConsLoMotif(this.poppy, new ConsLoMotif(this.daisy, this.mtlist)));
  IMotif flowers = new GroupMotif("flowers", this.listFlowers);

  IMotif tree = new ChainStitchMotif("tree", 3.0);
  IMotif bird = new CrossStitchMotif("bird", 4.5);

  ILoMotif listNature = new ConsLoMotif(this.bird,
      new ConsLoMotif(this.tree, new ConsLoMotif(this.flowers, this.mtlist)));
  IMotif nature = new GroupMotif("nature", this.listNature);

  // examples of embroidery pieces
  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", this.nature);

  // test the method averageDifficulty for the class EmbroideryPiece
  boolean testAverageDifficulty(Tester t) {
    return t.checkInexact(this.pillowCover.averageDifficulty(), 4.09, 0.01);
  }

  // test the method getDifficulty for the interface IMotif
  boolean testGetDifficulty(Tester t) {
    return t.checkInexact(this.bird.getDifficulty(), 4.5, 0.01)
        && t.checkInexact(this.tree.getDifficulty(), 3.0, 0.01)
        && t.checkInexact(this.flowers.getDifficulty(), 12.95, 0.01);
  }

  // test the method count for the interface IMotif
  boolean testCount(Tester t) {
    return t.checkExpect(this.bird.count(), 1) && t.checkExpect(this.tree.count(), 1)
        && t.checkExpect(this.flowers.count(), 3);
  }

  // test the method getLoDifficulty for the interface ILoMotif
  boolean testGetLoDifficulty(Tester t) {
    return t.checkInexact(this.listFlowers.getLoDifficulty(), 12.95, 0.01)
        && t.checkInexact(this.listNature.getLoDifficulty(), 20.45, 0.01);
  }

  // test the method count for the interface ILoMotif
  boolean testCount2(Tester t) {
    return t.checkExpect(this.listFlowers.count(), 3) && t.checkExpect(this.listNature.count(), 5)
        && t.checkExpect(this.mtlist.count(), 0);
  }

  // test the method embroideryInfo for the class EmbroideryPiece
  boolean testEmbroideryInfo(Tester t) {
    return t.checkExpect(this.pillowCover.embroideryInfo(),
        "Pillow Cover: bird (cross stitch), tree (chain stitch), "
            + "rose (cross stitch), poppy (chain stitch), " + "daisy (cross stitch).");
  }
}
