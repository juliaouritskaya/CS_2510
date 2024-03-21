import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

// to represent a vertex 
class Vertex {
  ArrayList<Edge> outEdges = new ArrayList<Edge>();
  Posn pos;
  Vertex left;
  Vertex top;
  Vertex right;
  Vertex bottom;
  Vertex prev;
  boolean flagBottom = true;
  boolean flagRight = true;
  boolean visited = false;

  // the constructor
  Vertex(Posn pos) {
    this.pos = pos;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
    this.prev = null;
  }

  // draws the cell from the vertex
  WorldImage drawCell(Posn pos, Color color) {
    WorldImage cell = new RectangleImage(Maze.CELL - 2, Maze.CELL - 2, OutlineMode.SOLID, color);
    int x = -pos.x * Maze.CELL / pos.x / 2;
    int y = -pos.x * Maze.CELL / pos.x / 2;

    return cell.movePinhole(x, y);
  }

  // draws a right edge
  WorldImage drawRightEdge() {
    WorldImage line = new LineImage(new Posn(0, Maze.CELL), Color.BLACK);
    int x = Maze.CELL * -1;
    int y = Maze.CELL / -2;

    return line.movePinhole(x, y);
  }

  // draws a bottom edge
  WorldImage drawBottomEdge() {
    WorldImage line = new LineImage(new Posn(Maze.CELL, 0), Color.BLACK);
    int x = Maze.CELL / -2;
    int y = Maze.CELL * -1;

    return line.movePinhole(x, y);
  }

  // determines if this vertex is equal to the given vertex
  public boolean equals(Object other) {
    if (!(other instanceof Vertex)) {
      return false;
    }

    Vertex that = (Vertex) other;
    return this.pos.x == that.pos.x && this.pos.y == that.pos.y;
  }

  // hashcode method overridden due to equals overridden
  public int hashCode() {
    return this.pos.hashCode() * 1000;
  }

  // updates the previous cell
  void updatePrev() {
    if (this.left != null && this.left.prev == null && !this.left.flagRight) {
      this.prev = this.left;
    }
    else if (this.top != null && this.top.prev == null && !this.top.flagRight) {
      this.prev = this.top;
    }
    else if (this.right != null && this.right.prev == null && !this.right.flagRight) {
      this.prev = this.right;
    }
    else if (this.bottom != null && this.bottom.prev == null && !this.bottom.flagRight) {
      this.prev = this.bottom;
    }
  }
}

// to represent an edge 
class Edge implements Comparable<Edge> {
  Vertex source;
  Vertex dest;
  int weight;

  // the constructor
  Edge(Vertex source, Vertex dest, int weight) {
    this.source = source;
    this.dest = dest;
    this.weight = weight;
  }

  // determines if the weight of this edge is smaller than the given edge
  public int compareTo(Edge other) {
    if (this.weight < other.weight) {
      return -1;
    }

    if (this.weight > other.weight) {
      return 1;
    }

    else {
      return 0;
    }
  }

  // determines if this edge is equal to the given edge
  public boolean equals(Object other) {
    if (!(other instanceof Edge)) {
      return false;
    }

    Edge that = (Edge) other;
    return (this.source.equals(that.source) || this.source.equals(that.dest))
        && (this.dest.equals(that.dest) || this.dest.equals(that.source));
  }

  // hashcode method overridden due to equals overridden
  public int hashCode() {
    return this.weight * 1000;
  }
}

// to represent a player 
class Player {
  Vertex current;

  // the constructor
  Player(Vertex current) {
    this.current = current;
  }

  // draws the player
  WorldImage drawPlayer() {
    WorldImage player = new RectangleImage(Maze.CELL - 3, Maze.CELL - 3, OutlineMode.SOLID,
        Color.RED);
    int x = Maze.CELL * -1 / 2;

    return player.movePinhole(x, x);
  }

