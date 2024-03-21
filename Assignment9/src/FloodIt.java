import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;

import javalib.worldimages.*;

// to represent a single square of the game area 
class Cell {

  static final int CELL_SIZE = 20;

  Random random = new Random();

  // in logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;

  Color color;
  boolean flooded;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // the list of colors
  static ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.RED, Color.ORANGE,
      Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.PINK, Color.GRAY, Color.BLACK));

  // the constructor
  Cell(int x, int y, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = colors.get(random.nextInt(colors.size()));
    this.flooded = flooded;
  }

  // Convenience Constructor only for testing.
  Cell(int x, int y, Color color, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;

    if (left != null) {
      left.right = this;
    }

    if (top != null) {
      top.bottom = this;
    }
  }

  // draws this cell onto the background at the specified logical coordinates
  WorldImage draw() {
    return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, this.color);
  }

  // updates the flooded field of this cell's neighbors
  void updateFlooded(Color color) {
    if (this.left != null && !this.left.flooded && this.left.color.equals(color)) {
      this.left.flooded = true;
    }

    if (this.top != null && !this.top.flooded && this.top.color.equals(color)) {
      this.top.flooded = true;
    }

    if (this.right != null && !this.right.flooded && this.right.color.equals(color)) {
      this.right.flooded = true;
    }

    if (this.bottom != null && !this.bottom.flooded && this.bottom.color.equals(color)) {
      this.bottom.flooded = true;
    }
  }
}

//to represent the world flooding 
class FloodItWorld extends World {

  // all the cells of the game
  ArrayList<Cell> board = new ArrayList<Cell>();

  // defines an int constant
  static int BOARD_SIZE = 20;

  int numColors;

  int clicks;

  // the constructor
  FloodItWorld(int boardSize, int numColors) {
    FloodItWorld.BOARD_SIZE = boardSize;
    this.numColors = numColors;

    Cell origin = new Cell(0, 0, true);

    for (int c = 0; c < boardSize; c++) {
      for (int r = 0; r < boardSize; r++) {
        if (c == 0 && r == 0) {
          this.board.add(origin);
        }
        else {
          this.board.add(new Cell(c, r, false));
        }
      }
    }

    for (int i = 0; i < this.board.size(); i++) {
      Cell update = this.board.get(i);

      if (update.x == 0) {
        update.left = null;
      }
      else {
        update.left = this.board.get(i - FloodItWorld.BOARD_SIZE);
      }

      if (update.y == 0) {
        update.top = null;
      }
      else {
        update.top = this.board.get(i - 1);
      }

      if (update.x == FloodItWorld.BOARD_SIZE - 1) {
        update.right = null;
      }
      else {
        update.right = this.board.get(i + FloodItWorld.BOARD_SIZE);
      }

      if (update.y == FloodItWorld.BOARD_SIZE - 1) {
        update.bottom = null;
      }
      else {
        update.bottom = this.board.get(i + 1);
      }
    }

    this.clicks = FloodItWorld.BOARD_SIZE + (Math.floorDiv(FloodItWorld.BOARD_SIZE, 2));
  }

  // returns the WorldScene to be shown on each clock tick
  public WorldScene makeScene() {
    WorldScene background = new WorldScene(
        FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE + FloodItWorld.BOARD_SIZE * 5,
        FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE + FloodItWorld.BOARD_SIZE * 5);
    WorldImage clicks = new TextImage(Integer.toString(this.clicksLeft()), 20, FontStyle.ITALIC,
        Color.BLACK);

    WorldImage lose = new TextImage("You Lost", 20, FontStyle.ITALIC, Color.BLACK);
    WorldImage win = new TextImage("You Won!", 20, FontStyle.ITALIC, Color.BLACK);

    for (Cell cell : this.board) {
      background.placeImageXY(cell.draw(), Cell.CELL_SIZE / 2 + Cell.CELL_SIZE * cell.x,
          Cell.CELL_SIZE / 2 + Cell.CELL_SIZE * cell.y);
    }

    background.placeImageXY(clicks, FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE + FloodItWorld.BOARD_SIZE * 4);

    if (this.clicksLeft() == 0 && !this.allFlooded()) {
      background.placeImageXY(lose, FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE / 2,
          FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE / 2);
    }
    else if (this.clicksLeft() > 0 && this.allFlooded()) {
      background.placeImageXY(win, FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE / 2,
          FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE / 2);
    }

    return background;
  }

