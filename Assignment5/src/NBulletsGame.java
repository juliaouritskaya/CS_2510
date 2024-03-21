import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

// to represent the N Bullets Game
class MyGame extends World {
  Color fontColor = Color.black;
  int bulletsLeft;
  int shipsHit;
  int spawnTime;
  Random random;
  ILoShip ships;
  ILoBullet bullets;

  // the main constructor
  MyGame(int bulletsLeft, int shipsHit, int spawnTime, Random random, ILoShip ships,
      ILoBullet bullets) {
    this.bulletsLeft = bulletsLeft;
    this.shipsHit = shipsHit;
    this.spawnTime = spawnTime;
    this.random = random;
    this.ships = ships;
    this.bullets = bullets;
  }

  // starting constructor that just takes an integer, which represents the number
  // of bullets a player has to shoot
  MyGame(int bullets) {
    this(bullets, 0, 0, new Random(), new MtLoShip(), new MtLoBullet());
  }

  // new constructor that creates a random number
  MyGame(int bulletsLeft, int shipsHit, int spawnTime, ILoShip ships, ILoBullet bullets) {
    this(bulletsLeft, shipsHit, spawnTime, new Random(), ships, bullets);
  }

  // returns the WorldScene to be shown on each clock tick
  public WorldScene makeScene() {
    return this.displayBulletsLeft(bullets.draw(ships.drawShips(this.getEmptyScene())));
  }

  // returns a WorldScene that displays how many bullets are left to shoot
  public WorldScene displayBulletsLeft(WorldScene scene) {
    TextImage bulletsLeft = new TextImage("Bullets Left: " + Integer.toString(this.bulletsLeft),
        fontColor);

    return scene.placeImageXY(bulletsLeft, 75, 275);
  }

  // spawns ships with some fixed frequency, and a non-zero, random amount of them
  // spawn at the same time
  public MyGame spawnShips() {
    int shipstoSpawn = this.random.nextInt(2) + 1;

    if (this.spawnTime < 28) {
      return new MyGame(this.bulletsLeft, this.shipsHit, this.spawnTime + 1, this.ships,
          this.bullets);
    }
    else {
      return new MyGame(this.bulletsLeft, this.shipsHit, 0, this.random,
          this.ships.spawnShips(shipstoSpawn, this.random), this.bullets);
    }
  }

  // handles ticking the clock and updating the world
  public MyGame onTick() {
    return this.move().remove().explode().spawnShips();
  }

  // moves the ships and bullets across the screen by returning a new x-position
  // for the ships and y-position for the bullets
  public MyGame move() {
    return new MyGame(this.bulletsLeft, this.shipsHit, this.spawnTime, this.random,
        this.ships.move(), this.bullets.move());
  }

  // removes ships and bullets from the game that have flown past the edge of the
  // screen
  public MyGame remove() {
    return new MyGame(this.bulletsLeft, this.shipsHit, this.spawnTime, this.random,
        this.ships.remove(), this.bullets.remove());
  }

  // explodes the ships when hit by a bullet and explodes bullet into many bullets
  public MyGame explode() {
    return new MyGame(this.bulletsLeft, this.shipsHit + this.ships.shipsHit(this.bullets),
        this.spawnTime, this.random, this.ships.explodeShips(this.bullets),
        this.bullets.explodeBullets(this.ships));
  }

  // fires a bullet from the center of the bottom of the screen when the user
  // presses the space bar (as long as there are bullets left to fire)
  public MyGame onKeyEvent(String key) {
    Bullet shootBullet = new Bullet(5, new Posn(0, -8), new Posn(250, 300), 0);

    if (key.equals(" ") && this.bulletsLeft > 0) {
      return new MyGame(this.bulletsLeft - 1, this.shipsHit, this.spawnTime, this.random,
          this.ships, new ConsLoBullet(shootBullet, this.bullets));
    }
    else {
      return this;
    }
  }