  // determines if the player can move
  boolean canMove(String direction) {
    boolean answer = false;
    if (direction.equals("left") && this.current.left != null) {
      return !this.current.left.flagRight;
    }
    else if (direction.equals("up") && this.current.top != null) {
      return !this.current.top.flagBottom;
    }
    else if (direction.equals("right") && this.current.right != null) {
      return !this.current.flagRight;
    }
    else if (direction.equals("down") && this.current.bottom != null) {
      return !this.current.flagBottom;
    }
    else {
      return answer;
    }
  }
}

// to represent the maze
class Maze extends World {
  static final int CELL = 20;
  int width = 20;
  int height = 20;

  ArrayList<ArrayList<Vertex>> board;
  ArrayList<Edge> edges = new ArrayList<Edge>();
  ArrayList<Edge> worklist = new ArrayList<Edge>();
  ArrayList<Vertex> visited = new ArrayList<Vertex>();
  Player player;
  Vertex last;
  boolean finished = false;
  WorldScene background = new WorldScene(0, 0);

  // the constructor
  Maze() {
    this.board = this.setup();
    this.player = new Player(this.board.get(0).get(0));
    this.createEdges(this.board);
    this.kruskals(this.board, this.worklist);
    this.last = this.board.get(this.height - 1).get(this.width - 1);
    this.makeScene();
  }

  // new constructor for testing
  Maze(int width, int height) {
    this.width = width;
    this.height = height;
    this.board = this.setup();
    this.player = new Player(this.board.get(0).get(0));
    this.visited = new ArrayList<Vertex>();
    this.last = this.board.get(height - 1).get(width - 1);
    this.finished = false;
    this.makeScene();
  }

  // returns the WorldScene to be shown on each clock tick
  public WorldScene makeScene() {
    WorldImage endPoint = this.board.get(this.height - 1).get(this.width - 1)
        .drawCell(new Posn(this.width, this.height), Color.MAGENTA);
    WorldImage player = this.player.drawPlayer();
    TextImage winner = new TextImage("You Won!", 40, Color.BLACK);

    background.placeImageXY(endPoint, (this.width - 1) * Maze.CELL, (this.height - 1) * Maze.CELL);

    for (int x = 0; x < this.height; x = x + 1) {
      for (int y = 0; y < this.width; y = y + 1) {
        WorldImage path = this.board.get(x).get(y).drawCell(new Posn(this.width, this.height),
            Color.PINK);
        WorldImage rightEdge = this.board.get(x).get(y).drawRightEdge();
        WorldImage bottomEdge = this.board.get(x).get(y).drawBottomEdge();

        this.updateFlagRight(this.board.get(x).get(y));
        this.updateFlagBottom(this.board.get(x).get(y));

        if (this.board.get(x).get(y).visited) {
          background.placeImageXY(path, y * Maze.CELL, x * Maze.CELL);
        }

        if (this.board.get(x).get(y).flagRight) {
          background.placeImageXY(rightEdge, Maze.CELL * y, Maze.CELL * x);
        }

        if (this.board.get(x).get(y).flagBottom) {
          background.placeImageXY(bottomEdge, Maze.CELL * y, Maze.CELL * x);
        }
      }
    }

    background.placeImageXY(player, this.player.current.pos.x * Maze.CELL,
        this.player.current.pos.y * Maze.CELL);

    if (this.player.current == this.board.get(this.height - 1).get(this.width - 1)) {
      background.placeImageXY(winner, this.width * Maze.CELL / 2, this.height * Maze.CELL / 2);
    }

    return background;
  }

  // sets up the board of vertices
  ArrayList<ArrayList<Vertex>> setup() {
    ArrayList<ArrayList<Vertex>> board = new ArrayList<ArrayList<Vertex>>();
    for (int i = 0; i < height; i++) {
      board.add(new ArrayList<Vertex>());
      ArrayList<Vertex> row = board.get(i);
      for (int j = 0; j < this.width; j++) {
        row.add(new Vertex(new Posn(j, i)));
      }
    }

    for (int c = 0; c < height; c++) {
      for (int r = 0; r < this.width; r++) {
        if (c < height - 1) {
          board.get(c).get(r).bottom = board.get(c + 1).get(r);
        }
        if (c >= 1) {
          board.get(c).get(r).top = board.get(c - 1).get(r);
        }
        if (r < this.width - 1) {
          board.get(c).get(r).right = board.get(c).get(r + 1);
        }
        if (r >= 1) {
          board.get(c).get(r).left = board.get(c).get(r - 1);
        }
      }
    }

    this.createEdges(board);
    return board;
  }

