// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();

  // produces a new list of Strings, sorted in alphabetical order
  ILoString sort();

  // inserts the given String into this list of Strings
  // already sorted in alphabetical order
  ILoString insert(String s);

  // determines whether this list of Strings is sorted in alphabetical order
  // in a case-insensitive way
  boolean isSorted();

  // determines if this list of Strings comes before the given String
  // alphabetically
  boolean isSortedHelper(String s);

  // produces a list of Strings where the first, third, fifth... elements are from
  // this list and the second, fourth, sixth... elements are from the given list;
  // any "leftover" elements should be left at the end
  ILoString interleave(ILoString that);

  // produces a sorted list of Strings that contains this sorted list of Strings
  // and a given sorted list of Strings, including duplicates
  ILoString merge(ILoString that);

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  ILoString reverse();

  // accumulates the list of Strings in reverse
  ILoString reverseHelper(ILoString accumulator);

  // determines if this list of Strings contains pairs of identical strings
  boolean isDoubledList();

  // determines if the first and second Strings are identical and recursively
  // calls the rest of the list
  boolean isDoubledListHelper(String s);

  // determines if whether list of Strings contains the same words reading the
  // list in either order
  boolean isPalindromeList();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString() {
  }

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  // produces a new list of empty Strings
  public ILoString sort() {
    return this;
  }

  // inserts the given String into this empty list of Strings
  // already sorted in alphabetical order
  public ILoString insert(String s) {
    return new ConsLoString(s, this);
  }

  // determines whether this empty list is sorted in alphabetical order
  public boolean isSorted() {
    return true;
  }

  // determines whether this empty list of Strings comes before the given String
  // alphabetically
  public boolean isSortedHelper(String s) {
    return true;
  }

  // produces a list of Strings of the "leftover" elements from the given list
  public ILoString interleave(ILoString that) {
    return that;
  }

  // produces a sorted list of Strings that contains this empty list of Strings
  // and a given sorted list of Strings, including duplicates
  public ILoString merge(ILoString that) {
    return that.sort();
  }

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  public ILoString reverse() {
    return this;
  }

  // accumulates the list of Strings in reverse
  public ILoString reverseHelper(ILoString accumulator) {
    return accumulator;
  }

  // determines if this empty list of Strings contains pairs of identical strings
  public boolean isDoubledList() {
    return true;
  }

  // determines if the first and second Strings are identical and recursively
  // calls the rest of the list
  public boolean isDoubledListHelper(String s) {
    return false;
  }

  // determines if whether list of Strings contains the same words reading the
  // list in either order
  public boolean isPalindromeList() {
    return true;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   TEMPLATE
   FIELDS:
   ... this.first ...         -- String
   ... this.rest ...          -- ILoString
   
   METHODS
   ... this.combine() ...                    -- String
   ... this.sort() ...                       -- ILoString
   ... this.insert() ...                     -- ILoString
   ... this.isSorted() ...                   -- boolean
   ... this.isSortedHelper(String) ...       -- boolean
   ... this.interleave(ILoString) ...        -- ILoString
   ... this.merge(ILoString) ...             -- ILoString
   ... this.reverse() ...                    -- ILoString 
   ... this.reverseHelper(ILoString) ...     -- ILoString
   ... this.isDoubledList() ...              -- boolean
   ... this.isDoubledListHelper(String) ...  -- boolean
   ... this.isPalindromeList() ...           -- boolean  
   
   METHODS FOR FIELDS
   ... this.first.concat(String) ...               -- String
   ... this.first.compareTo(String) ...            -- int
   ... this.rest.combine() ...                     -- String
   ... this.rest.sort() ...                        -- ILoString
   ... this.rest.insert() ...                      -- ILoString
   ... this.rest.isSorted() ...                    -- boolean
   ... this.rest.isSortedHelper() ...              -- boolean
   ... this.rest.interleave(ILoString) ...         -- ILoString
   ... this.rest.merge(ILoString) ...              -- ILoString
   ... this.rest.reverse() ...                     -- ILoString 
   ... this.rest.reverseHelper(ILoString) ...      -- ILoString 
   ... this.rest.isDoubledList() ...               -- boolean 
   ... this.rest.isDoubledListHelper(String) ...   -- boolean 
   ... this.rest.isPalindromeList() ...            -- boolean 
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }

  // produces a new list of Strings, sorted in alphabetical order,
  // treating all Strings as if they were given in all lower case
  public ILoString sort() {
    return this.rest.sort().insert(this.first);
  }

  // inserts the given String into this list of Strings
  // already sorted in alphabetical order
  public ILoString insert(String s) {
    /* Template:
     * Parameters:
     * ... s ...        -- String
     * 
     * Methods on parameters:
     * ... s.compareTo(String) ...       -- int
     * ... s.toLowerCase() ...           -- String
     */
    if (this.first.toLowerCase().compareTo(s.toLowerCase()) < 0) {
      return new ConsLoString(this.first, this.rest.insert(s));
    }
    else {
      return new ConsLoString(s, this);
    }
  }

  // determines whether this list of Strings is sorted in alphabetical order
  // in a case-insensitive way
  public boolean isSorted() {
    return this.rest.isSortedHelper(this.first);
  }

  // determines whether this list of Strings comes before the given String
  // alphabetically
  public boolean isSortedHelper(String s) {
    /* Template:
     * Parameters:
     * ... s ...        -- String
     * 
     * Methods on parameters:
     * ... s.compareTo(String) ...       -- int
     * ... s.toLowerCase() ...           -- String
     */
    return (this.first.toLowerCase().compareTo(s.toLowerCase()) >= 0)
        && this.rest.isSortedHelper(this.first);
  }

  // Produces a list of Strings where the first, third, fifth... elements are from
  // this list
  // and the second, fourth, sixth... elements are from the given list;
  // any "leftover" elements should be left at the end
  public ILoString interleave(ILoString that) {
    /* Template:
     * Parameters:
     * ... that ...        -- ILoString
     * 
     * Methods on parameters:
     * ... that.interleave(ILoString) ...       -- ILoString
     */
    return new ConsLoString(this.first, that.interleave(rest));
  }

  // produces a sorted list of Strings that contains this sorted list of Strings
  // and a given sorted list of Strings, including duplicates
  public ILoString merge(ILoString that) {
    /* Template:
     * Parameters:
     * ... that ...        -- ILoString
     * 
     * Methods on parameters:
     * ... that.sort() ...       -- ILoString
     */
    return this.interleave(that).sort();
  }

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  public ILoString reverse() {
    return reverseHelper(new MtLoString());
  }

  // accumulates the list of Strings in reverse
  public ILoString reverseHelper(ILoString accumulator) {
    /* Template:
     * Parameters:
     * ... accumulator ...        -- ILoString
     */
    return this.rest.reverseHelper(new ConsLoString(this.first, accumulator));
  }

  // determines if this list of Strings contains pairs of identical strings
  public boolean isDoubledList() {
    return (this.rest.isDoubledListHelper(this.first));
  }

  // determines if the first and second Strings are identical and recursively
  // calls the rest of the list
  public boolean isDoubledListHelper(String s) {
    /* Template:
     * Parameters:
     * ... s ...        -- String
     */
    return (this.first.equals(s) && this.rest.isDoubledList());
  }

  // determines whether this list of Strings contains the same words reading the
  // list in either order
  public boolean isPalindromeList() {
    return (this.combine().equals(this.reverse().combine()));
  }
}

