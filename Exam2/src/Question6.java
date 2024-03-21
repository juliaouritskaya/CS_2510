import java.util.ArrayList;
import java.util.Arrays;

import tester.Tester;

// to represent a duplicate utils class 
class DuplicateUtils {
  // removes duplicates from the given list
  void removeDuplicates(ArrayList<String> list) {
    int i, j;

    for (i = 0; i < list.size(); i++) {
      for (j = i + 1; j < list.size(); j++) {
        if (list.get(j).equals(list.get(i))) {
          list.remove(j);
        }
      }
    }
  }

  // correctly fulfills the purpose of the removeDuplicates method
  void removeDuplicatesCorrect(ArrayList<String> list) {
    int i, j;

    for (i = 0; i < list.size(); i++) {
      for (j = i + 1; j < list.size(); j++) {
        if (list.get(j).equals(list.get(i))) {
          list.remove(j);
          j = j - 1;
        }
      }
    }
  }
}

// to represent examples and tests for duplicate utils
class ExamplesDuplicates {
  ExamplesDuplicates() {
  }

  DuplicateUtils dup;
  ArrayList<String> string;
  ArrayList<String> expected;
  ArrayList<String> string2;

  void initialConditions() {
    dup = new DuplicateUtils();
    string = new ArrayList<String>(Arrays.asList("a", "a", "a", "b", "c", "c", "d"));
    expected = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
    string2 = new ArrayList<String>(Arrays.asList("a", "a", "a", "b", "a", "c", "c", "d", "b"));
  }

  void testRemoveDuplicates(Tester t) {
    initialConditions();

    t.checkExpect(this.string.size(), 7);
    this.dup.removeDuplicates(this.string);
    t.checkExpect(this.string.size(), 4);
    t.checkExpect(this.string, this.expected);
  }

  void testRemoveDuplicatesCorrect(Tester t) {
    initialConditions();

    // checks if removesDuplicatesCorrect works correctly on an array list that
    // include 3 duplicates in a row

    // checks the initial size of the list
    t.checkExpect(this.string.size(), 7);
    // removes duplicates from the list
    this.dup.removeDuplicatesCorrect(this.string);
    // checks the size of the list after removing duplicates
    t.checkExpect(this.string.size(), 4);
    // checks that the list after removing duplicates is equal to the expected list
    t.checkExpect(this.string, this.expected);

    // checks if removesDuplicatesCorrect works correctly on an array list that
    // include 3 duplicates in a row as well as duplicates that occur later on in
    // the list (i.e., not consecutive)

    // checks the initial size of the list
    t.checkExpect(this.string2.size(), 9);
    // removes duplicates from the list
    this.dup.removeDuplicatesCorrect(this.string2);
    // checks the size of the list after removing duplicates
    t.checkExpect(this.string2.size(), 4);
    // checks that the list after removing duplicates is equal to the expected list
    t.checkExpect(this.string2, this.expected);
  }
}

/*
 * The original method removeDuplicates does not work correctly because it 
 * does not decrease the variable j when it finds an instance of two duplicate strings. 
 * Therefore, when there are more than two consecutive duplicate strings in the array list, the method 
 * removes only the first instance of a duplicate, leaving the rest of the duplicates in the list. 
 * In order to fix this issue, after we do list.remove(i), we have to subtract 1 from the j variable. 
 * This would correctly implement the method, which can be seen in the second example. 
 * */