  // finds the cell that is clicked
  Cell findCellAtClick(Posn pos) {

    Cell cell = null;
    for (Cell c : board) {
      if (((pos.x - Cell.CELL_SIZE / 2) / Cell.CELL_SIZE) == c.x
          && ((pos.y - Cell.CELL_SIZE / 2) / Cell.CELL_SIZE) == c.y) {
        cell = c;
      }
    }
    return cell;
  }

  // changes the color field of the first cell to the cell that is clicked on
  void changeCellColor(Cell c) {
    if (c != null) {
      Cell update = board.get(0);
      update.color = c.color;
      board.set(0, update);
    }
  }

  // handles the user pressing the mouse
  public void onMousePressed(Posn pos) {
    this.changeCellColor(this.findCellAtClick(pos));
    this.clicks--;
  }

  // changes the flooded cells to have the flood color
  public void updateCells() {
    Cell floodFromCell = board.get(0);
    Color floodColor = floodFromCell.color;

    for (int i = 0; i < board.size(); i++) {
      Cell cell = board.get(i);
      if (cell.flooded) {
        cell.color = floodColor;
        cell.updateFlooded(floodColor);
      }
    }
  }

  // checks if all the cells are flooded
  public boolean allFlooded() {
    boolean result = true;

    for (Cell cell : this.board) {
      if (!cell.flooded) {
        result = false;
      }
    }
    return result;
  }

  // handles ticking the clock and updating the world
  public void onTick() {
    updateCells();
  }

  // returns clicks left
  public int clicksLeft() {
    if (this.clicks >= 0) {
      return this.clicks;
    }
    else {
      return 0;
    }
  }

  // restarts the game when r key is pressed
  public void onKeyEvent(String k) {
    if (k.equals("r")) {
      new FloodItWorld(BOARD_SIZE, this.numColors);
      this.clicks = FloodItWorld.BOARD_SIZE + (Math.floorDiv(FloodItWorld.BOARD_SIZE, 2));
      makeScene();
    }
  }
}

// to represent examples and tests for Flood-It
class ExamplesFloodIt {
  ExamplesFloodIt() {
  }

  int boardSize = 2;
  int colorsVal = 4;
  int clicks = 8;

  Cell red;
  Cell orange;
  Cell yellow;
  Cell green;

