import tester.Tester;

// to represent a picture 
interface IPicture {

  // Computes the overall width of this picture
  int getWidth();

  // Computes the number of single shapes involved in producing the final image
  int countShapes();

  // Computes how deeply operations are nested in the construction of this picture
  int comboDepth();

  // Mirrors the image
  IPicture mirror();

  // Produces a String representing the contents of this picture,
  // where the recipe for the picture has expanded only the given times
  String pictureRecipe(int depth);
}

// to represent a single shape
class Shape implements IPicture {
  String kind; // "circle" or "square"
  int size; // height == width

  // the constructor
  Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }

  /* Template:
   * Fields:
   * this.kind -- String
   * this.size -- int
   * 
   * Methods: 
   * this.getWidth() -- int
   * this.countShapes() -- int
   * this.comboDepth() -- int
   * this.mirror() -- IPicture
   * this.pictureRecipe() -- String
   */

  // Computes the overall width of this shape
  public int getWidth() {
    return this.size;
  }

  // Computes the number of single shapes
  public int countShapes() {
    return 1;
  }

  // Computes how deeply operations are nested in the construction of this shape
  public int comboDepth() {
    return 0;
  }

  // Mirrors the shape
  public IPicture mirror() {
    return this;
  }

  // Produces a String representing the contents of this shape,
  // where the recipe for the picture has expanded only the given times
  public String pictureRecipe(int depth) {
    return this.kind;
  }
}

// to represent a combo that connects one or more pictures
class Combo implements IPicture {
  String name;
  IOperation operation;

  // the constructor
  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }

  /* Template: 
   * Fields:
   * this.name -- String
   * this.operation -- IOperation 
   * 
   * Methods: 
   * this.getWidth() -- int
   * this.countShapes() -- int
   * this.comboDepth() -- int
   * this.mirror() -- IPicture
   * this.pictureRecipe() -- String
   * 
   * Methods for fields:
   * this.operation.getWidthOp() -- int
   * this.operation.countShapesOp() -- int
   * this.operation.comboDepthOp() -- int
   * this.operation.mirrorOp() -- IPicture
   * this.operation.pictureRecipeOp() -- String
   */

  // Computes the overall width of this combo
  public int getWidth() {
    return this.operation.getWidthOp();
  }

  // Computes the number of single shapes involved in producing the combo
  public int countShapes() {
    return this.operation.countShapesOp();
  }

  // Computes how deeply operations are nested in the construction of this combo
  public int comboDepth() {
    return this.operation.comboDepthOp();
  }

  // Mirrors the combo image
  public IPicture mirror() {
    return new Combo(this.name, this.operation.mirrorOp());
  }

  // Produces a String representing the contents of this combo,
  // where the recipe for the picture has expanded only the given times
  public String pictureRecipe(int depth) {
    /* Template:
     * Parameters:
     * depth -- int
     */
    if (depth <= 0) {
      return this.name;
    }
    else {
      return this.operation.pictureRecipeOp(depth - 1);
    }
  }
}

// to represent an operation
interface IOperation {

  // Computes the overall width of the picture after operation
  int getWidthOp();

  // Computes the number of single shapes after operation
  int countShapesOp();

  // Computes how deeply operations are nested in the construction of the picture
  int comboDepthOp();

  // Mirrors the image after operation
  IOperation mirrorOp();

  // Produces a String representing the contents of this picture after the
  // operation,
  // where the recipe for the picture has expanded only the given times
  String pictureRecipeOp(int depth);
}

// to represent a scale operation
// takes a single picture and draws it twice as large
class Scale implements IOperation {
  IPicture picture;

  // the constructor
  Scale(IPicture picture) {
    this.picture = picture;
  }

  /* Template:
   * Fields:
   * this.picture -- IPicture
   * 
   * Methods:
   * this.getWidthOp() -- int
   * this.countShapesOp() -- int
   * this.comboDepthOp() -- int
   * this.mirrorOp() -- IOperation
   * this.pictureRecipeOp(int) -- String
   * 
   * Methods for fields:
   * this.picture.getWidth() -- int
   * this.picture.countShapes() -- int
   * this.picture.comboDepth() -- int
   * this.picture.mirror() -- IPicture
   * this.picture.pictureRecipe(int) -- String
   */

