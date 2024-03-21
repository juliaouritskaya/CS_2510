import java.awt.Color;

// how to do combine -- do i rotate or just create a new branch
// for getWidth -- can we just get the width of a leaf 
//(because that would mess up my calculation for a tree) 
//and would this have to work on a combine

import tester.*;
import javalib.funworld.WorldScene;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

// to represent a tree
interface ITree {
  // renders ITree to a picture
  WorldImage draw();

  // computes whether any of the twigs in the tree (either stems or branches) are
  // pointing downward rather than upward
  boolean isDrooping();

  // helper method for isDrooping() using recursion
  boolean isDroopingHelper();

  // takes the current tree and a given tree and produces a Branch using the given
  // arguments, with this tree on the left and the given tree on the right
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);

  // helper method for combine() using recursion
  ITree combineHelper(double theta);

  // returns the width of the tree
  double getWidth();

  // helper method for getWidth() using recursion for left tree
  double leftMost();

  // helper method for getWidth() using recursion for left tree
  double rightMost();
}

// to represent a leaf on a tree
class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  // the constructor
  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  // renders Leaf to a picture
  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  // computes whether any of the leaves in the tree are
  // pointing downward rather than upward
  public boolean isDrooping() {
    return false;
  }

  // helper method for isDrooping() using recursion
  public boolean isDroopingHelper() {
    return false;
  }

  // takes the current tree and a given tree and produces a Branch using the given
  // arguments, with this tree on the left and the given tree on the right
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.combineHelper(90 - leftTheta), otherTree.combineHelper(90 - rightTheta));
  }

  // helper method for combine() using recursion
  public ITree combineHelper(double theta) {
    return this;
  }

  // returns the width of the Leaf (its diameter)
  public double getWidth() {
    return this.size * 2;
  }

  // helper method for getWidth() using recursion for left tree
  public double leftMost() {
    return -this.size;
  }

  // helper method for getWidth() using recursion for left tree
  public double rightMost() {
    return this.size;
  }
}

// to represent a stem of a tree
class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  // the constructor
  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }

  // renders Stem to a picture
  public WorldImage draw() {
    WorldImage branch = this.tree.draw();
    WorldImage stem = new LineImage(
        new Posn((int) (this.length * Math.cos(Math.toRadians(180 - this.theta))),
            (int) (this.length * Math.sin(Math.toRadians(this.theta)))),
        Color.black);
    WorldImage stemMovedPinhole = stem.movePinhole(
        (int) (this.length * Math.cos(Math.toRadians(this.theta))) / 2,
        (int) (this.length * Math.sin(Math.toRadians(this.theta))) / -2);
    WorldImage combined = new OverlayImage(stemMovedPinhole, branch);
    WorldImage combinedMovedPinhole = combined.movePinhole(
        (int) -(this.length * Math.cos(Math.toRadians(this.theta))),
        (int) (this.length * Math.sin(Math.toRadians(this.theta))));

    return combinedMovedPinhole;
  }

  // computes whether any of the stems in the tree are
  // pointing downward rather than upward
  public boolean isDrooping() {
    return (this.theta > 180) || this.tree.isDroopingHelper();
  }

  // helper method for isDrooping() using recursion
  public boolean isDroopingHelper() {
    return (this.theta > 180);
  }

  // takes the current tree and a given tree and produces a Branch using the given
  // arguments, with this tree on the left and the given tree on the right
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.combineHelper(leftTheta - 90), otherTree.combineHelper(rightTheta - 90));
  }

  // helper method for combine() using recursion
  public ITree combineHelper(double theta) {

    return new Stem(this.length, (this.theta + theta) % 360, this.tree.combineHelper(theta));
  }

  // returns the width of the Stem
  public double getWidth() {
    return this.rightMost() - this.leftMost();
  }

  // helper method for getWidth() using recursion for left tree
  public double leftMost() {
    double width = this.length * Math.cos(Math.toRadians(this.theta));

    return Math.min(0, width + this.tree.leftMost());
  }

  // helper method for getWidth() using recursion for left tree
  public double rightMost() {
    double width = this.length * Math.cos(Math.toRadians(this.theta));

    return Math.max(0, width + this.tree.rightMost());
  }

}