// to represent examples for lists of strings
class ExamplesStrings {
  ExamplesStrings() {
  }

  ILoString mary = new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
      new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))));
  ILoString marySorted = new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("lamb.",
      new ConsLoString("little ", new ConsLoString("Mary ", new MtLoString())))));
  ILoString abcd = new ConsLoString("a",
      new ConsLoString("D", new ConsLoString("B", new ConsLoString("c", new MtLoString()))));
  ILoString abcdSorted = new ConsLoString("a",
      new ConsLoString("B", new ConsLoString("c", new ConsLoString("D", new MtLoString()))));
  ILoString doremi = new ConsLoString("do",
      new ConsLoString("re", new ConsLoString("mi", new MtLoString())));
  ILoString efg = new ConsLoString("e",
      new ConsLoString("f", new ConsLoString("g", new MtLoString())));
  ILoString aabb = new ConsLoString("a",
      new ConsLoString("a", new ConsLoString("b", new ConsLoString("b", new MtLoString()))));
  ILoString abba = new ConsLoString("a",
      new ConsLoString("b", new ConsLoString("b", new ConsLoString("a", new MtLoString()))));
  ILoString aaa = new ConsLoString("a",
      new ConsLoString("a", new ConsLoString("a", new MtLoString())));

  // test the method combine for the list of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.")
        && t.checkExpect(this.aabb.combine(), "aabb");
  }

  // test the method sort for the list of Strings
  boolean testSort(Tester t) {
    return t.checkExpect(this.mary.sort(),
        new ConsLoString("a ", new ConsLoString("had ",
            new ConsLoString("lamb.",
                new ConsLoString("little ", new ConsLoString("Mary ", new MtLoString()))))))
        && t.checkExpect(this.abcd.sort(),
            new ConsLoString("a", new ConsLoString("B",
                new ConsLoString("c", new ConsLoString("D", new MtLoString())))))
        && t.checkExpect(
            new ConsLoString("a",
                new ConsLoString("b", new ConsLoString("c",
                    new ConsLoString("d",
                        new ConsLoString("e", new ConsLoString("d", new ConsLoString("c",
                            new ConsLoString("b", new ConsLoString("a", new MtLoString())))))))))
                                .sort(),
            (new ConsLoString("a",
                new ConsLoString("a",
                    new ConsLoString("b", new ConsLoString("b",
                        new ConsLoString("c", new ConsLoString("c", new ConsLoString("d",
                            new ConsLoString("d", new ConsLoString("e", new MtLoString())))))))))));
  }

  // test the method insert(String) for the list of Strings
  boolean testInsert(Tester t) {
    return t.checkExpect(this.mary.insert("hi "),
        new ConsLoString("hi ",
            new ConsLoString("Mary ", new ConsLoString("had ",
                new ConsLoString("a ",
                    new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))))))
        && t.checkExpect(this.abba.insert("z"),
            new ConsLoString("a", new ConsLoString("b", new ConsLoString("b",
                new ConsLoString("a", new ConsLoString("z", new MtLoString()))))));
  }

  // test the method isSorted for the list of Strings
  boolean testIsSorted(Tester t) {
    return t.checkExpect(this.mary.isSorted(), false)
        && t.checkExpect(this.marySorted.isSorted(), true)
        && t.checkExpect(this.abcd.isSorted(), false)
        && t.checkExpect(this.abcdSorted.isSorted(), true)
        && t.checkExpect(this.aaa.isSorted(), true);
  }

  // test the method isSortedHelper for the list of Strings
  boolean testIsSortedHelper(Tester t) {
    return t.checkExpect(this.mary.isSortedHelper("lamb"), false)
        && t.checkExpect(this.efg.isSortedHelper("a"), true);
  }

  // test the method interleave for the list of Strings
  boolean testInterleave(Tester t) {
    return t.checkExpect(this.mary.interleave(this.marySorted), new ConsLoString("Mary ",
        new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("had ",
            new ConsLoString("a ", new ConsLoString("lamb.", new ConsLoString("little ",
                new ConsLoString("little ",
                    new ConsLoString("lamb.", new ConsLoString("Mary ", new MtLoString())))))))))))
        && t.checkExpect(this.abcd.interleave(this.abcdSorted),
            new ConsLoString("a",
                new ConsLoString("a",
                    new ConsLoString("D", new ConsLoString("B", new ConsLoString("B",
                        new ConsLoString("c",
                            new ConsLoString("c", new ConsLoString("D", new MtLoString())))))))))
        && t.checkExpect(this.mary.interleave(this.abcd),
            new ConsLoString("Mary ", new ConsLoString("a", new ConsLoString("had ",
                new ConsLoString("D", new ConsLoString("a ", new ConsLoString("B",
                    new ConsLoString("little ",
                        new ConsLoString("c", new ConsLoString("lamb.", new MtLoString()))))))))))
        && t.checkExpect(this.abcd.interleave(this.mary),
            new ConsLoString("a",
                new ConsLoString("Mary ",
                    new ConsLoString("D", new ConsLoString("had ",
                        new ConsLoString("B",
                            new ConsLoString("a ", new ConsLoString("c", new ConsLoString("little ",
                                new ConsLoString("lamb.", new MtLoString()))))))))));
  }

  // test the method merge for the list of Strings
  boolean testMerge(Tester t) {
    return t.checkExpect(this.doremi.merge(this.efg),
        new ConsLoString("do",
            new ConsLoString("e",
                new ConsLoString("f",
                    new ConsLoString("g",
                        new ConsLoString("mi", new ConsLoString("re", new MtLoString())))))))
        && t.checkExpect(this.abcdSorted.merge(this.abcdSorted),
            new ConsLoString("a",
                new ConsLoString("a",
                    new ConsLoString("B",
                        new ConsLoString("B", new ConsLoString("c", new ConsLoString("c",
                            new ConsLoString("D", new ConsLoString("D", new MtLoString())))))))));
  }

  // test the method reverse for the list of Strings
  boolean testReverse(Tester t) {
    return t.checkExpect(this.doremi.reverse(),
        new ConsLoString("mi", new ConsLoString("re", new ConsLoString("do", new MtLoString()))))
        && t.checkExpect(this.aabb.reverse(), new ConsLoString("b",
            new ConsLoString("b", new ConsLoString("a", new ConsLoString("a", new MtLoString())))));
  }

  // test the method reverseHelper for the list of Strings
  boolean testReverseHelper(Tester t) {
    return t
        .checkExpect(this.aabb.reverseHelper(aabb),
            new ConsLoString("b",
                new ConsLoString("b",
                    new ConsLoString("a", new ConsLoString("a", new ConsLoString("a",
                        new ConsLoString("a",
                            new ConsLoString("b", new ConsLoString("b", new MtLoString())))))))))
        && t.checkExpect(this.efg.reverseHelper(new ConsLoString("d", new MtLoString())),
            new ConsLoString("g", new ConsLoString("f",
                new ConsLoString("e", new ConsLoString("d", new MtLoString())))));
  }

  // test the method isDoubledList for the list of Strings
  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(this.aabb.isDoubledList(), true)
        && t.checkExpect(this.abba.isDoubledList(), false)
        && t.checkExpect(this.aaa.isDoubledList(), false);
  }

  // test the method isDoubledListHelper for the list of Strings
  boolean testIsDoubledListHelper(Tester t) {
    return t.checkExpect(this.efg.isDoubledListHelper("e"), false)
        && t.checkExpect(this.aabb.isDoubledListHelper("b"), false);
  }

  // test the method isPalindromeList for the list the of Strings
  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(this.aabb.isPalindromeList(), false)
        && t.checkExpect(this.abba.isPalindromeList(), true);
  }
}
