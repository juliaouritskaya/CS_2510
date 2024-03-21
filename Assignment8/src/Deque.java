import java.util.function.Predicate;
import tester.Tester;

// to represent a deque 
class Deque<T> {
  Sentinel<T> header;

  // main constructor
  Deque() {
    header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> data) {
    this.header = data;
  }

  // counts the number of nodes in a list Deque, not including the header node
  int size() {
    return this.header.size();
  }

  // consumes a value of type T and inserts it at the front of this list
  void addAtHead(T value) {
    this.header.addAtHead(value);
  }

  // consumes a value of type T and inserts it at the tail of this list
  void addAtTail(T value) {
    this.header.addAtTail(value);
  }

  // removes the first node from this Deque
  T removeFromHead() {
    return this.header.removeFromHead();
  }

  // removes the last node from this Deque
  T removeFromTail() {
    return this.header.removeFromTail();
  }

  // produces the first node in this Deque for which the given predicate returns
  // true
  ANode<T> find(Predicate<T> predicate) {
    return this.header.find(predicate);
  }

  // removes the given node from this Deque
  void removeNode(ANode<T> node) {
    node.removeFromHelper();
  }
}

class FindNode<T> implements Predicate<T> {
  T data;

  // the constructor
  FindNode(T data) {
    this.data = data;
  }

  // to checks if this node is the same as the given node
  public boolean test(T other) {
    return data.equals(other) || data == other;
  }
}

// common superclass for the Sentinel and Node classes 
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts the number of nodes in a list Deque, not including the header node
  abstract int sizeHelper();

  // helps to remove a Node
  abstract T removeFromHelper();

  // helps to find a node using a predicate
  abstract ANode<T> findHelper(Predicate<T> predicate);
}

// to represent a sentinel node that is a part of the header 
class Sentinel<T> extends ANode<T> {
  // the constructor
  Sentinel() {
    next = this;
    prev = this;
  }

  // counts the number of sentinels in a list Deque, not including the header node
  int size() {
    return this.next.sizeHelper();
  }

  // returns 0 in order to not include the header node
  int sizeHelper() {
    return 0;
  }

  // consumes a value of type T and inserts it at the front of the list
  public void addAtHead(T value) {
    new Node<T>(value, this.next, this);
  }

  // consumes a value of type T and inserts it at the tail of this list
  public void addAtTail(T value) {
    new Node<T>(value, this, this.prev);
  }

  // removes the first node from this Sentinel
  T removeFromHead() {
    return this.next.removeFromHelper();
  }

  // removes the last node from this Sentinel
  public T removeFromTail() {
    return this.prev.removeFromHelper();
  }

  // helps to remove a Node from this Sentinel
  T removeFromHelper() {
    throw new RuntimeException("cannot remove from an empty list");
  }

  // produces the first node in this Sentinel for which the given predicate
  // returns true
  public ANode<T> find(Predicate<T> predicate) {
    return this.next.findHelper(predicate);
  }

  // helper for find that returns the Sentinel itself
  ANode<T> findHelper(Predicate<T> predicate) {
    return this;
  }
}

// to represent a data node that has two links 
class Node<T> extends ANode<T> {
  T data;

  // main constructor
  Node(T data) {
    this.data = data;
    next = null;
    prev = null;
  }

  // convenience constructor
  Node(T data, ANode<T> node1, ANode<T> node2) {
    this.data = data;
    next = node1;
    prev = node2;

    if (node1 == null || node2 == null) {
      throw new IllegalArgumentException("given node is null");
    }
    node1.prev = this;
    node2.next = this;
  }

  // counts the number of nodes in a list Deque, not including the header node
  int sizeHelper() {
    return 1 + this.next.sizeHelper();
  }

  // helps to remove a Node
  T removeFromHelper() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.data;
  }

  // helper for find that returns the first node for which the predicate is true
  public ANode<T> findHelper(Predicate<T> predicate) {
    if (predicate.test(this.data)) {
      return this;
    }
    else {
      return this.next.findHelper(predicate);
    }
  }
}

// to represent examples and tests for Deque
class ExamplesDeque {
  ExamplesDeque() {
  }

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<Integer> deque3;
  Deque<Integer> deque4;

  Sentinel<String> sentinelString;
  Sentinel<Integer> sentinelNumber;
  Sentinel<Integer> sentinelEmpty;

  Node<String> nodeA;
  Node<String> nodeB;
  Node<String> nodeC;
  Node<String> nodeD;
  Node<Integer> node1;
  Node<Integer> node2;
  Node<Integer> node3;
  Node<Integer> node4;
  Node<Integer> node5;

