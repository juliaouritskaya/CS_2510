import java.util.Comparator;

import tester.Tester;

// to represent a book
class Book {
  String title;
  String author;
  int price;

  // the constructor
  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

// to represent a generic BST of type T
abstract class ABST<T> {
  Comparator<T> order;

  // the constructor
  ABST(Comparator<T> order) {
    this.order = order;
  }

  // takes an item and produces a new BST with the given item inserted in the
  // correct place (if the value is a duplicate according to the tree order,
  // insert it into the right-side subtree)
  abstract ABST<T> insert(T item);

  // takes an item and returns whether that item is present in the binary search
  // tree
  boolean present(T item) {
    return false;
  }

  // returns the leftmost item contained in this tree
  abstract T getLeftmost();

  // finds the minimum value in the tree
  abstract T getLeftmostHelper(T item);

  // returns the tree containing all but the leftmost item of this tree
  abstract ABST<T> getRight();

  // determines whether this binary search tree is the same as the given one
  abstract boolean sameTree(ABST<T> tree);

  // checks if this leaf is the same as the given one
  boolean sameLeaf(Leaf<T> leaf) {
    return false;
  }

  // checks if this node is the same as the given one
  boolean sameNode(Node<T> node) {
    return false;
  }

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  abstract boolean sameData(ABST<T> tree);

  // recursively checks if this tree contains the same data as the given tree
  boolean sameDataHelper(ABST<T> tree) {
    return true;
  }

  // checks if this tree has the same data as the given tree
  boolean inTree(T tree) {
    return false;
  }

  // produces a list of items in the tree in the sorted order
  abstract IList<T> buildList();

  // produces a list with the leftmost value first, and the right of the tree as
  // the rest
  abstract IList<T> buildListHelper(IList<T> list);
}

// to represent a leaf of a generic BST
class Leaf<T> extends ABST<T> {

  // the constructor
  Leaf(Comparator<T> order) {
    super(order);
  }

  // takes an item and produces a new BST with the given item inserted in the
  // correct place (if the value is a duplicate according to the tree order,
  // insert it into the right-side subtree)
  ABST<T> insert(T item) {
    return new Node<T>(this.order, item, this, this);
  }

  // returns the leftmost item contained in this tree
  T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // finds the minimum value in the tree
  T getLeftmostHelper(T item) {
    return item;
  }

  // returns the tree containing all but the leftmost item of this tree
  ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  // determines whether this binary search tree is the same as the given one
  boolean sameTree(ABST<T> tree) {
    return tree.sameLeaf(this);
  }

  // checks if this leaf is the same as the given one
  boolean sameLeaf(Leaf<T> leaf) {
    return this.order.equals(leaf.order);
  }

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  boolean sameData(ABST<T> tree) {
    if (tree.sameDataHelper(this)) {
      return tree.sameLeaf(this);
    }
    else {
      return false;
    }
  }

  // produces a list of items in the tree in the sorted order
  IList<T> buildList() {
    return new MtList<T>();
  }

  // produces a list with the leftmost value first, and the right of the tree as
  // the rest
  IList<T> buildListHelper(IList<T> list) {
    return list;
  }
}

// to represent a node of a generic BST
class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  // main constructor
  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  // convenience constructor
  Node(Comparator<T> order, T data) {
    super(order);
    this.data = data;
    this.left = new Leaf<T>(order);
    this.right = new Leaf<T>(order);
  }