  ArrayList<Color> colors = new ArrayList<Color>(
      Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN));
  ArrayList<Cell> board;

  FloodItWorld world = new FloodItWorld(3, 4);

  // sets up the intitial conditions
  void initialConditions() {
    this.red = new Cell(0, 0, Color.RED, true, null, null, null, null);
    this.orange = new Cell(1, 0, Color.ORANGE, false, this.red, null, null, null);
    this.yellow = new Cell(0, 1, Color.YELLOW, false, null, this.red, null, null);
    this.green = new Cell(1, 1, Color.GREEN, false, this.yellow, this.orange, null, null);

    this.red.bottom = this.yellow;
    this.orange.bottom = this.green;
    this.yellow.right = this.green;

    this.board = new ArrayList<Cell>(Arrays.asList(this.red, this.orange, this.yellow, this.green));

    this.world.board = this.board;
  }

  // to test the draw method in the class Cell
  void testDraw(Tester t) {
    t.checkExpect(this.red.draw(), new RectangleImage(22, 22, OutlineMode.SOLID, this.red.color));
    t.checkExpect(this.orange.draw(),
        new RectangleImage(22, 22, OutlineMode.SOLID, this.orange.color));
  }

  // to test the method updateFlooded in the class Cell
  void testUpdateFlooded(Tester t) {
    initialConditions();

    t.checkExpect(this.red.right.flooded, false);
    t.checkExpect(this.red.bottom.flooded, false);

    this.red.updateFlooded(Color.RED);

    t.checkExpect(this.red.right.flooded, false);
    t.checkExpect(this.red.bottom.flooded, false);

    this.red.color = Color.ORANGE;
    this.red.updateFlooded(Color.ORANGE);

    t.checkExpect(this.red.right.flooded, true);
    t.checkExpect(this.red.bottom.flooded, false);

    this.red.updateFlooded(Color.YELLOW);

    t.checkExpect(this.red.right.flooded, true);
    t.checkExpect(this.red.bottom.flooded, true);
  }

  // to test the method makeScene in the class FloodItWorld
  void testMakeScene(Tester t) {
    initialConditions();

    WorldScene background = new WorldScene(24, 24);
    WorldImage clicks = new TextImage("4", 20, FontStyle.ITALIC, Color.BLACK);

    background.placeImageXY(this.red.draw(), 10, 10);
    background.placeImageXY(this.orange.draw(), 30, 10);
    background.placeImageXY(this.yellow.draw(), 10, 30);
    background.placeImageXY(this.green.draw(), 30, 30);

    background.placeImageXY(clicks, 4, 21);

    // t.checkExpect(this.world.makeScene(), background);
  }

  // to test the method setUp in the class FloodItWorld
  void testSetUp(Tester t) {
    initialConditions();
    t.checkExpect(this.board.get(0).flooded, true);
    t.checkExpect(this.board.get(3).flooded, false);
  }

  // to test the method findCellAtClick in the class FloodItWorld
  void testFindAtClick(Tester t) {
    initialConditions();

    t.checkExpect(this.world.findCellAtClick(new Posn(10, 10)), this.board.get(0));
    t.checkExpect(this.world.findCellAtClick(new Posn(30, 10)), this.board.get(1));
    t.checkExpect(this.world.findCellAtClick(new Posn(10, 30)), this.board.get(2));
    t.checkExpect(this.world.findCellAtClick(new Posn(30, 30)), this.board.get(3));
    t.checkExpect(this.world.findCellAtClick(new Posn(100, 100)), null);
  }

  // to test the method changeCellColor in the class FloodItWorld
  void testChangeCellColor(Tester t) {
    initialConditions();

    t.checkExpect(this.world.board.get(0), this.red);
    this.world.changeCellColor(green);
    t.checkExpect(this.world.board.get(0).color, Color.GREEN);
  }

  // to test the method onMousePressed in the class FloodItWorld
  void testOnMousePressed(Tester t) {
    initialConditions();

    this.world.makeScene();
    t.checkExpect(this.world.board.get(0), this.red);
    t.checkExpect(this.world.clicks, 4);
    this.world.onMousePressed(new Posn(30, 10));
    t.checkExpect(this.world.board.get(0).color, Color.ORANGE);
    t.checkExpect(this.world.clicks, 3);
  }

  // to test the method updateCells in the class FloodItWorld
  void testUpdateCells(Tester t) {
    initialConditions();

    t.checkExpect(this.world.board.get(0), this.red);
    this.world.board.get(1).flooded = true;
    this.world.updateCells();
    t.checkExpect(this.world.board.get(1).color, Color.RED);
  }

  // to test the method onTick in the class FloodItWorld
  void testOnTick(Tester t) {
    initialConditions();

    t.checkExpect(this.world.board.get(0), this.red);
    this.world.board.get(1).flooded = true;
    this.world.onTick();
    t.checkExpect(this.world.board.get(1).color, Color.RED);
  }

  // to test the method clicksLeft in the class FloodItWorld
  void testClicksLeft(Tester t) {
    initialConditions();

    this.world.clicks = 4;
    t.checkExpect(this.world.clicksLeft(), 4);
  }

  // to test the method onKeyEvent in the class FloodItWorld
  void testOnKeyEvent(Tester t) {
    initialConditions();

    t.checkExpect(this.world.board.size(), 4);
    t.checkExpect(this.world.numColors, 4);
    t.checkExpect(this.world.clicks, 4);
    this.world.onMousePressed(new Posn(30, 10));
    t.checkExpect(this.world.board.get(0).color, Color.ORANGE);
    t.checkExpect(this.world.clicks, 3);
    this.world.onKeyEvent("r");
    t.checkExpect(this.world.clicks, 4);
  }

  // to test the game
  void testGame(Tester t) {
    FloodItWorld w = new FloodItWorld(18, 6);
    w.bigBang(500, 500, 0.1);
  }
}