  // creates all edges leaving each vertex
  ArrayList<Edge> createEdges(ArrayList<ArrayList<Vertex>> board) {
    Random random = new Random();

    for (int c = 0; c < board.size(); c++) {
      for (int r = 0; r < board.get(c).size(); r++) {

        if (c < board.size() - 1) {
          Edge bottom = new Edge(board.get(c).get(r), board.get(c).get(r).bottom,
              random.nextInt(50));
          this.edges.add(bottom);
        }

        if (r < board.get(c).size() - 1) {
          Edge right = new Edge(board.get(c).get(r), board.get(c).get(r).right, random.nextInt(50));
          this.edges.add(right);
        }
      }
    }

    this.sort(this.edges);
    return this.edges;
  }

  // sorts an array list of edges by their weight
  ArrayList<Edge> sort(ArrayList<Edge> edges) {
    Collections.sort(edges);
    return edges;
  }

  // maps each vertex to itself and then connects them
  ArrayList<Edge> kruskals(ArrayList<ArrayList<Vertex>> board, ArrayList<Edge> worklist) {
    HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>();

    for (ArrayList<Vertex> array : board) {
      for (Vertex vertex : array) {
        representatives.put(vertex, vertex);
      }
    }

    int count = 0;

    while (this.worklist.size() < this.edges.size() && count < this.edges.size()) {
      Edge edge = this.edges.get(count);

      if (!find(representatives, edge.dest).equals(find(representatives, edge.source))) {
        this.worklist.add(edge);
        union(representatives, find(representatives, edge.dest),
            find(representatives, edge.source));
      }
      count = count + 1;
    }

    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        for (Edge edge : this.worklist) {
          if (this.board.get(i).get(j).equals(edge.source)
              || this.board.get(i).get(j).equals(edge.dest)) {
            this.board.get(i).get(j).outEdges.add(edge);
          }
        }
      }
    }
    return this.worklist;
  }

  // finds the parent vertex
  Vertex find(HashMap<Vertex, Vertex> representatives, Vertex vertex) {
    if (representatives.get(vertex).equals(vertex)) {
      return representatives.get(vertex);
    }
    else {
      return find(representatives, representatives.get(vertex));
    }
  }

  // changes the vertex to the given vertex
  void union(HashMap<Vertex, Vertex> representatives, Vertex vertex1, Vertex vertex2) {
    representatives.put(vertex1, vertex2);
  }

  // updates the flagRight field of a vertex
  void updateFlagRight(Vertex vertex) {
    for (Edge edge : this.worklist) {
      if (edge.source.pos.y == edge.dest.pos.y) {
        edge.source.flagRight = false;
      }
    }
  }

  // updates the flagBottom field of a vertex
  void updateFlagBottom(Vertex vertex) {
    for (Edge edge : this.worklist) {
      if (edge.source.pos.x == edge.dest.pos.x) {
        edge.source.flagBottom = false;
      }
    }
  }

  // handles the user pressing a key
  public void onKeyEvent(String ke) {
    WorldImage player = this.player.drawPlayer();

    if (ke.equals("left") && this.player.canMove("left")) {
      this.player.current.visited = true;
      this.player.current = this.player.current.left;
    }
    else if (ke.equals("up") && this.player.canMove("up")) {
      this.player.current.visited = true;
      this.player.current = this.player.current.top;
    }
    else if (ke.equals("right") && this.player.canMove("right")) {
      this.player.current.visited = true;
      this.player.current = this.player.current.right;
    }
    else if (ke.equals("down") && this.player.canMove("down")) {
      this.player.current.visited = true;
      this.player.current = this.player.current.bottom;
    }
    else if (ke.equals("b")) {
      this.last = this.board.get(this.height - 1).get(this.width - 1);
      this.visited = this.bfs(this.board.get(0).get(0), this.last);
    }
    else if (ke.equals("d")) {
      this.last = this.board.get(this.height - 1).get(this.width - 1);
      this.visited = this.dfs(this.board.get(0).get(0), this.last);
    }

    background.placeImageXY(player, this.player.current.pos.x * Maze.CELL,
        this.player.current.pos.y * Maze.CELL);
    this.makeScene();
  }

  // finds the path
  ArrayList<Vertex> findPath(Vertex source, Vertex dest, IUtil<Vertex> worklist) {
    ArrayList<Vertex> visited = new ArrayList<Vertex>();

    worklist.add(source);

    while (worklist.size() > 0) {
      Vertex next = worklist.remove();
      if (next == dest) {
        return visited;
      }
      // do nothing
      else if (visited.contains(next)) {

      }
      else {
        for (Edge edge : next.outEdges) {
          worklist.add(edge.source);
          worklist.add(edge.dest);
          if (visited.contains(edge.source)) {
            next.prev = edge.source;
          }
          else if (visited.contains(edge.dest)) {
            next.prev = edge.dest;
          }
        }
        visited.add(next);
      }
    }
    return visited;
  }

  // breadth-first search
  ArrayList<Vertex> bfs(Vertex source, Vertex dest) {
    return this.findPath(source, dest, new Queue<Vertex>());
  }

  // depth-first search
  ArrayList<Vertex> dfs(Vertex source, Vertex dest) {
    return this.findPath(source, dest, new Stack<Vertex>());
  }

  // updates the maze1 every tick
  public void onTick() {
    if (this.visited.size() > 1) {
      this.pathFinder();
    }
    else if (this.visited.size() > 0) {
      this.endCell();
    }
    else if (this.finished && this.last.prev != null) {
      this.reconstruct();
    }
  }

  // draws the path to the end
  void pathFinder() {
    Vertex next = this.visited.remove(0);
    WorldImage cell = next.drawCell(new Posn(this.width, this.height), Color.YELLOW);
    background.placeImageXY(cell, next.pos.x * Maze.CELL, next.pos.y * Maze.CELL);
  }

  // draws the last cell
  void endCell() {
    Vertex next = this.visited.remove(0);
    WorldImage cell = next.drawCell(new Posn(this.width, this.height), Color.YELLOW);
    background.placeImageXY(cell, next.pos.x * Maze.CELL, next.pos.y * Maze.CELL);

    if (!this.last.left.flagRight && this.last.left.prev != null) {
      this.last.prev = this.last.left;
    }
    else if (!this.last.top.flagBottom && this.last.top.prev != null) {
      this.last.prev = this.last.top;
    }
    else {
      this.last.prev = next;
    }
    this.finished = true;
  }

  // reconstructs the maze1
  void reconstruct() {
    WorldImage cell = this.last.drawCell(new Posn(this.width, this.height), Color.BLUE);
    WorldImage prev = this.last.prev.drawCell(new Posn(this.width, this.height), Color.BLUE);

    if (this.last.pos.x == this.width - 1 && this.last.pos.y == this.height - 1) {
      background.placeImageXY(cell, this.last.pos.x * Maze.CELL, this.last.pos.y * Maze.CELL);
    }

    background.placeImageXY(prev, this.last.prev.pos.x * Maze.CELL,
        this.last.prev.pos.y * Maze.CELL);
    this.last = this.last.prev;
  }
}