  // takes an item and produces a new BST with the given item inserted in the
  // correct place (if the value is a duplicate according to the tree order,
  // insert it into the right-side subtree)
  ABST<T> insert(T item) {
    if (this.order.compare(item, this.data) <= 0) {
      return new Node<T>(this.order, this.data, this.left.insert(item), this.right);
    }
    else {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(item));
    }
  }

  // takes an item and returns whether that item is present in the binary search
  // tree
  boolean present(T item) {
    return this.order.compare(item, this.data) == 0 || this.left.present(item)
        || this.right.present(item);
  }

  // returns the leftmost item contained in this tree
  T getLeftmost() {
    return this.left.getLeftmostHelper(this.data);
  }

  // finds the minimum value in the tree
  T getLeftmostHelper(T item) {
    if (this.order.compare(item, this.data) > 0) {
      return this.left.getLeftmostHelper(this.data);
    }
    else {
      return item;
    }
  }

  // returns the tree containing all but the leftmost item of this tree
  ABST<T> getRight() {
    if (this.order.compare(this.getLeftmost(), this.data) == 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  // determines whether this binary search tree is the same as the given one
  boolean sameTree(ABST<T> tree) {
    return tree.sameNode(this);
  }

  // checks if this node is the same as the given one
  boolean sameNode(Node<T> node) {
    return this.order.compare(node.data, this.data) == 0 && this.left.sameTree(node.left)
        && this.right.sameTree(node.right);
  }

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  boolean sameData(ABST<T> tree) {
    return tree.sameDataHelper(this) && this.sameDataHelper(tree);
  }

  // recursively checks if this tree contains the same data as the given tree
  boolean sameDataHelper(ABST<T> tree) {
    return tree.inTree(this.data) && this.left.sameDataHelper(tree)
        && this.right.sameDataHelper(tree);
  }

  // checks if this tree has the same data as the given tree
  boolean inTree(T tree) {
    if (this.order.compare(this.data, tree) == 0) {
      return true;
    }
    else {
      return this.left.inTree(tree) || this.right.inTree(tree);
    }
  }

  // produces a list of items in the tree in the sorted order
  IList<T> buildList() {
    return this.buildListHelper(new MtList<T>());
  }

  // produces a list with the leftmost value first, and the right of the tree as
  // the rest
  IList<T> buildListHelper(IList<T> list) {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildListHelper(list));
  }
}

// to compare two books by their title 
class BooksByTitle implements Comparator<Book> {

  // compares two books by their title alphabetically
  public int compare(Book book1, Book book2) {
    return book1.title.compareTo(book2.title);
  }
}

// to compare two books by their author 
class BooksByAuthor implements Comparator<Book> {

  // compares two books by their author alphabetically
  public int compare(Book book1, Book book2) {
    return book1.author.compareTo(book2.author);
  }
}

// to compare two books by their price 
class BooksByPrice implements Comparator<Book> {

  // compares two books by their price by increasing value
  public int compare(Book book1, Book book2) {
    return book1.price - book2.price;
  }
}

// to represent a generic list
interface IList<T> {

}

// to represent a generic empty list
class MtList<T> implements IList<T> {
  MtList() {
  }
}

// to represent a generic non-empty list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  // the constructor
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

// to represents examples and tests for ABST<T>
class ExamplesABST {
  ExamplesABST() {
  }

  Book itEndsWithUs = new Book("It Ends with Us", "Coleen Hoover", 15);
  Book littleWomen = new Book("Little Women", "Louisa May Alcott", 18);
  Book theLawsOfHumanNature = new Book("The Laws of Human Nature", "Robert Greene", 42);

  Book aliceInWonderland = new Book("Alice in Wonderland", "Lewis Carroll", 10);
  Book yearOfTheFrench = new Book("Year of the French", "Thomas Flanagan", 20);

  Comparator<Book> compareByTitle = new BooksByTitle();
  Comparator<Book> compareByAuthor = new BooksByAuthor();
  Comparator<Book> compareByPrice = new BooksByPrice();

  ABST<Book> leaf1 = new Leaf<Book>(this.compareByTitle);
  ABST<Book> leaf2 = new Leaf<Book>(this.compareByAuthor);
  ABST<Book> leaf3 = new Leaf<Book>(this.compareByPrice);

  // examples for compareByTitle
  ABST<Book> titleNode1 = new Node<Book>(this.compareByTitle, this.aliceInWonderland);
  ABST<Book> titleNode2 = new Node<Book>(this.compareByTitle, this.itEndsWithUs);
  ABST<Book> titleNode3 = new Node<Book>(this.compareByTitle, this.littleWomen);
  ABST<Book> titleNode4 = new Node<Book>(this.compareByTitle, this.theLawsOfHumanNature);
  ABST<Book> titleNode5 = new Node<Book>(this.compareByTitle, this.yearOfTheFrench);