  // sets up the initial conditions by intializing
  void initialConditions() {
    this.sentinelString = new Sentinel<String>();
    this.sentinelNumber = new Sentinel<Integer>();
    this.sentinelEmpty = new Sentinel<Integer>();

    this.nodeA = new Node<String>("abc", this.sentinelString, this.sentinelString);
    this.nodeB = new Node<String>("bcd", this.sentinelString, this.nodeA);
    this.nodeC = new Node<String>("cde", this.sentinelString, this.nodeB);
    this.nodeD = new Node<String>("def", this.sentinelString, this.nodeC);

    this.node5 = new Node<Integer>(5, this.sentinelNumber, this.sentinelNumber);
    this.node4 = new Node<Integer>(4, this.sentinelNumber, this.node5);
    this.node1 = new Node<Integer>(1, this.sentinelNumber, this.node4);
    this.node2 = new Node<Integer>(2, this.sentinelNumber, this.node1);
    this.node3 = new Node<Integer>(3, this.sentinelNumber, this.node2);

    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>(this.sentinelString);
    this.deque3 = new Deque<Integer>();
    this.deque4 = new Deque<Integer>(this.sentinelNumber);
  }

  // to test the method size on the class Deque and Sentinel
  void testSize(Tester t) {
    initialConditions();

    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 0);
    t.checkExpect(this.sentinelNumber.size(), 5);
    t.checkExpect(this.sentinelString.size(), 4);
  }

  // to test the method sizeHelper on the class Sentinel and Node
  void testSizeHelper(Tester t) {
    initialConditions();

    t.checkExpect(this.sentinelNumber.sizeHelper(), 0);
    t.checkExpect(this.sentinelString.sizeHelper(), 0);
    t.checkExpect(this.nodeA.sizeHelper(), 4);
    t.checkExpect(this.node3.sizeHelper(), 1);
    t.checkExpect(this.nodeD.sizeHelper(), 1);
  }

  // to test the method addAtHead on the class Deque
  void testAddAtHead(Tester t) {
    initialConditions();

    t.checkExpect(this.deque3, this.deque3);
    this.deque3.addAtHead(3);
    this.deque3.addAtHead(2);
    this.deque3.addAtHead(1);
    this.deque3.addAtHead(4);
    this.deque3.addAtHead(5);
    t.checkExpect(this.deque3, this.deque4);
  }

  // to test the method addAtTail on the class Deque
  void testAddAtTail(Tester t) {
    initialConditions();

    t.checkExpect(this.deque2, this.deque2);
    this.deque1.addAtTail("abc");
    this.deque1.addAtTail("bcd");
    this.deque1.addAtTail("cde");
    this.deque1.addAtTail("def");
    t.checkExpect(this.deque1, this.deque2);
  }

  // to test the method removeFromHead on the class Deque and Sentinel
  void testRemoveFromHead(Tester t) {
    initialConditions();

    t.checkException(new RuntimeException("cannot remove from an empty list"), this.deque1,
        "removeFromHead");
    t.checkException(new RuntimeException("cannot remove from an empty list"), this.sentinelEmpty,
        "removeFromHead");

    this.deque2.removeFromHead();
    t.checkExpect(this.deque2.header.next, this.nodeB);
    this.deque4.removeFromHead();
    t.checkExpect(this.deque4.header.next, this.node4);
  }

  // to test the method removeFromTail on the class Deque and Sentinel
  void testRemoveFromTail(Tester t) {
    initialConditions();

    t.checkException(new RuntimeException("cannot remove from an empty list"), this.deque3,
        "removeFromTail");
    t.checkException(new RuntimeException("cannot remove from an empty list"), this.sentinelEmpty,
        "removeFromTail");

    this.deque2.removeFromTail();
    t.checkExpect(this.deque2.header.prev, this.nodeC);
    this.deque4.removeFromTail();
    t.checkExpect(this.deque2.header.next, this.nodeA);
  }

  // to test the method removeFromHelper on the abstract class ANode<T>
  void testRemoveHelper(Tester t) {
    initialConditions();

    t.checkException(new RuntimeException("cannot remove from an empty list"), this.sentinelEmpty,
        "removeFromHelper");

    this.nodeB.removeFromHelper();
    t.checkExpect(this.nodeA.next, this.nodeC);
  }

  // to test the method find on the class Deque
  void testFind(Tester t) {
    initialConditions();

    t.checkExpect(this.deque2.find(new FindNode<String>("abc")), this.sentinelString.next);
    t.checkExpect(this.deque4.find(new FindNode<Integer>(3)), this.node2.next);
    t.checkExpect(this.deque3.find(new FindNode<Integer>(7)), this.sentinelEmpty);
  }

  // to test the method findHelper on the abstract class ANode<T>
  void testFindHelper(Tester t) {
    initialConditions();

    t.checkExpect(this.sentinelNumber.find(new FindNode<Integer>(2)), this.node2);
    t.checkExpect(this.nodeB.findHelper(new FindNode<String>("bcd")), this.nodeB);
  }

  // to test the method removeNode on the class Deque
  void testRemoveNode(Tester t) {
    initialConditions();

    this.deque2.removeNode(this.nodeA);
    this.deque2.removeNode(this.nodeB);
    this.deque2.removeNode(this.nodeC);
    this.deque2.removeNode(this.nodeD);
    t.checkExpect(this.deque2, this.deque1);
  }
}