// to represent an utility interface 
interface IUtil<T> {
  // adds an element
  void add(T elem);

  // removes an element
  T remove();

  // returns the size
  int size();
}

// to represent a queue 
class Queue<T> implements IUtil<T> {
  Deque<T> deque;

  // the constructor
  Queue() {
    this.deque = new Deque<T>();
  }

  // adds an element to the queue
  public void add(T elem) {
    this.deque.addAtTail(elem);
  }

  // removes an element from the queue
  public T remove() {
    return this.deque.removeFromHead();
  }

  // returns the size of the queue
  public int size() {
    return this.deque.size();
  }
}

// to represent a stack 
class Stack<T> implements IUtil<T> {
  Deque<T> deque;

  // the constructor
  Stack() {
    this.deque = new Deque<T>();
  }

  // adds an element to the stack
  public void add(T elem) {
    this.deque.addAtHead(elem);
  }

  // removes an element from the stack
  public T remove() {
    return this.deque.removeFromHead();
  }

  // returns the size of the stack
  public int size() {
    return this.deque.size();
  }
}

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

//to represent examples and tests for the maze1 
class ExamplesMaze {
  ExamplesMaze() {
  }

  HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
  Maze maze = new Maze(2, 3);
  Maze maze1;

  Vertex a = new Vertex(new Posn(0, 0));
  Vertex b = new Vertex(new Posn(0, 1));
  Vertex c = new Vertex(new Posn(0, 2));
  Vertex d = new Vertex(new Posn(1, 0));
  Vertex e = new Vertex(new Posn(1, 1));
  Vertex f = new Vertex(new Posn(1, 2));
  Vertex g = new Vertex(new Posn(2, 0));
  Vertex h = new Vertex(new Posn(2, 1));
  Vertex i = new Vertex(new Posn(2, 2));