  // ends the game when there are no more bullets to fire and there are no bullets
  // left on the screen
  public WorldEnd worldEnds() {
    if (this.bulletsLeft <= 0 && this.bullets instanceof MtLoBullet) {
      return new WorldEnd(true, this.lastScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // returns the appropriate final scene showing total ships hit
  public WorldScene lastScene() {
    WorldImage gameOver = new TextImage("Game Over", 30, fontColor);
    WorldImage score = new TextImage("Score: " + Integer.toString(this.shipsHit) + " Ships Hit", 20,
        fontColor);

    return this.displayBulletsLeft(this.getEmptyScene().placeImageXY(score, 250, 150))
        .placeImageXY(gameOver, 250, 100);
  }
}

// to represent a ship
class Ship {
  int radius;
  Posn speed;
  Posn position;

  // the constructor
  Ship(int radius, Posn speed, Posn position) {
    this.radius = radius;
    this.speed = speed;
    this.position = position;
  }

  // draws the ship
  WorldScene drawShip(WorldScene scene) {
    CircleImage ship = new CircleImage(this.radius, OutlineMode.SOLID, Color.cyan);

    return this.position.drawHelper(scene, ship);
  }

  // moves the ship in the x-direction across the screen
  Ship move() {
    return new Ship(this.radius, this.speed, this.position.moveHelper(this.speed));
  }

  // checks whether the ship has been hit by the bullet
  boolean hasHit(Bullet bullet) {
    return this.position.distance(bullet.position, this.radius + bullet.radius);
  }

  // checks whether the ships has flown past the edge of the screen
  boolean pastEdge() {
    return this.position.pastEdge();
  }
}

// to represent a list of ships 
interface ILoShip {
  int RADIUS = 10;

  // draws the scene with a list of ships
  WorldScene drawShips(WorldScene scene);

  // spawns a list of ships with some fixed frequency, and a non-zero,
  // random amount of them spawn at the same time
  ILoShip spawnShips(int shipsLeft, Random rand);

  // moves a list of ships across the screen by returning a new x-position
  ILoShip move();

  // removes a list of ships from the game that have flown past the edge of the
  // screen
  ILoShip remove();

  // explodes a list of ships when hit by a bullet
  ILoShip explodeShips(ILoBullet bullets);

  // checks whether a list of ships has been hit by a bullet
  boolean hit(Bullet bullet);

  // returns the number of ships that have been hit by a bullet
  int shipsHit(ILoBullet bullets);
}

// to represent an empty list of ships
class MtLoShip implements ILoShip {
  MtLoShip() {
  }

  // draws the scene with an empty list of ships
  public WorldScene drawShips(WorldScene scene) {
    return scene;
  }

  // spawns an empty list of ships with some fixed frequency, and a non-zero,
  // random amount of them spawn at the same time
  public ILoShip spawnShips(int shipsLeft, Random rand) {
    int xSpeed;
    int xPosition;
    int yPosition;
    int direction = rand.nextInt(2);

    if (shipsLeft <= 0) {
      return this;
    }
    else {
      if (direction == 0) {
        xSpeed = -4;
        xPosition = 500;
      }
      else {
        xSpeed = 4;
        xPosition = 0;
      }
      yPosition = (int) ((rand.nextInt(250) + 10));
      Ship newShip = new Ship(RADIUS, new Posn(xSpeed, 0), new Posn(xPosition, yPosition));
      return new ConsLoShip(newShip, this).spawnShips(shipsLeft - 1, rand);
    }
  }

  // moves an empty list of ships across the screen by returning a new x-position
  public ILoShip move() {
    return this;
  }

  // removes an empty list of ships from the game that have flown past the edge of
  // the screen
  public ILoShip remove() {
    return this;
  }

  // explodes an empty list of ships when hit by a bullet
  public ILoShip explodeShips(ILoBullet bullets) {
    return this;
  }

  // checks whether an empty list of ships has been hit by a bullet
  public boolean hit(Bullet bullet) {
    return false;
  }

  // returns the number of ships that have been hit by a bullet
  public int shipsHit(ILoBullet bullets) {
    return 0;
  }
}

// to represent a non-empty list of ships 
class ConsLoShip implements ILoShip {
  Ship first;
  ILoShip rest;

  // the constructor
  ConsLoShip(Ship first, ILoShip rest) {
    this.first = first;
    this.rest = rest;
  }

  // draws the scene with an non-empty list of ships
  public WorldScene drawShips(WorldScene scene) {
    return this.rest.drawShips(this.first.drawShip(scene));
  }

  // spawns a non-empty list of ships with some fixed frequency, and a non-zero,
  // random amount of them spawn at the same time
  public ILoShip spawnShips(int shipsLeft, Random rand) {
    int xSpeed;
    int xPosition;
    int yPosition;
    int direction = rand.nextInt(2);

    if (shipsLeft < 0) {
      return this;
    }
    else {
      if (direction == 0) {
        xSpeed = -5;
        xPosition = 500;
      }
      else {
        xSpeed = 5;
        xPosition = 0;
      }
      yPosition = (int) ((rand.nextInt(300) + 1));
      Ship newShip = new Ship(10, new Posn(xSpeed, 0), new Posn(xPosition, yPosition));
      return new ConsLoShip(newShip, this).spawnShips(shipsLeft - 1, rand);
    }
  }

  // moves a non-empty list of ships across the screen by returning a new
  // x-position
  public ILoShip move() {
    return new ConsLoShip(this.first.move(), this.rest.move());
  }

  // removes a non-empty list of ships from the game that have flown past the edge
  // of the screen
  public ILoShip remove() {
    if (this.first.pastEdge()) {
      return this.rest.remove();
    }
    else {
      return new ConsLoShip(this.first, this.rest.remove());
    }
  }

  // explodes a non-empty list of ships when hit by a bullet
  public ILoShip explodeShips(ILoBullet bullets) {
    if (bullets.hit(this.first)) {
      return this.rest;
    }
    else {
      return new ConsLoShip(this.first, this.rest.explodeShips(bullets));
    }
  }

  // checks whether a non-empty list of ships has been hit by a bullet
  public boolean hit(Bullet bullet) {
    return this.first.hasHit(bullet) || this.rest.hit(bullet);
  }

  // returns the number of ships that have been hit by a bullet
  public int shipsHit(ILoBullet bullets) {
    if (bullets.hit(this.first)) {
      return 1 + this.rest.shipsHit(bullets);
    }
    else {
      return this.rest.shipsHit(bullets);
    }
  }
}

// to represent a position
class Posn {
  int x;
  int y;

  // the constructor
  Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // helps to move the ships and bullets by adding respective x- and y-positions
  // to the given position
  Posn moveHelper(Posn otherPosn) {
    return new Posn(this.x + otherPosn.x, this.y + otherPosn.y);
  }

  // checks whether the ships or bullet have flown past the edge of the screen
  boolean pastEdge() {
    return this.x > 500 || this.x < 0 || this.y > 300 || this.y < 0;
  }

  // checks whether the distance between the given position is close to this
  // position
  boolean distance(Posn posn, int distance) {
    return (int) (Math.hypot((double) (this.x - posn.x), (double) (this.y - posn.y))) <= distance;
  }

  // helps to draw the ship by placing the image at given x- and y-positions
  WorldScene drawHelper(WorldScene scene, WorldImage image) {
    return scene.placeImageXY(image, this.x, this.y);
  }
}

// to represent a bullet 
class Bullet {
  int radius;
  Posn speed;
  Posn position;
  int hits;

  // the constructor
  Bullet(int radius, Posn speed, Posn position, int hits) {
    if (this.radius > 10) {
      this.radius = 10;
    }
    else {
      this.radius = radius;
    }

    this.speed = speed;
    this.position = position;
    this.hits = hits;
  }

  // draws the bullet
  public WorldScene drawBullet(WorldScene scene) {
    CircleImage bullet = new CircleImage(this.radius, OutlineMode.SOLID, Color.pink);

    return this.position.drawHelper(scene, bullet);
  }

  // moves the bullet in the y-direction across the screen
  Bullet move() {
    return new Bullet(this.radius, this.speed, this.position.moveHelper(this.speed), this.hits);
  }

  // checks whether the bullet has flown past the edge of the screen
  boolean pastEdge() {
    return this.position.pastEdge();
  }

  // checks whether the bullet has hit a ship
  boolean hitShip(Ship ship) {
    return this.position.distance(ship.position, this.radius + ship.radius);
  }

  // explodes a bullet into many bullets when it collides with a ship
  ILoBullet explodeBullet(ILoBullet bullets) {
    int manyBullets = this.hits + 2;
    double angle = Math.PI / manyBullets;

    return explodeBulletHelper(bullets, manyBullets, angle);
  }

  // helps to explode a bullet by computing the angle between the destroyed bullet
  ILoBullet explodeBulletHelper(ILoBullet bullets, int manyBullets, double angle) {
    int newRadius = this.radius + (this.hits + 1) * 2;
    Posn newSpeed = new Posn((int) (this.radius * Math.cos(angle * manyBullets)),
        (int) (this.radius * -Math.sin(angle * manyBullets)));
    Bullet newBullet = new Bullet(newRadius, newSpeed, this.position, this.hits + 1);

    if (manyBullets <= 0) {
      return bullets;
    }
    else {
      return explodeBulletHelper(new ConsLoBullet(newBullet, bullets), manyBullets - 1, angle);
    }
  }
}

//to represent a list of bullets 
interface ILoBullet {
  // draws the scene with a list of bullets
  WorldScene draw(WorldScene scene);

  // moves a list of bullets across the screen by returning a new y-position
  ILoBullet move();

  // removes a list of bullets from the game that have flown past the edge of the
  // screen
  ILoBullet remove();

  // explodes a list of bullets when they hit a ship
  ILoBullet explodeBullets(ILoShip ships);

  // checks whether a list of bullets has hit a ship
  boolean hit(Ship ship);
}

//to represent an empty list of bullets
class MtLoBullet implements ILoBullet {
  MtLoBullet() {
  }

  // draws the scene with an empty-list of bullets
  public WorldScene draw(WorldScene scene) {
    return scene;
  }

  // moves an empty list of bullets across the screen by returning a new
  // y-position
  public ILoBullet move() {
    return this;
  }

  // removes an empty list of bullets from the game that have flown past the
  // edge of the screen
  public ILoBullet remove() {
    return this;
  }

  // explodes an empty-list of bullets when they hit a ship
  public ILoBullet explodeBullets(ILoShip ships) {
    return this;
  }

  // checks whether a non-empty list of bullets has hit a ship
  public boolean hit(Ship ship) {
    return false;
  }
}

// to represent a non-empty list of bullets 
class ConsLoBullet implements ILoBullet {
  Bullet first;
  ILoBullet rest;

  // the constructor
  ConsLoBullet(Bullet first, ILoBullet rest) {
    this.first = first;
    this.rest = rest;
  }

  // draws the scene with a non-empty list of bullets
  public WorldScene draw(WorldScene scene) {
    return this.rest.draw(this.first.drawBullet(scene));
  }

  // moves a non-empty list of bullets across the screen by returning a new
  // y-position
  public ILoBullet move() {
    return new ConsLoBullet(this.first.move(), this.rest.move());
  }

  // removes a non-empty list of bullets from the game that have flown past the
  // edge of the screen
  public ILoBullet remove() {
    if (this.first.pastEdge()) {
      return this.rest.remove();
    }
    else {
      return new ConsLoBullet(this.first, this.rest.remove());
    }
  }

  // explodes a non-empty list of bullets when they hit a ship
  public ILoBullet explodeBullets(ILoShip ships) {
    if (ships.hit(this.first)) {
      return this.first.explodeBullet(this.rest.explodeBullets(ships));
    }
    else {
      return new ConsLoBullet(this.first, this.rest.explodeBullets(ships));
    }
  }

  // checks whether a non-empty list of bullets has hit a ship
  public boolean hit(Ship ship) {
    return this.first.hitShip(ship) || this.rest.hit(ship);
  }
}

// to represents examples and tests for the game 
class ExamplesGame {
  Random random = new Random();
  WorldScene scene = new WorldScene(500, 300);

  CircleImage ship = new CircleImage(10, OutlineMode.SOLID, Color.cyan);
  CircleImage bullet = new CircleImage(10, OutlineMode.SOLID, Color.pink);
  TextImage bulletsLeft = new TextImage("Bullets Left: 2", Color.black);

  Posn position1 = new Posn(32, 46);
  Posn position2 = new Posn(10, 20);
  Posn position3 = new Posn(100, 250);
  Posn position4 = new Posn(250, 300);

  Posn vx1 = new Posn(6, 0);
  Posn vx2 = new Posn(8, 0);
  Posn vx3 = new Posn(2, 0);

  Posn vy1 = new Posn(0, 6);
  Posn vy2 = new Posn(0, 8);
  Posn vy3 = new Posn(0, 2);

  Ship ship1 = new Ship(30, this.vx1, this.position1);
  Ship ship2 = new Ship(20, this.vx2, this.position2);
  Ship ship3 = new Ship(10, this.vx3, this.position3);

  ILoShip mtLoShip = new MtLoShip();
  ILoShip loShip1 = new ConsLoShip(this.ship1, this.mtLoShip);
  ILoShip loShip2 = new ConsLoShip(this.ship1, this.loShip1);
  ILoShip loShip3 = new ConsLoShip(this.ship3, this.loShip2);

  Bullet bullet1 = new Bullet(10, this.vy1, this.position4, 0);
  Bullet bullet2 = new Bullet(10, this.vy2, this.position4, 0);
  Bullet bullet3 = new Bullet(10, this.vy3, this.position4, 0);

  ILoBullet mtLoBullet = new MtLoBullet();
  ILoBullet loBullet1 = new ConsLoBullet(this.bullet1, this.mtLoBullet);
  ILoBullet loBullet2 = new ConsLoBullet(this.bullet2, this.loBullet1);
  ILoBullet loBullet3 = new ConsLoBullet(this.bullet3, this.loBullet2);

  MyGame mygame1 = new MyGame(10);
  MyGame mygame2 = new MyGame(0, 10, 20, this.random, this.loShip1, this.loBullet1);
  MyGame mygame3 = new MyGame(1, 3, 5, this.random, this.loShip2, this.loBullet2);
  MyGame mygame4 = new MyGame(2, 4, 6, this.loShip3, this.loBullet3);

  // to test the method bigBang on the interface MyGame
  boolean testBigBang(Tester t) {
    MyGame w = new MyGame(10);
    int gameWidth = 500;
    int gameHeight = 300;
    double tickRate = 1.0 / 28.0;
    return w.bigBang(gameWidth, gameHeight, tickRate);
  }

  // to test the method makeScene on the interface
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.mygame4.makeScene(),
        new WorldScene(0, 0).placeImageXY(this.bullet, 250, 300).placeImageXY(this.ship, 500, 200)
            .placeImageXY(this.bulletsLeft, 75, 275));
  }

  // to test the method displayBulletsLeft on the interface
  boolean testDisplayBulletsLeft(Tester t) {
    return t.checkExpect(this.mygame4.displayBulletsLeft(this.scene),
        this.scene.placeImageXY(this.bulletsLeft, 75, 275));
  }

  // to test the method spawnShips on the interface
  boolean testSpawnShips(Tester t) {
    return t.checkExpect(this.mygame4.spawnShips(),
        new MyGame(2, 4, 7, this.random, this.loShip3, this.loBullet3))
        && t.checkExpect(this.mygame3.spawnShips(),
            new MyGame(1, 3, 6, this.random, this.loShip2, this.loBullet2));
  }

  // to test the method onTick on the interface
  boolean testOnTick(Tester t) {
    return t.checkExpect(this.mygame1.onTick(),
        new MyGame(10, 0, 1, this.random, this.mtLoShip, this.mtLoBullet));
  }

  // to test the method move on the interface
  boolean testMove(Tester t) {
    return t.checkExpect(this.ship1.move(), new Ship(30, new Posn(6, 0), new Posn(38, 46)))
        && t.checkExpect(this.bullet2.move(), new Bullet(10, new Posn(0, 8), new Posn(250, 308), 0))
        && t.checkExpect(this.mtLoBullet.move(), this.mtLoBullet) && t.checkExpect(
            this.mygame1.move(), new MyGame(10, 0, 0, this.random, this.mtLoShip, this.mtLoBullet));
  }

  // to test the method remove on the interface
  boolean testRemove(Tester t) {
    return t.checkExpect(this.loBullet1.remove(),
        new ConsLoBullet(new Bullet(10, new Posn(0, 6), new Posn(250, 300), 0), this.mtLoBullet))
        && t.checkExpect(this.loShip1.remove(),
            new ConsLoShip(new Ship(30, new Posn(6, 0), new Posn(32, 46)), this.mtLoShip))
        && t.checkExpect(this.mtLoShip.remove(), this.mtLoShip)
        && t.checkExpect(this.mygame1.remove(),
            new MyGame(10, 0, 0, this.random, this.mtLoShip, this.mtLoBullet));
  }

  // tests the explode method on the interface
  boolean testExplode(Tester t) {
    return t.checkExpect(this.mygame1.explode(), mygame1)
        && t.checkExpect(this.mygame2.explode(), mygame2);
  }

  // tests on Key Event method on the interface
  boolean testOnKey(Tester t) {
    return t.checkExpect(this.mygame2.onKeyEvent(" "), new MyGame(0, 10, 20, new Random(),
        new ConsLoShip(new Ship(30, new Posn(6, 0), new Posn(32, 46)), this.mtLoShip),
        new ConsLoBullet(new Bullet(10, new Posn(0, 6), new Posn(250, 300), 0), this.mtLoBullet)));
  }

  // test World Ends method on the interface
  boolean testWorldEnds(Tester t) {
    return t.checkExpect(mygame2.worldEnds(), new WorldEnd(false, this.mygame2.makeScene()))
        && t.checkExpect(mygame1.worldEnds(), new WorldEnd(false, this.mygame1.makeScene()));
  }

  // tests last scene method on the interface
  boolean testLastScene(Tester t) {
    return t.checkExpect(this.mygame3.lastScene(),
        new WorldScene(0, 0).placeImageXY(new TextImage("Game Over", 20, Color.BLACK), 250, 150)
            .placeImageXY(new TextImage("Total Ships Hit: 4", Color.BLACK), 250, 100));

  }

  // test the method drawBullet
  boolean testDrawBullet(Tester t) {
    return t.checkExpect(this.bullet1.drawBullet(new WorldScene(0, 0)),
        this.position1.drawHelper(new WorldScene(0, 0), new CircleImage(2, "solid", Color.PINK)))
        && t.checkExpect(this.bullet2.drawBullet(new WorldScene(0, 0)), this.position2
            .drawHelper(new WorldScene(0, 0), new CircleImage(10, "solid", Color.PINK)));
  }

  // test the method drawShip
  boolean testDrawShip(Tester t) {
    return t.checkExpect(ship1.drawShip(new WorldScene(0, 0)),
        this.position2.drawHelper(new WorldScene(0, 0), new CircleImage(10, "solid", Color.cyan)))
        && t.checkExpect(ship3.drawShip(new WorldScene(0, 0)), this.position4
            .drawHelper(new WorldScene(0, 0), new CircleImage(20, "solid", Color.CYAN)));
  }

  // test the method spawnShip
  boolean testSpawnShip(Tester t) {
    return t.checkExpect(this.mygame1.spawnShips(),
        new MyGame(10, 0, 1, this.random, this.mtLoShip, this.mtLoBullet))
        && t.checkExpect(this.mygame2.spawnShips(),
            new MyGame(0, 10, 21, this.random,
                new ConsLoShip(new Ship(30, new Posn(6, 0), new Posn(32, 46)), this.mtLoShip),
                new ConsLoBullet(new Bullet(10, new Posn(0, 6), new Posn(250, 300), 0),
                    this.mtLoBullet)));
  }

  // test the method explodeShips
  boolean testExplodeShips(Tester t) {
    return t.checkExpect(this.loShip2.explodeShips(this.loBullet1), this.loShip2)
        && t.checkExpect(this.mtLoShip.explodeShips(this.loBullet1), this.mtLoShip);
  }

  // test the method shipsHit
  boolean testShipsHit(Tester t) {
    return t.checkExpect(this.mtLoShip.shipsHit(loBullet1), 0)
        && t.checkExpect(this.loShip3.shipsHit(loBullet3), 0);
  }

  // tests draw Helper in the ships class
  boolean testDrawHelper(Tester t) {
    return t.checkExpect(this.position1.drawHelper(new WorldScene(500, 300), this.bullet),
        new WorldScene(500, 300).placeImageXY(this.bullet, 32, 46));
  }

  // test the method explodeBullets
  boolean testExplodeBullets(Tester t) {
    return t.checkExpect(loBullet1.explodeBullets(loShip1), loBullet1)
        && t.checkExpect(loBullet1.explodeBullets(mtLoShip), loBullet1)
        && t.checkExpect(mtLoBullet.explodeBullets(loShip1), mtLoBullet)
        && t.checkExpect(mtLoBullet.explodeBullets(mtLoShip), mtLoBullet);
  }

  // test the method hitShip
  boolean testHitShip(Tester t) {
    return t.checkExpect(loBullet1.hit(ship1), false) && t.checkExpect(loBullet2.hit(ship1), false)
        && t.checkExpect(mtLoBullet.hit(ship3), false);
  }

  // test the method moveHelper
  boolean testMoveHelper(Tester t) {
    return t.checkExpect(position1.moveHelper(position1), new Posn(64, 92))
        && t.checkExpect(position2.moveHelper(position4), new Posn(260, 320));
  }

  // test the method pastEdge
  boolean testPastEdge(Tester t) {
    return t.checkExpect(position1.pastEdge(), false)
        && t.checkExpect(new Posn(320, 530).pastEdge(), true);
  }

  // test the method distance
  boolean testDistance(Tester t) {
    return t.checkExpect(position1.distance(position2, 20), false)
        && t.checkExpect(position3.distance(position4, 10), false);
  }

  // test the method hit
  boolean testHit(Tester t) {
    return t.checkExpect(this.loShip1.hit(this.bullet1), false)
        && t.checkExpect(this.loShip2.hit(this.bullet3), false);
  }

}