  ABST<Book> titleNodeLeft = new Node<Book>(
      this.compareByTitle, this.theLawsOfHumanNature, new Node<Book>(this.compareByTitle,
          this.littleWomen, new Node<Book>(this.compareByTitle, this.itEndsWithUs), this.leaf1),
      this.leaf1);
  ABST<Book> titleNodeMiddle = new Node<Book>(this.compareByTitle, this.littleWomen,
      this.titleNode2, this.titleNode4);

  // examples for comparebyAuthor
  ABST<Book> authorNode1 = new Node<Book>(this.compareByAuthor, this.aliceInWonderland);
  ABST<Book> authorNode2 = new Node<Book>(this.compareByAuthor, this.itEndsWithUs);
  ABST<Book> authorNode3 = new Node<Book>(this.compareByAuthor, this.littleWomen);
  ABST<Book> authorNode4 = new Node<Book>(this.compareByAuthor, this.theLawsOfHumanNature);
  ABST<Book> authorNode5 = new Node<Book>(this.compareByAuthor, this.yearOfTheFrench);

  ABST<Book> authorNodeLeft = new Node<Book>(
      this.compareByAuthor, this.theLawsOfHumanNature, new Node<Book>(this.compareByAuthor,
          this.littleWomen, new Node<Book>(this.compareByAuthor, this.itEndsWithUs), this.leaf2),
      this.leaf2);
  ABST<Book> authorNodeMiddle = new Node<Book>(this.compareByAuthor, this.littleWomen,
      this.authorNode2, this.authorNode4);

  // examples for comparebyPrice
  ABST<Book> priceNode1 = new Node<Book>(this.compareByPrice, this.aliceInWonderland);
  ABST<Book> priceNode2 = new Node<Book>(this.compareByPrice, this.itEndsWithUs);
  ABST<Book> priceNode3 = new Node<Book>(this.compareByPrice, this.littleWomen);
  ABST<Book> priceNode4 = new Node<Book>(this.compareByPrice, this.theLawsOfHumanNature);
  ABST<Book> priceNode5 = new Node<Book>(this.compareByPrice, this.yearOfTheFrench);

  ABST<Book> priceNodeLeft = new Node<Book>(
      this.compareByPrice, this.theLawsOfHumanNature, new Node<Book>(this.compareByPrice,
          this.littleWomen, new Node<Book>(this.compareByPrice, this.itEndsWithUs), this.leaf3),
      this.leaf3);
  ABST<Book> priceNodeMiddle = new Node<Book>(this.compareByPrice, this.littleWomen,
      this.priceNode2, this.priceNode4);

  ABST<Book> priceNodeMiddleNew = new Node<Book>(this.compareByPrice, this.littleWomen,
      this.priceNode1, this.priceNode4);

  // examples of lists of books
  IList<Book> listBooksByTitle = new ConsList<Book>(this.itEndsWithUs, new ConsList<Book>(
      this.littleWomen, new ConsList<Book>(this.theLawsOfHumanNature, new MtList<Book>())));
  IList<Book> listBooksbyAuthor = new ConsList<Book>(this.itEndsWithUs, new ConsList<Book>(
      this.littleWomen, new ConsList<Book>(this.theLawsOfHumanNature, new MtList<Book>())));
  IList<Book> listBooksByPrice = new ConsList<Book>(this.aliceInWonderland, new ConsList<Book>(
      this.littleWomen, new ConsList<Book>(this.theLawsOfHumanNature, new MtList<Book>())));