// to represent a branch of a tree
class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  // the constructor
  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }

  // renders Branch to a picture
  public WorldImage draw() {
    WorldImage leafLeft = this.left.draw();
    WorldImage leafRight = this.right.draw();

    WorldImage branchLeft = new LineImage(
        new Posn((int) (this.leftLength * Math.cos(Math.toRadians(this.rightTheta))),
            (int) (this.leftLength * Math.sin(Math.toRadians(this.rightTheta)))),
        Color.black);
    WorldImage branchLeftMovedPinhole = branchLeft.movePinhole(
        (int) (this.leftLength * Math.cos(Math.toRadians(this.rightTheta))) / -2,
        (int) (this.leftLength * Math.sin(Math.toRadians(this.rightTheta))) / -2);

    WorldImage branchRight = new LineImage(
        new Posn((int) (this.rightLength * Math.cos(Math.toRadians(this.leftTheta))),
            (int) (this.rightLength * Math.sin(Math.toRadians(this.leftTheta)))),
        Color.black);
    WorldImage branchRightMovedPinhole = branchRight.movePinhole(
        (int) (this.rightLength * Math.cos(Math.toRadians(this.leftTheta))) / -2,
        (int) (this.rightLength * Math.sin(Math.toRadians(this.leftTheta))) / -2);

    WorldImage combineLeft = new OverlayImage(leafLeft, branchLeftMovedPinhole);
    WorldImage combineRight = new OverlayImage(leafRight, branchRightMovedPinhole);

    WorldImage bl = combineLeft.movePinhole(
        (int) (this.leftLength * Math.cos(Math.toRadians(this.rightTheta))),
        (int) (this.leftLength * Math.sin(Math.toRadians(this.rightTheta))));

    WorldImage br = combineRight.movePinhole(
        (int) (this.rightLength * Math.cos(Math.toRadians(this.leftTheta))),
        (int) (this.rightLength * Math.sin(Math.toRadians(this.leftTheta))));

    WorldImage combine = new OverlayImage(bl, br);

    return combine;
  }

  // computes whether any of the branches in the tree are
  // pointing downward rather than upward
  public boolean isDrooping() {
    return (this.leftTheta > 180 || this.rightTheta > 180) || this.left.isDroopingHelper()
        || this.right.isDroopingHelper();
  }

  // helper method for isDrooping() using recursion
  public boolean isDroopingHelper() {
    return (this.leftTheta > 180 || this.rightTheta > 180);
  }

  // takes the current tree and a given tree and produces a Branch using the given
  // arguments, with this tree on the left and the given tree on the right
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.combineHelper(90 - leftTheta), otherTree.combineHelper(90 - rightTheta));
  }

  // helper method for combine() using recursion
  public ITree combineHelper(double theta) {
    return new Branch(this.leftLength, this.rightLength, (this.leftTheta + theta) % 360,
        (this.rightTheta + theta) % 360, this.left.combineHelper(theta),
        this.right.combineHelper(theta));
  }

  // returns the width of the Branch
  public double getWidth() {
    return this.rightMost() - this.leftMost();
  }

  // helper method for getWidth() using recursion for left tree
  public double leftMost() {
    double leftBase = (this.leftLength * Math.cos(Math.toRadians(this.leftTheta)));
    double rightBase = (this.rightLength * Math.cos(Math.toRadians(this.rightTheta)));

    return Math.min(Math.min(0, leftBase + left.leftMost()), rightBase + right.leftMost());
  }

  // helper method for getWidth() using recursion for left tree
  public double rightMost() {
    double leftBase = (this.leftLength * Math.cos(Math.toRadians(this.leftTheta)));
    double rightBase = (this.rightLength * Math.cos(Math.toRadians(this.rightTheta)));

    return Math.max(Math.max(0, leftBase + left.rightMost()), rightBase + right.rightMost());
  }
}