  // Computes the overall width of the picture after scale
  public int getWidthOp() {
    return this.picture.getWidth() * 2;
  }

  // Computes the number of single shapes after scale operation
  public int countShapesOp() {
    return this.picture.countShapes();
  }

  // Computes how deeply operations are nested after the scale operation
  public int comboDepthOp() {
    return 1 + this.picture.comboDepth();
  }

  // Mirrors the image after scale operation, leaving the image unchanged
  public IOperation mirrorOp() {
    return new Scale(this.picture.mirror());
  }

  // Produces a String representing the contents of this picture after the scale
  // operation,
  // where the recipe for the picture has expanded only the given times
  public String pictureRecipeOp(int depth) {
    /* Template:
     * Parameters:
     * depth -- int
     */
    return "scale(" + this.picture.pictureRecipe(depth) + ")";
  }
}

// to represent a beside operation
// takes two pictures, and draws picture1 to the left of picture2
class Beside implements IOperation {
  IPicture picture1;
  IPicture picture2;

  // the constructor
  Beside(IPicture picture1, IPicture picture2) {
    this.picture1 = picture1;
    this.picture2 = picture2;
  }

  /* Template:
   * Fields:
   * this.picture1 -- IPicture
   * this.picture2 -- IPicture
   * 
   * Methods:
   * this.getWidthOp() -- int
   * this.countShapesOp() -- int
   * this.comboDepthOp() -- int
   * this.mirrorOp() -- IOperation
   * this.pictureRecipeOp() -- String
   * 
   * Methods for fields:
   * this.picture1.getWidth() -- int
   * this.picture1.countShapes() -- int
   * this.picture1.comboDepth() -- int
   * this.picture1.mirror() -- IPicture
   * this.picture1.pictureRecipe() -- String
   * this.picture2.getWidth() -- int
   * this.picture2.countShapes() -- int
   * this.picture2.comboDepth() -- int
   * this.picture2.mirror() -- IPicture
   * this.picure2.pitureRecipe() -- String
   */

  // Computes the overall width of the picture after beside
  public int getWidthOp() {
    return this.picture1.getWidth() + this.picture2.getWidth();
  }

  // Computes the number of single shapes after beside operation
  public int countShapesOp() {
    return this.picture1.countShapes() + this.picture2.countShapes();
  }

  // Computes how deeply operations are nested after the beside operation
  // 1 + max(p1 depth, p2 depth)
  public int comboDepthOp() {
    if (this.picture1.comboDepth() > this.picture2.comboDepth()) {
      return 1 + this.picture1.comboDepth();
    }
    else {
      return 1 + this.picture2.comboDepth();
    }
  }

  // Mirrors the image after beside operation, which
  // flips the two sub-images
  public IOperation mirrorOp() {
    return new Beside(this.picture2.mirror(), this.picture1.mirror());
  }

  // Produces a String representing the contents of this picture after the beside
  // operation, where the recipe for the picture has expanded only the given times
  public String pictureRecipeOp(int depth) {
    /* Template:
     * Parameters:
     * depth -- int
     */
    return "beside(" + this.picture1.pictureRecipe(depth) + ", "
        + this.picture2.pictureRecipe(depth) + ")";
  }
}

// to represent an overlay operation
// takes two pictures, and draws top-picture on top of
// bottom-picture, with their centers aligned
class Overlay implements IOperation {
  IPicture topPicture;
  IPicture bottomPicture;