  Edge e1 = new Edge(this.a, this.c, 1);
  Edge e2 = new Edge(this.a, this.b, 2);
  Edge e3 = new Edge(this.c, this.d, 3);
  Edge e4 = new Edge(this.b, this.d, 4);
  Edge e5 = new Edge(this.b, this.c, 5);
  Edge e6 = new Edge(this.e, this.f, 6);
  Edge e7 = new Edge(this.c, this.f, 7);

  Vertex j;
  Vertex k;
  Vertex l;
  Vertex m;
  Vertex n;
  Vertex o;

  Edge aB;
  Edge aE;
  Edge bC;
  Edge bE;
  Edge bF;
  Edge cD;
  Edge eC;
  Edge fD;

  Edge jK;
  Edge jN;
  Edge kL;
  Edge kN;
  Edge kO;
  Edge lM;
  Edge nL;
  Edge oM;

  ArrayList<Vertex> list1;
  ArrayList<Vertex> list2;

  ArrayList<ArrayList<Vertex>> board;

  ArrayList<Edge> emptyEdges;
  ArrayList<Edge> edges;
  ArrayList<Edge> sortedEdges;

  HashMap<Vertex, Vertex> reps;
  HashMap<Vertex, Vertex> connectedreps;

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

  // to test the game
  void testGame(Tester t) {
    Maze mazeGame = new Maze();
    mazeGame.bigBang(20 * Maze.CELL, 20 * Maze.CELL + Maze.CELL, 0.05);
  }

  // initializes the data
  void initialConditions() {
    this.maze1 = new Maze();
    this.emptyEdges = new ArrayList<Edge>();

    this.j = new Vertex(new Posn(0, 0));
    this.k = new Vertex(new Posn(0, 1));
    this.l = new Vertex(new Posn(0, 2));
    this.m = new Vertex(new Posn(1, 0));
    this.n = new Vertex(new Posn(1, 1));
    this.o = new Vertex(new Posn(1, 2));

    this.jK = new Edge(this.j, this.k, 30);
    this.jN = new Edge(this.j, this.n, 50);
    this.kL = new Edge(this.k, this.l, 40);
    this.kN = new Edge(this.k, this.n, 35);
    this.kO = new Edge(this.k, this.o, 50);
    this.lM = new Edge(this.l, this.m, 25);
    this.nL = new Edge(this.n, this.l, 15);
    this.oM = new Edge(this.o, this.m, 50);

    this.j.outEdges.add(jK);
    this.j.outEdges.add(jN);
    this.k.outEdges.add(kL);
    this.k.outEdges.add(kN);
    this.k.outEdges.add(kO);
    this.l.outEdges.add(lM);
    this.o.outEdges.add(oM);
    this.n.outEdges.add(nL);

    this.list1 = new ArrayList<Vertex>(Arrays.asList(this.j, this.k, this.l));
    this.list2 = new ArrayList<Vertex>(Arrays.asList(this.m, this.n, this.o));

    this.board = new ArrayList<ArrayList<Vertex>>(Arrays.asList(this.list1, this.list2));

    this.edges = new ArrayList<Edge>(
        Arrays.asList(this.jK, this.kL, this.kO, this.nL, this.jN, this.kN, this.lM, this.oM));

    this.sortedEdges = new ArrayList<Edge>(
        Arrays.asList(this.nL, this.lM, this.jK, this.kN, this.kL, this.kO, this.jN, this.oM));

    this.reps = new HashMap<Vertex, Vertex>();
    this.reps.put(this.j, this.j);
    this.reps.put(this.k, this.k);
    this.reps.put(this.l, this.l);
    this.reps.put(this.m, this.m);
    this.reps.put(this.n, this.n);
    this.reps.put(this.o, this.o);

    this.connectedreps = new HashMap<Vertex, Vertex>();
    this.connectedreps.put(this.j, this.j);
    this.connectedreps.put(this.k, this.k);
    this.connectedreps.put(this.l, this.l);
    this.connectedreps.put(this.m, this.m);
    this.connectedreps.put(this.n, this.n);
    this.connectedreps.put(this.o, this.o);
  }