class ExamplesTree {
  ExamplesTree() {
  }

  ITree leaf1 = new Leaf(10, Color.RED);
  ITree leaf2 = new Leaf(15, Color.BLUE);
  ITree leaf3 = new Leaf(8, Color.ORANGE);

  ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
  ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
  ITree tree3 = new Branch(30, 30, 200, 260, new Leaf(10, Color.CYAN), new Leaf(15, Color.MAGENTA));

  ITree stem1 = new Stem(40, 90, tree1);
  ITree stem2 = new Stem(50, 90, tree2);
  ITree stem3 = new Stem(60, 90, tree3);

  ITree tree4 = new Branch(30, 30, 200, 260, this.tree1, this.tree2);
  ITree stem4 = new Stem(70, 100, this.tree4);

  ITree stem5 = new Stem(70, 100, this.leaf1);
  ITree stem6 = new Stem(80, 100, this.leaf2);

  // test the method draw on Tree
  boolean testDrawTree(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    WorldScene full = s.placeImageXY(new VisiblePinholeImage(this.tree1.draw()), 250, 250);

    return c.drawScene(full) && c.show();
  }

  // test the method draw on Tree
  boolean testDrawTree2(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    WorldScene full = s.placeImageXY(new VisiblePinholeImage(this.stem1.draw()), 250, 250);

    return c.drawScene(full) && c.show();
  }

  // test the method isDrooping on Tree
  boolean testIsDrooping(Tester t) {
    return t.checkExpect(this.tree1.isDrooping(), false)
        && t.checkExpect(this.tree3.isDrooping(), true)
        && t.checkExpect(this.stem1.isDrooping(), false)
        && t.checkExpect(this.stem4.isDrooping(), true);
  }

  // test the method isDroopingHelper on Tree
  boolean testIsDroopingHelper(Tester t) {
    return t.checkExpect(this.tree1.isDroopingHelper(), false)
        && t.checkExpect(this.tree3.isDroopingHelper(), true)
        && t.checkExpect(this.stem1.isDroopingHelper(), false)
        && t.checkExpect(this.stem4.isDroopingHelper(), false);
  }

  // test the method combine on Tree with draw
  boolean testCombineWithDraw(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    WorldScene full = s.placeImageXY(
        new VisiblePinholeImage(this.tree1.combine(40, 50, 150, 30, this.tree2).draw()), 250, 250);

    return c.drawScene(full) && c.show();
  }

  // test the method combine on Tree
  boolean testCombine(Tester t) {
    return t.checkExpect(this.tree1.combine(40, 50, 150, 30, this.tree2),
        new Branch(40, 50, 150.0, 30.0,
            new Branch(30, 30, 75.0, -20.0, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE)),
            new Branch(30, 30, 175.0, 125.0, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE))))
        && t.checkExpect(this.tree2.combine(70, 40, 15, 130, this.tree1),
            new Branch(70, 40, 15, 130,
                new Branch(30, 30, 190, 140, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE)),
                new Branch(30, 30, 95, 0, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE))));
  }

  // test the method getWidth on Tree
  boolean testGetWidth(Tester t) {
    return t.checkInexact(this.leaf1.getWidth(), 20.0, 0.01)
        && t.checkInexact(this.tree1.getWidth(), 69.193, 0.01);
  }

  //test the method getLeftMost on Tree
  boolean testGetLeftMost(Tester t) {
    return t.checkInexact(stem1.leftMost(), -31.213, 0.01)
        && t.checkInexact(tree1.leftMost(), -31.213, 0.01)
        && t.checkInexact(leaf1.leftMost(), -10.0, 0.01);

  }

  // test the method getRightMost on Tree
  boolean testGetRighMost(Tester t) {
    return t.checkInexact(stem1.rightMost(), 37.981, 0.01)
        && t.checkInexact(tree1.rightMost(), 37.981, 0.01)
        && t.checkInexact(leaf1.rightMost(), 10.0, 0.01);
  }
}