  // the constructor
  Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }

  /* Template:
   * Fields:
   * this.topPicture -- IPicture
   * this.bottomPicture -- IPicture 
   * 
   * Methods:
   * this.getWidthOp() -- int
   * this.countShapesOp() -- int
   * this.comboDepthOp() -- int
   * this.mirrorOp() -- IOperation
   * this.pictureRecipeOp() -- String
   * 
   * Methods for fields:
   * this.topPicture.getWidth() -- int
   * this.topPicture.countShapes() -- int
   * this.topPicture.comboDepth() -- int
   * this.topPicture.mirror() -- IPicture
   * this.topPicture.pictureRecipe() -- String
   * this.bottomPicture.getWidth() -- int
   * this.bottomPicture.countShapes() -- int
   * this.bottomPicture.comboDepth() -- int
   * this.bottomPicture.mirror() -- IPicture
   * this.bottomPicture.pictureRecipe() -- String
   */

  // Computes the overall width of the picture after overlay
  public int getWidthOp() {
    if (this.topPicture.getWidth() > this.bottomPicture.getWidth()) {
      return this.topPicture.getWidth();
    }
    else {
      return this.bottomPicture.getWidth();
    }
  }

  // Computes the number of single shapes after overlay operation
  public int countShapesOp() {
    return this.topPicture.countShapes() + this.bottomPicture.countShapes();
  }

  // Computes how deeply operations are nested after the overlay operation
  public int comboDepthOp() {
    if (this.topPicture.comboDepth() > this.bottomPicture.comboDepth()) {
      return 1 + this.topPicture.comboDepth();
    }
    else {
      return 1 + this.bottomPicture.comboDepth();
    }
  }

  // Mirrors the image after overlay operation, leaving the image unchanged
  public IOperation mirrorOp() {
    return new Overlay(this.topPicture.mirror(), this.bottomPicture.mirror());
  }

  // Produces a String representing the contents of this picture after the overlay
  // operation, where the recipe for the picture has expanded only the given times
  public String pictureRecipeOp(int depth) {
    /* Template:
     * Parameters:
     * depth -- int
     */
    return "overlay(" + this.topPicture.pictureRecipe(depth) + ", "
        + this.bottomPicture.pictureRecipe(depth) + ")";
  }
}

// to represent example and tests for pictures 
class ExamplesPicture {
  ExamplesPicture() {
  }

  // examples of pictures
  IPicture circle = new Shape("circle", 20);
  IPicture square = new Shape("square", 30);

  IPicture bigCircle = new Combo("big circle", new Scale(this.circle));
  IPicture squareOnCircle = new Combo("square on circle", new Overlay(this.square, this.bigCircle));
  IPicture doubledSquareOnCircle = new Combo("doubled square on circle",
      new Beside(this.squareOnCircle, this.squareOnCircle));

  IPicture biggerCircle = new Combo("bigger circle", new Scale(this.bigCircle));
  IPicture bigSquare = new Combo("bigger square", new Scale(this.square));
  IPicture circleOnSquare = new Combo("circle on square", new Overlay(this.circle, this.square));
  IPicture quadrupledSquareonCircle = new Combo("doubled circle on square",
      new Beside(this.doubledSquareOnCircle, this.doubledSquareOnCircle));
  IPicture circleBySquare = new Combo("circle by square",
      new Beside(this.bigCircle, this.bigSquare));

  // test the method getWidthOp on the interface IOperation
  boolean testGetWidthOp(Tester t) {
    return t.checkExpect(new Scale(this.circle).getWidthOp(), 40)
        && t.checkExpect(new Beside(this.squareOnCircle, this.squareOnCircle).getWidthOp(), 80)
        && t.checkExpect(new Overlay(this.square, this.bigCircle).getWidthOp(), 40)
        && t.checkExpect(new Overlay(this.bigCircle, this.square).getWidthOp(), 40);
  }

  // test the method getWidth on the interface IPicture
  boolean testGetWidth(Tester t) {
    return t.checkExpect(this.circle.getWidth(), 20) && t.checkExpect(this.square.getWidth(), 30)
        && t.checkExpect(this.bigCircle.getWidth(), 40)
        && t.checkExpect(this.squareOnCircle.getWidth(), 40)
        && t.checkExpect(this.doubledSquareOnCircle.getWidth(), 80)
        && t.checkExpect(this.biggerCircle.getWidth(), 80)
        && t.checkExpect(this.circleOnSquare.getWidth(), 30)
        && t.checkExpect(this.quadrupledSquareonCircle.getWidth(), 160);
  }

  // test the method countShapesOp on the interface IOperation
  boolean testCountShapesOp(Tester t) {
    return t.checkExpect(new Scale(this.circle).countShapesOp(), 1)
        && t.checkExpect(new Beside(this.squareOnCircle, this.squareOnCircle).countShapesOp(), 4)
        && t.checkExpect(new Overlay(this.square, this.bigCircle).countShapesOp(), 2);
  }