  // to test the class BooksByTitle implementing Comparator<T> interface
  boolean testBooksByTitle(Tester t) {
    return t.checkExpect(this.compareByTitle.compare(this.itEndsWithUs, this.littleWomen) < 0, true)
        && t.checkExpect(
            this.compareByTitle.compare(this.littleWomen, this.theLawsOfHumanNature) < 0, true)
        && t.checkExpect(
            this.compareByTitle.compare(this.theLawsOfHumanNature, this.itEndsWithUs) < 0, false);
  }

  // to test the class BooksByAuthor implementing Comparator<T> interface
  boolean testBooksByAuthor(Tester t) {
    return t.checkExpect(this.compareByAuthor.compare(this.itEndsWithUs, this.littleWomen) < 0,
        true)
        && t.checkExpect(
            this.compareByAuthor.compare(this.littleWomen, this.theLawsOfHumanNature) < 0, true)
        && t.checkExpect(
            this.compareByAuthor.compare(this.theLawsOfHumanNature, this.itEndsWithUs) < 0, false);
  }

  // to test the class BooksByPrice implementing Comparator<T> interface
  boolean testBooksByPrice(Tester t) {
    return t.checkExpect(this.compareByPrice.compare(this.itEndsWithUs, this.littleWomen) < 0, true)
        && t.checkExpect(
            this.compareByPrice.compare(this.littleWomen, this.theLawsOfHumanNature) < 0, true)
        && t.checkExpect(
            this.compareByPrice.compare(this.theLawsOfHumanNature, this.itEndsWithUs) < 0, false);
  }

