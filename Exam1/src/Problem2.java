import tester.Tester;

//represents an ancestor tree
interface IAT {
  // returns the number of generations away from this IAT the Person with the
  // given name is
  int numGensAway(String name);

  // returns the largest secret value of any ancestor tree
  int largestSecretNumber();

  // returns the largest secret value 
  public int largestSecretNumberHelper(int childSecretNumber);
}

//represents a leaf on an ancestor tree
class Unknown implements IAT {
  Unknown() {
  }

  // returns the number of generations away from this IAT the Person with the
  // given name is
  public int numGensAway(String name) {
    return -1;
  }

  // returns the largest secret value of any ancestor tree
  public int largestSecretNumber() {
    throw new IllegalStateException("No largest secret value");
  }

  // returns the largest secret value 
  public int largestSecretNumberHelper(int childSecretNumber) {
    throw new IllegalStateException("No largest secret value");
  }
}

//represents a person in an ancestor tree
class Person implements IAT {
  String name;
  int secretNumber;
  IAT dad;
  IAT mom;

  Person(String name, int secretNumber, IAT dad, IAT mom) {
    this.name = name;
    this.secretNumber = secretNumber;
    this.dad = dad;
    this.mom = mom;
  }

  // returns the number of generations away from this IAT the Person with the
  // given name is
  public int numGensAway(String name) {
    if (this.name.equals(name)) {
      return 0;
    }
    else {
      return Math.min(this.dad.numGensAway(name) + 1, this.mom.numGensAway(name) + 1);
    }
  }

  // returns the largest secret value of any ancestor tree
  public int largestSecretNumber() {
    return Math.max(this.mom.largestSecretNumberHelper(this.secretNumber),
        this.dad.largestSecretNumberHelper(this.secretNumber));
  }

  // returns the largest secret value 
  public int largestSecretNumberHelper(int childSecretNumber) {
    if (this.secretNumber > childSecretNumber) {
      return this.largestSecretNumber();
    }
    else {
      return childSecretNumber;
    }
  }
}

class ExamplesIAT {
  ExamplesIAT() {
  }

  IAT unknown = new Unknown();
  IAT davisSr = new Person("DavisSr", -23, new Unknown(), new Unknown());
  IAT edna = new Person("Edna", 67, new Unknown(), new Unknown());
  IAT davisJr = new Person("DavisJr", 23, davisSr, edna);
  IAT carl = new Person("Carl", 0, new Unknown(), new Unknown());
  IAT candace = new Person("Candace", 2, davisJr, new Unknown());
  IAT claire = new Person("Claire", 99, new Unknown(), new Unknown());
  IAT bill = new Person("Bill", 0, carl, candace);
  IAT bree = new Person("Bree", 10, new Unknown(), claire);
  IAT anthony = new Person("Anthony", -12, bill, bree);

  // to test the method numGensAway on the interface IAT
  boolean testNumGensAway(Tester t) {
    return t.checkExpect(this.anthony.numGensAway("Candace"), 2)
        && t.checkExpect(this.anthony.numGensAway("DavisJr"), 3)
        && t.checkExpect(this.anthony.numGensAway("Anthony"), 0);
  }

  // to test the method largestSecretNumber on the interface IAT
  boolean testLargestSecretNumber(Tester t) {
    return t.checkExpect(this.anthony.largestSecretNumber(), 99);
        // this.unknown.largestSecretNumber() --> IllegalStateException("No largest secret value");
  }
  
  // to test the method largestSecretNumber on the interface IAT
  boolean testLargestSecretNumberHelper(Tester t) {
    return t.checkExpect(this.anthony.largestSecretNumber(), 99);
  }
}