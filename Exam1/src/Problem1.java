import tester.Tester;

//represents a list of strings
interface ILoString {
  // returns a list of strings from this list in the same order, starting from the
  // given start position and including all strings up to and including the last
  // string
  ILoString remainingElements(int start);

  // helps remainingElements by keeping track of the index in this list
  ILoString remainingElementsHelper(int start);
  
  // returns true if all the elements of other are contained in this list 
  boolean allContained(ILoString other);
  
  // checks if the first of each list is equal and recursively calls on the rest of the list
  boolean allContainedHelper(String other);
}

//represents an empty list of strings
class MtLoString implements ILoString {
  MtLoString() {
  }

  // returns a list of strings from this list in the same order, starting from the
  // given start position and including all strings up to and including the last
  // string
  public ILoString remainingElements(int start) {
    return this;
  }

  // helps remainingElements by keeping track of the index in this list
  public ILoString remainingElementsHelper(int start) {
    return this;
  }
  
  // returns true if all the elements of other are contained in this list 
  public boolean allContained(ILoString other) {
    return false;
  }
  
  // checks if the first of each list is equal and recursively calls on the rest of the list
  public boolean allContainedHelper(String other) {
    return false;
  }
}

//represents a non-empty list of strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  // returns a list of strings from this list in the same order, starting from the
  // given start position and including all strings up to and including the last
  // string
  public ILoString remainingElements(int start) {
    if (start < 0) {
      return this;
    }
    else {
      return remainingElementsHelper(start);
    }
  }

  // helps remainingElements by keeping track of the index in this list
  public ILoString remainingElementsHelper(int start) {
    if (start == 0) {
      return this;
    }
    else {
      return this.rest.remainingElements(start - 1);
    }
  }
  
  // returns true if all the elements of other are contained in this list 
  public boolean allContained(ILoString other) {
    return other.allContainedHelper(this.first) || this.rest.allContained(other);
  }
  
  // checks if the first of each list is equal and recursively calls on the rest of the list
  public boolean allContainedHelper(String other) {
    return this.first.equals(other) || this.rest.allContainedHelper(other);
  }
 }

class ExamplesLists {
  ExamplesLists() {
  }

  ILoString mt = new MtLoString();
  ILoString ex1 = new ConsLoString("Hello", new ConsLoString("there",
      new ConsLoString("how", new ConsLoString("are", new ConsLoString("you", new MtLoString())))));
  ILoString ex2 = new ConsLoString("there",
      new ConsLoString("how", new ConsLoString("are", new ConsLoString("you", new MtLoString()))));
  ILoString ex3 = new ConsLoString("how", new ConsLoString("you", new MtLoString()));
  ILoString ex4 = new ConsLoString("how", new ConsLoString("today", new MtLoString()));

  // to test the method remainingElements on the interface ILoString
  boolean testRemainingElements(Tester t) {
    return 
        t.checkExpect(this.mt.remainingElements(0), this.mt) &&
        t.checkExpect(this.mt.remainingElements(2), this.mt) &&
        t.checkExpect(this.ex1.remainingElements(0), this.ex1) &&
        t.checkExpect(this.ex1.remainingElements(1), this.ex2) &&
        t.checkExpect(this.ex1.remainingElements(-1), this.ex1) &&
        t.checkExpect(this.ex1.remainingElements(5), this.mt);
  }
  
  // to test the method remainingElementsHelper on the interface ILoString
  boolean testRemainingElementsHelper(Tester t) {
    return 
        t.checkExpect(this.mt.remainingElementsHelper(0), this.mt) &&
        t.checkExpect(this.mt.remainingElementsHelper(2), this.mt) &&
        t.checkExpect(this.ex1.remainingElementsHelper(0), this.ex1) &&
        t.checkExpect(this.ex1.remainingElementsHelper(1), this.ex2) &&
        t.checkExpect(this.ex1.remainingElementsHelper(5), this.mt);
  }
  
 // to test the method allContained on the interface ILoString 
  boolean testAllContained(Tester t) {
    return 
        t.checkExpect(this.ex1.allContained(ex3), true) &&
        t.checkExpect(this.ex1.allContained(this.ex4), false) &&
        t.checkExpect(this.mt.allContained(ex1), true);
  }
  
  // to test the method allContainedHelper on the interface ILoString 
  boolean testAllContainedHelper(Tester t) {
    return 
        t.checkExpect(this.ex1.allContained(ex3), true) &&
        t.checkExpect(this.ex1.allContained(this.ex4), false) &&
        t.checkExpect(this.mt.allContained(ex1), true);
  }
}