  // to test the method drawCell in the class Vertex
  void testDrawCell(Tester t) {
    initialConditions();

    t.checkExpect(this.j.drawCell(new Posn(2, 2), Color.RED),
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.RED).movePinhole(-10, -10));
  }

  // to test the method compareTo in the class Edge
  void testCompareTo(Tester t) {
    initialConditions();

    t.checkExpect(this.jK.compareTo(this.jN), -1);
    t.checkExpect(this.kO.compareTo(this.lM), 1);
    t.checkExpect(this.kO.compareTo(this.oM), 0);
  }

  // to test the method sort in the class maze1
  void testSort(Tester t) {
    initialConditions();
    t.checkExpect(this.maze1.sort(this.edges), this.sortedEdges);
  }

  // sets up the initial conditions by intializing
  void initialCondition() {
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
    initialCondition();

    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 0);
    t.checkExpect(this.sentinelNumber.size(), 5);
    t.checkExpect(this.sentinelString.size(), 4);
  }

  // to test the method addAtHead on the class Deque
  void testAddAtHead(Tester t) {
    initialCondition();

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
    initialCondition();

    t.checkExpect(this.deque2, this.deque2);
    this.deque1.addAtTail("abc");
    this.deque1.addAtTail("bcd");
    this.deque1.addAtTail("cde");
    this.deque1.addAtTail("def");
    t.checkExpect(this.deque1, this.deque2);
  }

  // to test the method removeFromHead on the class Deque and Sentinel
  void testRemoveFromHead(Tester t) {
    initialCondition();

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
    initialCondition();

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
    initialCondition();

    t.checkException(new RuntimeException("cannot remove from an empty list"), this.sentinelEmpty,
        "removeFromHelper");

    this.nodeB.removeFromHelper();
    t.checkExpect(this.nodeA.next, this.nodeC);
  }

  // to test the method find on the class Deque
  void testFind2(Tester t) {
    initialCondition();

    t.checkExpect(this.deque2.find(new FindNode<String>("abc")), this.sentinelString.next);
    t.checkExpect(this.deque4.find(new FindNode<Integer>(3)), this.node2.next);
    t.checkExpect(this.deque3.find(new FindNode<Integer>(7)), this.sentinelEmpty);
  }

  // to test the method findHelper on the abstract class ANode<T>
  void testFindHelper(Tester t) {
    initialCondition();

    t.checkExpect(this.sentinelNumber.find(new FindNode<Integer>(2)), this.node2);
    t.checkExpect(this.nodeB.findHelper(new FindNode<String>("bcd")), this.nodeB);
  }

  // to test the method removeNode on the class Deque
  void testRemoveNode(Tester t) {
    initialCondition();

    this.deque2.removeNode(this.nodeA);
    this.deque2.removeNode(this.nodeB);
    this.deque2.removeNode(this.nodeC);
    this.deque2.removeNode(this.nodeD);
    t.checkExpect(this.deque2, this.deque1);
  }

  // initializes the data
  void init() {
    this.maze.edges = new ArrayList<Edge>(
        Arrays.asList(this.e1, this.e2, this.e3, this.e4, this.e5, this.e6, this.e7));

    this.maze.worklist = new ArrayList<Edge>(
        Arrays.asList(this.e1, this.e2, this.e3, this.e5, this.e6));

    this.maze.board.get(0).get(0).flagRight = false;
    this.maze.board.get(0).get(1).flagRight = true;
    this.maze.board.get(1).get(0).flagRight = true;
    this.maze.board.get(1).get(1).flagRight = true;
    this.maze.board.get(2).get(0).flagRight = true;
    this.maze.board.get(2).get(1).flagRight = true;

    map.put(this.maze.board.get(0).get(0), this.maze.board.get(0).get(0));
    map.put(this.maze.board.get(0).get(1), this.maze.board.get(0).get(1));
    map.put(this.maze.board.get(1).get(0), this.maze.board.get(1).get(0));
    map.put(this.maze.board.get(1).get(1), this.maze.board.get(1).get(1));
    map.put(this.maze.board.get(2).get(0), this.maze.board.get(2).get(0));
    map.put(this.maze.board.get(2).get(1), this.maze.board.get(2).get(1));

    this.maze.board.get(0).get(0).flagBottom = false;
    this.maze.board.get(0).get(1).flagBottom = false;
    this.maze.board.get(1).get(0).flagBottom = false;
    this.maze.board.get(1).get(1).flagBottom = false;
    this.maze.board.get(2).get(0).flagBottom = true;
    this.maze.board.get(2).get(1).flagBottom = true;
  }

  // to test the method setup in the class maze1
  void testSetup(Tester t) {
    this.init();

    t.checkExpect(maze.board, new ArrayList<ArrayList<Vertex>>(Arrays.asList(
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0), maze.board.get(0).get(1))),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(1).get(0), maze.board.get(1).get(1))),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(2).get(0), maze.board.get(2).get(1))))));

    t.checkExpect(this.maze.board.get(0).get(0).right, this.maze.board.get(0).get(1));
    t.checkExpect(maze.board.get(0).get(0).bottom, maze.board.get(1).get(0));
    t.checkExpect(maze.board.get(0).get(0).top, null);
    t.checkExpect(maze.board.get(0).get(0).left, null);
  }

  // to test the method kruskals in the class Maze
  void testKruskals(Tester t) {
    this.init();

    maze.setup();
    t.checkExpect(maze.worklist.get(0),
        new Edge(maze.worklist.get(0).source, maze.worklist.get(0).dest, 1));
    t.checkExpect(maze.worklist.get(1),
        new Edge(maze.worklist.get(1).source, maze.worklist.get(1).dest, 2));
    t.checkExpect(maze.worklist.get(2),
        new Edge(maze.worklist.get(2).source, maze.worklist.get(2).dest, 3));
    t.checkExpect(maze.worklist.get(3),
        new Edge(maze.worklist.get(3).source, maze.worklist.get(3).dest, 5));
    t.checkExpect(maze.worklist.get(4),
        new Edge(maze.worklist.get(4).source, maze.worklist.get(4).dest, 6));
  }

  // to test the method union in the class Maze
  void testUnion(Tester t) {
    this.init();

    maze.union(this.map, maze.board.get(0).get(0), maze.board.get(0).get(1));
    t.checkExpect(maze.find(this.map, maze.board.get(0).get(0)), maze.board.get(0).get(1));
    maze.union(this.map, maze.board.get(0).get(1), maze.board.get(1).get(1));
    t.checkExpect(maze.find(this.map, maze.board.get(0).get(1)), maze.board.get(1).get(1));
    maze.union(this.map, maze.board.get(2).get(0), maze.board.get(0).get(1));
    t.checkExpect(maze.find(this.map, maze.board.get(0).get(0)), maze.board.get(1).get(1));
  }

  // to test the method find in the class Maze
  void testFind(Tester t) {
    this.init();

    t.checkExpect(maze.find(this.map, maze.board.get(0).get(0)), maze.board.get(0).get(0));
    t.checkExpect(maze.find(this.map, maze.board.get(2).get(0)), maze.board.get(2).get(0));
  }

  // to test the method onKeyEvent in the class Maze
  void testOnKeyEvent(Tester t) {
    this.init();

    maze.onKeyEvent("right");
    t.checkExpect(maze.player.current, maze.board.get(0).get(1));
    maze.onKeyEvent("down");
    t.checkExpect(maze.player.current, maze.board.get(1).get(1));
    maze.onKeyEvent("up");
    t.checkExpect(maze.player.current, maze.board.get(0).get(1));
    maze.onKeyEvent("left");
    t.checkExpect(maze.player.current, maze.board.get(0).get(0));
    maze.onKeyEvent("d");
    t.checkExpect(maze.visited, new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));
    maze.onKeyEvent("b");
    t.checkExpect(maze.visited, new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));
  }

  // to test the method canMove in the class Maze
  void testCanMove(Tester t) {
    this.init();

    t.checkExpect(maze.player.canMove("up"), false);
    t.checkExpect(maze.player.canMove("left"), false);
    t.checkExpect(maze.player.canMove("down"), true);
    t.checkExpect(maze.player.canMove("right"), true);
  }

  // to test the method updateFlagRight in the class Maze
  void testUpdateFlagRight(Tester t) {
    this.init();

    maze.updateFlagRight(maze.board.get(0).get(0));
    t.checkExpect(maze.board.get(0).get(0).flagRight, false);
    maze.updateFlagRight(maze.board.get(2).get(0));
    t.checkExpect(maze.board.get(2).get(0).flagRight, true);
    maze.updateFlagRight(maze.board.get(1).get(1));
    t.checkExpect(maze.board.get(1).get(1).flagRight, true);
    maze.updateFlagRight(maze.board.get(2).get(1));
    t.checkExpect(maze.board.get(2).get(1).flagRight, true);
  }

  // to test the method updateFlagBottom in the class Maze
  void testUpdateFlagBottom(Tester t) {
    this.init();

    maze.updateFlagBottom(maze.board.get(0).get(0));
    t.checkExpect(maze.board.get(0).get(0).flagBottom, false);

    maze.updateFlagBottom(maze.board.get(0).get(1));
    t.checkExpect(maze.board.get(0).get(1).flagBottom, false);

    maze.updateFlagBottom(maze.board.get(2).get(0));
    t.checkExpect(maze.board.get(2).get(0).flagBottom, true);

    maze.updateFlagBottom(maze.board.get(1).get(0));
    t.checkExpect(maze.board.get(1).get(0).flagBottom, false);

    maze.updateFlagBottom(maze.board.get(1).get(1));
    t.checkExpect(maze.board.get(1).get(1).flagBottom, false);

    maze.updateFlagBottom(maze.board.get(2).get(1));
    t.checkExpect(maze.board.get(2).get(1).flagBottom, true);
  }

  // to test the method addToHead in the interface IUtil
  void testAddToHead(Tester t) {
    Stack<Vertex> s = new Stack<Vertex>();
    t.checkExpect(s.size(), 0);
    s.add(new Vertex(new Posn(0, 0)));

    t.checkExpect(s.size(), 1);
  }

  // to test the method dfs in the class Maze
  void testdfs(Tester t) {
    this.init();

    t.checkExpect(maze.dfs(maze.board.get(0).get(0), maze.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));
  }

  // to test the method bfs in the class Maze
  void testbfs(Tester t) {
    this.init();

    t.checkExpect(maze.bfs(maze.board.get(0).get(0), maze.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));
  }

  // to test the method findPath in the class Maze
  void testFindPath(Tester t) {
    this.init();

    t.checkExpect(
        maze.findPath(maze.board.get(0).get(0), maze.board.get(2).get(1), new Stack<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));

    t.checkExpect(
        maze.findPath(maze.board.get(0).get(0), maze.board.get(2).get(1), new Queue<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(maze.board.get(0).get(0))));
  }
}