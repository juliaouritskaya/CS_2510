import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import tester.Tester;

// to represent an iterator that iterates through elements in a back-and-forth fashion, starting from the first element
class BackAndForthIterator<T> implements Iterator<T> {
  ArrayList<T> iterator;
  int nextIndex;
  int lastIndex;
  boolean answer;

  // the constructor
  BackAndForthIterator(ArrayList<T> iterator) {
    this.iterator = iterator;
    this.nextIndex = 0;
    this.lastIndex = this.iterator.size() - 1;
    this.answer = true;
  }

  // checks if there is another element
  public boolean hasNext() {
    return this.nextIndex <= this.lastIndex;
  }

  // get the next value in this sequence
  public T next() {
    if (answer) {
      T result = this.iterator.get(this.nextIndex);
      this.nextIndex = this.nextIndex + 1;
      this.answer = false;
      return result;
    }
    else {
      T result = this.iterator.get(this.lastIndex);
      this.lastIndex = this.lastIndex - 1;
      this.answer = true;
      return result;
    }
  }
}

// to represent tests and examples for BackAndForthIterator
class ExamplesBackAndForthIterator {

  ArrayList<String> mtStrings = new ArrayList<String>();
  ArrayList<String> stringsOne = new ArrayList<String>(Arrays.asList("hi"));
  ArrayList<String> stringsFive = new ArrayList<String>(
      Arrays.asList("ken", "david", "kate", "tamara", "suzie"));

  ArrayList<Integer> mtIntegers = new ArrayList<Integer>();
  ArrayList<Integer> integersOne = new ArrayList<Integer>(Arrays.asList(1));
  ArrayList<Integer> integersSix = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));

  Iterator<String> mtIteratorString = new BackAndForthIterator<String>(this.mtStrings);
  Iterator<String> oneIteratorString = new BackAndForthIterator<String>(this.stringsOne);
  Iterator<String> fiveIteratorString = new BackAndForthIterator<String>(this.stringsFive);

  Iterator<Integer> mtIteratorInteger = new BackAndForthIterator<Integer>(this.mtIntegers);
  Iterator<Integer> oneIteratorInteger = new BackAndForthIterator<Integer>(this.integersOne);
  Iterator<Integer> fiveIteratorInteger = new BackAndForthIterator<Integer>(this.integersSix);

  // to test the BackAndForthIterator
  void testBackAndForthIterator(Tester t) {
    // checks that the empty string iterator does not have a next
    t.checkExpect(this.mtIteratorString.hasNext(), false);

    // checks that the string iterator with one element has a next element
    t.checkExpect(this.oneIteratorString.hasNext(), true);
    // returns the next element in the string iterator with one element
    t.checkExpect(this.oneIteratorString.next(), "hi");
    // checks that the string iterator with one element does not have a next element
    t.checkExpect(this.oneIteratorString.hasNext(), false);

    // checks that the string iterator with five elements has next elements up to
    // the last one
    t.checkExpect(this.fiveIteratorString.hasNext(), true);
    // returns the next element in the string iterator with five elements
    t.checkExpect(this.fiveIteratorString.next(), "ken");
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorString.next(), "suzie");
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorString.next(), "david");
    t.checkExpect(this.fiveIteratorString.hasNext(), true);
    t.checkExpect(this.fiveIteratorString.next(), "tamara");
    t.checkExpect(this.fiveIteratorString.hasNext(), true);
    t.checkExpect(this.fiveIteratorString.next(), "kate");
    t.checkExpect(this.fiveIteratorString.hasNext(), false);

    // checks that the empty integer iterator does not have a next
    t.checkExpect(this.mtIteratorInteger.hasNext(), false);

    // checks that the integer iterator with one element has a next element
    t.checkExpect(this.oneIteratorInteger.hasNext(), true);
    // returns the next element in the integer iterator with one element
    t.checkExpect(this.oneIteratorInteger.next(), 1);
    // checks that the integer iterator with one element does not have a next
    // element
    t.checkExpect(this.oneIteratorInteger.hasNext(), false);

    // checks that the integer iterator with five elements has next elements up to
    // the last one
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    // returns the next element in the integer iterator with five elements
    t.checkExpect(this.fiveIteratorInteger.next(), 1);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorInteger.next(), 6);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorInteger.next(), 2);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorInteger.next(), 5);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorInteger.next(), 3);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), true);
    t.checkExpect(this.fiveIteratorInteger.next(), 4);
    t.checkExpect(this.fiveIteratorInteger.hasNext(), false);
  }
}