  // to test the method insert on the class BooksByTitle
  boolean testInsertByTitle(Tester t) {
    return t.checkExpect(this.leaf1.insert(this.itEndsWithUs), this.titleNode2)

        && t.checkExpect(this.titleNodeLeft.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByTitle, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByTitle, this.littleWomen,
                    new Node<Book>(this.compareByTitle, this.itEndsWithUs, this.titleNode1,
                        this.leaf1),
                    this.leaf1),
                this.leaf1))

        && t.checkExpect(this.titleNodeLeft.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByTitle, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByTitle, this.littleWomen, this.titleNode2, this.leaf1),
                this.titleNode5))

        && t.checkExpect(this.titleNodeMiddle.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByTitle, this.littleWomen,
                new Node<Book>(this.compareByTitle, this.itEndsWithUs, this.titleNode1, this.leaf1),
                this.titleNode4))

        && t.checkExpect(this.titleNodeMiddle.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByTitle, this.littleWomen, this.titleNode2, new Node<Book>(
                this.compareByTitle, this.theLawsOfHumanNature, this.leaf1, this.titleNode5)));
  }

  // to test the method insert on the class BooksByAuthor
  boolean testInsertByAuthor(Tester t) {
    return t.checkExpect(this.leaf2.insert(this.aliceInWonderland), this.authorNode1)

        && t.checkExpect(this.authorNodeLeft.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByAuthor, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByAuthor, this.littleWomen,
                    new Node<Book>(this.compareByAuthor, this.itEndsWithUs, this.leaf2,
                        this.authorNode1),
                    this.leaf2),
                this.leaf2))

        && t.checkExpect(this.authorNodeLeft.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByAuthor, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByAuthor, this.littleWomen,
                    new Node<Book>(this.compareByAuthor, this.itEndsWithUs), this.leaf2),
                this.authorNode5))

        && t.checkExpect(this.authorNodeMiddle.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByAuthor, this.littleWomen,
                new Node<Book>(this.compareByAuthor, this.itEndsWithUs, this.leaf2,
                    this.authorNode1),
                new Node<Book>(this.compareByAuthor, this.theLawsOfHumanNature)))

        && t.checkExpect(this.authorNodeMiddle.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByAuthor, this.littleWomen,
                new Node<Book>(this.compareByAuthor, this.itEndsWithUs),
                new Node<Book>(this.compareByAuthor, this.theLawsOfHumanNature, this.leaf2,
                    this.authorNode5)));
  }

  // to test the method insert on the class BooksByPrice
  boolean testInsertByPrice(Tester t) {
    return t.checkExpect(this.leaf3.insert(this.aliceInWonderland), this.priceNode1)

        && t.checkExpect(this.priceNodeLeft.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByPrice, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByPrice, this.littleWomen,
                    new Node<Book>(this.compareByPrice, this.itEndsWithUs, this.priceNode1,
                        this.leaf3),
                    this.leaf3),
                this.leaf3))

        && t.checkExpect(this.priceNodeLeft.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByPrice, this.theLawsOfHumanNature,
                new Node<Book>(this.compareByPrice, this.littleWomen,
                    new Node<Book>(this.compareByPrice, this.itEndsWithUs), this.priceNode5),
                this.leaf3))

        && t.checkExpect(this.priceNodeMiddle.insert(this.aliceInWonderland),
            new Node<Book>(this.compareByPrice, this.littleWomen,
                new Node<Book>(this.compareByPrice, this.itEndsWithUs, this.priceNode1, this.leaf3),
                this.priceNode4))

        && t.checkExpect(this.priceNodeMiddle.insert(this.yearOfTheFrench),
            new Node<Book>(this.compareByPrice, this.littleWomen, this.priceNode2, new Node<Book>(
                this.compareByPrice, this.theLawsOfHumanNature, this.priceNode5, this.leaf3)));
  }

  // to test the method present on the interface Comparator<T>
  boolean testPresent(Tester t) {
    return t.checkExpect(this.leaf1.present(this.aliceInWonderland), false)
        && t.checkExpect(this.leaf2.present(this.aliceInWonderland), false)
        && t.checkExpect(this.leaf3.present(this.aliceInWonderland), false)
        && t.checkExpect(this.titleNodeMiddle.present(this.aliceInWonderland), false)
        && t.checkExpect(this.titleNodeMiddle.present(this.itEndsWithUs), true)
        && t.checkExpect(this.authorNodeMiddle.present(this.yearOfTheFrench), false)
        && t.checkExpect(this.authorNodeMiddle.present(this.theLawsOfHumanNature), true)
        && t.checkExpect(this.priceNodeMiddle.present(this.aliceInWonderland), false)
        && t.checkExpect(this.priceNodeMiddle.present(this.littleWomen), true);
  }

  // to test the method getLeftMost on the interface Comparator<T>
  boolean testGetLeftMost(Tester t) {
    return t.checkExpect(this.titleNodeLeft.getLeftmost(), this.itEndsWithUs)
        && t.checkExpect(this.authorNodeMiddle.getLeftmost(), this.itEndsWithUs)
        && t.checkExpect(this.priceNode4.getLeftmost(), this.theLawsOfHumanNature)
        && t.checkException(new RuntimeException("No leftmost item of an empty tree"), this.leaf1,
            "getLeftmost");
  }

  // to test the method getLeftMostHelper on the interface Comparator<T>
  boolean testGetLeftMostHelper(Tester t) {
    return t.checkExpect(this.leaf1.getLeftmostHelper(this.itEndsWithUs), this.itEndsWithUs)
        && t.checkExpect(this.titleNodeLeft.getLeftmostHelper(this.aliceInWonderland),
            this.aliceInWonderland)
        && t.checkExpect(this.authorNodeMiddle.getLeftmostHelper(this.yearOfTheFrench),
            this.itEndsWithUs);
  }

  // to test the method getRight on the interface Comparator<T>
  boolean testGetRight(Tester t) {
    return t.checkExpect(this.titleNodeLeft.getRight(),
        new Node<Book>(this.compareByTitle, this.theLawsOfHumanNature,
            new Node<Book>(this.compareByTitle, this.littleWomen, this.leaf1, this.leaf1),
            this.leaf1))
        && t.checkException(new RuntimeException("No right of an empty tree"), this.leaf1,
            "getRight")
        && t.checkExpect(this.authorNode1.getRight(), this.leaf2);
  }

  // to test the method sameTree on the interface Comparator<T>
  boolean testSameTree(Tester t) {
    return t.checkExpect(this.authorNode1.sameTree(this.authorNode1), true)
        && t.checkExpect(this.authorNode1.sameTree(this.authorNode2), false)
        && t.checkExpect(this.priceNodeMiddle.sameTree(this.priceNodeMiddle), true)
        && t.checkExpect(this.priceNodeMiddle.sameTree(this.priceNodeMiddleNew), false);
  }

  // to test the method sameLeaf on the interface Leaf<T>
  boolean testSameLeaf(Tester t) {
    return t.checkExpect(this.leaf1.sameLeaf(new Leaf<Book>(this.compareByTitle)), true)
        && t.checkExpect(this.leaf1.sameLeaf(new Leaf<Book>(this.compareByPrice)), false);
  }

  // to test the method sameNode on the interface Node<T>
  boolean testSameNode(Tester t) {
    return t.checkExpect(
        this.authorNode1.sameNode(new Node<Book>(this.compareByAuthor, this.aliceInWonderland)),
        true)
        && t.checkExpect(this.priceNodeLeft.sameNode(new Node<Book>(this.compareByPrice,
            this.littleWomen, this.priceNode2, this.priceNode4)), false);
  }

  // to test the method sameData on the interface Comparator<T>
  boolean testSameData(Tester t) {
    return t.checkExpect(this.authorNodeLeft.sameData(this.authorNodeMiddle), true)
        && t.checkExpect(this.priceNodeLeft.sameData(this.priceNodeLeft), true)
        && t.checkExpect(
            this.titleNodeMiddle.sameData(new Node<Book>(this.compareByTitle, this.littleWomen,
                new Node<Book>(this.compareByTitle, this.itEndsWithUs, this.titleNode1, this.leaf1),
                this.titleNode4)),
            false);
  }

  // to test the method sameDataHelper on the interface Comparator<T>
  boolean testSameDataHelper(Tester t) {
    return t.checkExpect(this.leaf1.sameDataHelper(this.leaf1), true)
        && t.checkExpect(this.authorNode1.sameDataHelper(this.authorNode2), false)
        && t.checkExpect(this.priceNodeMiddle.sameDataHelper(this.priceNodeLeft), true)
        && t.checkExpect(this.priceNodeMiddle.sameDataHelper(this.priceNodeMiddleNew), false);
  }

  // to test the method inTree on the interface Comparator<T>
  boolean testInTree(Tester t) {
    return t.checkExpect(this.leaf2.inTree(this.itEndsWithUs), false)
        && t.checkExpect(this.titleNode5.inTree(this.yearOfTheFrench), true)
        && t.checkExpect(this.authorNodeLeft.inTree(this.littleWomen), true);
  }

  // to test the method buildList on the interface Comparator<T>
  boolean testBuildList(Tester t) {
    return t.checkExpect(this.titleNodeLeft.buildList(), this.listBooksByTitle)
        && t.checkExpect(this.authorNodeMiddle.buildList(), this.listBooksbyAuthor)
        && t.checkExpect(this.priceNodeMiddleNew.buildList(), this.listBooksByPrice)
        && t.checkExpect(this.authorNode1.buildList(),
            new ConsList<Book>(this.aliceInWonderland, new MtList<Book>()))
        && t.checkExpect(this.leaf1.buildList(), new MtList<Book>());
  }

  // to test the method buildListHelper on the interface Comparator<T>
  boolean testBuildListHelper(Tester t) {
    return t.checkExpect(this.titleNodeLeft.buildListHelper(new MtList<Book>()),
        this.listBooksByTitle)
        && t.checkExpect(this.authorNodeMiddle.buildListHelper(new MtList<Book>()),
            this.listBooksbyAuthor)
        && t.checkExpect(this.priceNodeMiddleNew.buildListHelper(new MtList<Book>()),
            this.listBooksByPrice)
        && t.checkExpect(this.authorNode1.buildListHelper(new MtList<Book>()),
            new ConsList<Book>(this.aliceInWonderland, new MtList<Book>()))
        && t.checkExpect(this.leaf1.buildListHelper(new MtList<Book>()), new MtList<Book>());
  }
}