  // test the method countShapes on the interface IPicture
  boolean testCountShapes(Tester t) {
    return t.checkExpect(this.circle.countShapes(), 1)
        && t.checkExpect(this.square.countShapes(), 1)
        && t.checkExpect(this.bigCircle.countShapes(), 1)
        && t.checkExpect(this.squareOnCircle.countShapes(), 2)
        && t.checkExpect(this.doubledSquareOnCircle.countShapes(), 4)
        && t.checkExpect(this.biggerCircle.countShapes(), 1)
        && t.checkExpect(this.circleOnSquare.countShapes(), 2)
        && t.checkExpect(this.quadrupledSquareonCircle.countShapes(), 8);
  }

  // test the method comboDepthOp on the interface IOperation
  boolean testComboDepthOp(Tester t) {
    return t.checkExpect(new Scale(this.circle).comboDepthOp(), 1)
        && t.checkExpect(new Beside(this.squareOnCircle, this.squareOnCircle).comboDepthOp(), 3)
        && t.checkExpect(new Beside(this.circleBySquare, this.bigSquare).comboDepthOp(), 3)
        && t.checkExpect(new Beside(this.bigSquare, this.circleBySquare).comboDepthOp(), 3)
        && t.checkExpect(new Overlay(this.square, this.bigCircle).comboDepthOp(), 2)
        && t.checkExpect(new Overlay(this.bigCircle, this.square).comboDepthOp(), 2);
  }

  // test the method comboDepth on the interface IPicture
  boolean testComboDepth(Tester t) {
    return t.checkExpect(this.circle.comboDepth(), 0) && t.checkExpect(this.square.comboDepth(), 0)
        && t.checkExpect(this.bigCircle.comboDepth(), 1)
        && t.checkExpect(this.squareOnCircle.comboDepth(), 2)
        && t.checkExpect(this.doubledSquareOnCircle.comboDepth(), 3)
        && t.checkExpect(this.biggerCircle.comboDepth(), 2)
        && t.checkExpect(this.circleOnSquare.comboDepth(), 1)
        && t.checkExpect(this.quadrupledSquareonCircle.comboDepth(), 4)
        && t.checkExpect(this.circleBySquare.comboDepth(), 2);
  }

  // test the method mirrorOp on the interface IOperation
  boolean testMirrorOp(Tester t) {
    return t.checkExpect(new Scale(this.circle).mirrorOp(), new Scale(this.circle).mirrorOp())
        && t.checkExpect(new Beside(this.circle, this.square).mirrorOp(),
            new Beside(this.square, this.circle))
        && t.checkExpect(new Overlay(this.square, this.bigCircle).mirrorOp(),
            new Overlay(this.square, this.bigCircle));
  }

  // test the method mirror on the interface IPicture
  boolean testMirror(Tester t) {
    return t.checkExpect(this.circle.mirror(), this.circle)
        && t.checkExpect(this.bigCircle.mirror(), this.bigCircle)
        && t.checkExpect(this.squareOnCircle.mirror(), this.squareOnCircle)
        && t.checkExpect(this.doubledSquareOnCircle.mirror(), this.doubledSquareOnCircle)
        && t.checkExpect(this.circleBySquare.mirror(),
            new Combo("circle by square", new Beside(this.bigSquare, this.bigCircle)));
  }

  // test the method pictureRecipeOp on the interface IOperation
  boolean testPictureRecipeOp(Tester t) {
    return t.checkExpect(new Scale(this.circle).pictureRecipeOp(0), "scale(circle)")
        && t.checkExpect(new Beside(this.circle, this.square).pictureRecipeOp(0),
            "beside(circle, square)")
        && t.checkExpect(new Overlay(this.circle, this.square).pictureRecipeOp(0),
            "overlay(circle, square)")
        && t.checkExpect(new Scale(this.bigCircle).pictureRecipeOp(0), "scale(big circle)");
  }

  // test the method pictureRecipe on the interface IOperation
  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(0), "doubled square on circle")
        && t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(1),
            "beside(square on circle, square on circle)")
        && t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(2),
            "beside(overlay(square, big circle), overlay(square, big circle))")
        && t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(3),
            "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))");
  }
}