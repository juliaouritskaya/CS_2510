import tester.*;
import java.util.function.Function;
import java.util.function.BiFunction;

// to represent an arithmetic expression 
interface IArith {
  <R> R accept(IArithVisitor<R> visitor);
}

// to represent a constant 
class Const implements IArith {
  double num;

  // the constructor
  Const(double num) {
    this.num = num;
  }

  // to return the result of applying the given visitor to this constant
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConstant(this);
  }
}

// to represent a unary formula to be applied to its respective operand: child
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  // the constructor
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  // to return the result of applying the given visitor to this unary formula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnary(this);
  }
}

// to represent a binary formula to be applied to its respective operative: left, right 
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  // the constructor
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  // to return the result of applying the given visitor to this binary formula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinary(this);
  }
}

//to represent negation on a unary formula
class Neg implements Function<Double, Double> {

  // negates the given number
  public Double apply(Double num) {
    return num * -1;
  }
}

// to represent squaring on a unary formula
class Sqr implements Function<Double, Double> {

  // squares the given number
  public Double apply(Double num) {
    return num * num;
  }
}

// to represent addition on a binary formula
class Plus implements BiFunction<Double, Double, Double> {

  // adds the two given numbers
  public Double apply(Double num1, Double num2) {
    return num1 + num2;
  }
}

// to represent addition on a binary formula
class Minus implements BiFunction<Double, Double, Double> {

  // subtracts the second given number from the first
  public Double apply(Double num1, Double num2) {
    return num1 - num2;
  }
}

// to represent addition on a binary formula
class Mul implements BiFunction<Double, Double, Double> {

  // multiplies the two given numbers
  public Double apply(Double num1, Double num2) {
    return num1 * num2;
  }
}

// to represent addition on a binary formula
class Div implements BiFunction<Double, Double, Double> {

  // divides the first given number by the second
  public Double apply(Double num1, Double num2) {
    return num1 / num2;
  }
}

// represents a visitor that visits an IArith and produces a result of type R
interface IArithVisitor<R> extends Function<IArith, R> {
  // delegates the visitor methods to the appropriate IArith
  R apply(IArith iObj);

  // visits a Constant and produces a result of type R
  R visitConstant(Const constant);

  // visits a UnaryFormula and produces a result of type R
  R visitUnary(UnaryFormula unary);

  // visits a BinaryFormula and produces a result of type R
  R visitBinary(BinaryFormula binary);
}

// visits an IArith and evaluates the tree to a Double answer 
class EvalVisitor implements IArithVisitor<Double> {

  // delegates the visitor methods to the appropriate IArith
  public Double apply(IArith iObj) {
    return iObj.accept(this);
  }

  // evaluates the Constant to a Double answer
  public Double visitConstant(Const constant) {
    return constant.num;
  }

  // evaluates the UnaryFormula to a DoubleAnswer based on an arithmetic
  // expression
  public Double visitUnary(UnaryFormula unary) {
    return unary.func.apply(unary.child.accept(this));
  }

  // evaluates the BinaryFormula to a DoubleAnswer based on an arithmetic
  // expression
  public Double visitBinary(BinaryFormula binary) {
    return binary.func.apply(binary.left.accept(this), binary.right.accept(this));
  }
}

// visits an IArith and produces a String showing 
// the fully-parenthesized expression in Racket-like prefix notation
class PrintVisitor implements IArithVisitor<String> {

  // delegates the visitor methods to the appropriate IArith
  public String apply(IArith iObj) {
    return iObj.accept(this);
  }

  // evaluates the Constant to a String expression
  public String visitConstant(Const constant) {
    return Double.toString(constant.num);
  }

  // evaluates the UnaryFormula to a fully-parenthesized String expression
  public String visitUnary(UnaryFormula unary) {
    return "(" + unary.name + " " + unary.child.accept(this) + ")";
  }

  // evaluates the BinaryFormula to a fully-parenthesized String expression
  public String visitBinary(BinaryFormula binary) {
    return "(" + binary.name + " " + binary.left.accept(this) + " " + binary.right.accept(this)
        + ")";
  }
}

// visits an IArith and produces another IArith, where every Const in the tree has been doubled 
class DoublerVisitor implements IArithVisitor<IArith> {

  // delegates the visitor methods to the appropriate IArith
  public IArith apply(IArith iObj) {
    return iObj.accept(this);
  }

  // produces another IArith where the Constant is doubled
  public IArith visitConstant(Const constant) {
    return new Const(constant.num * 2);
  }

  // produces another IArith where the Constant is doubled
  public IArith visitUnary(UnaryFormula unary) {
    return new UnaryFormula(unary.func, unary.name, unary.child.accept(this));
  }

  // produces another IArith where the Constant is doubled
  public IArith visitBinary(BinaryFormula binary) {
    return new BinaryFormula(binary.func, binary.name, binary.left.accept(this),
        binary.right.accept(this));
  }
}

// visits an IArith and produces a Boolean that is true,
// if a negative number is never encountered at any point during its evaluation
class NoNegativeResults implements IArithVisitor<Boolean> {

  // delegates the visitor methods to the appropriate IArith
  public Boolean apply(IArith iObj) {
    return iObj.accept(this);
  }

  // returns true if a negative number is never encountered at any point during
  // its evaluation
  public Boolean visitConstant(Const constant) {
    return constant.num >= 0;
  }

  // returns true if a negative number is never encountered at any point during
  // its evaluation
  public Boolean visitUnary(UnaryFormula unary) {
    return unary.child.accept(this) && (unary.accept(new EvalVisitor()) >= 0);
  }

  // returns true if a negative number is never encountered at any point during
  // its evaluation
  public Boolean visitBinary(BinaryFormula binary) {
    return binary.left.accept(this) && binary.right.accept(this)
        && (binary.accept(new EvalVisitor()) >= 0);
  }

}

// to represent examples and tests for IArith
class ExamplesVisitors {
  ExamplesVisitors() {
  }

  Function<Double, Double> neg = new Neg();
  Function<Double, Double> sqr = new Sqr();
  BiFunction<Double, Double, Double> plus = new Plus();
  BiFunction<Double, Double, Double> minus = new Minus();
  BiFunction<Double, Double, Double> mul = new Mul();
  BiFunction<Double, Double, Double> div = new Div();

  IArithVisitor<Double> evalVisitor = new EvalVisitor();
  IArithVisitor<String> printVisitor = new PrintVisitor();
  IArithVisitor<IArith> doublerVisitor = new DoublerVisitor();
  IArithVisitor<Boolean> noNegativeResults = new NoNegativeResults();

  IArith const0 = new Const(0);
  IArith const1 = new Const(1);
  IArith const2 = new Const(2);
  IArith const3 = new Const(3);

  IArith un1 = new UnaryFormula(this.neg, "neg", this.const1);
  IArith un2 = new UnaryFormula(this.sqr, "sqr", this.const2);
  IArith bi1 = new BinaryFormula(this.plus, "plus", this.const0, this.const3);
  IArith bi2 = new BinaryFormula(this.minus, "minus", this.const2, this.const1);
  IArith bi3 = new BinaryFormula(this.mul, "mul", this.const3, this.const3);
  IArith bi4 = new BinaryFormula(this.div, "div", this.const2, this.const2);
  IArith bi5 = new BinaryFormula(this.mul, "mul", this.un2, this.un2);
  IArith bi6 = new BinaryFormula(this.div, "div", this.bi3, this.bi1);
  IArith bi7 = new BinaryFormula(this.minus, "minus", this.bi5, this.const2);
  IArith bi8 = new BinaryFormula(this.minus, "minus", this.const1, this.bi7);

  // to test the method accept on the interface IArith
  void testAccept(Tester t) {
    t.checkInexact(this.const0.accept(this.evalVisitor), 0.0, 0.001);
    t.checkExpect(this.un1.accept(this.printVisitor), "(neg 1.0)");
    t.checkExpect(this.bi1.accept(this.doublerVisitor),
        new BinaryFormula(this.plus, "plus", this.const0, new Const(6)));
    t.checkExpect(new Const(-4).accept(this.noNegativeResults), false);
  }

  // to test the method apply on the built-in interface Function for the class
  // UnaryFormula
  void testApplytUnary(Tester t) {
    t.checkInexact(this.neg.apply(2.0), -2.0, 0.001);
    t.checkInexact(this.neg.apply(0.0), 0.0, 0.001);
    t.checkInexact(this.neg.apply(-2.0), 2.0, 0.001);
    t.checkInexact(this.sqr.apply(2.0), 4.0, 0.001);
    t.checkInexact(this.sqr.apply(0.0), 0.0, 0.001);
    t.checkInexact(this.sqr.apply(-2.0), 4.0, 0.001);
  }

  // to test the method apply on the built-in interface BiFunction for the class
  // BinaryFormula
  void testApplyBinary(Tester t) {
    t.checkInexact(this.plus.apply(1.0, 1.0), 2.0, 0.001);
    t.checkInexact(this.minus.apply(-4.0, 6.0), -10.0, 0.001);
    t.checkInexact(this.mul.apply(3.0, 4.0), 12.0, 0.001);
    t.checkInexact(this.div.apply(-27.0, 9.0), -3.0, 0.001);
  }

  // to test the method apply on the EvalVisitor class in the interface
  // IArithVisitor
  void testEvalVisitor(Tester t) {
    t.checkExpect(this.evalVisitor.apply(this.un1), -1.0);
    t.checkExpect(this.evalVisitor.apply(this.un2), 4.0);
    t.checkExpect(this.evalVisitor.apply(this.bi1), 3.0);
    t.checkExpect(this.evalVisitor.apply(this.bi2), 1.0);
    t.checkExpect(this.evalVisitor.apply(this.bi3), 9.0);
    t.checkExpect(this.evalVisitor.apply(this.bi4), 1.0);
    t.checkExpect(this.evalVisitor.apply(this.bi5), 16.0);
    t.checkExpect(this.evalVisitor.apply(this.bi6), 3.0);
    t.checkExpect(this.evalVisitor.apply(this.bi7), 14.0);
  }

  // to test the method apply on the PrintVisitor class in the interface
  // IArithVisitor
  void testPrintVisitor(Tester t) {
    t.checkExpect(this.printVisitor.apply(this.const0), "0.0");
    t.checkExpect(this.printVisitor.apply(this.un1), "(neg 1.0)");
    t.checkExpect(this.printVisitor.apply(this.un2), "(sqr 2.0)");
    t.checkExpect(this.printVisitor.apply(this.bi1), "(plus 0.0 3.0)");
    t.checkExpect(this.printVisitor.apply(this.bi2), "(minus 2.0 1.0)");
    t.checkExpect(this.printVisitor.apply(this.bi3), "(mul 3.0 3.0)");
    t.checkExpect(this.printVisitor.apply(this.bi4), "(div 2.0 2.0)");
    t.checkExpect(this.printVisitor.apply(this.bi5), "(mul (sqr 2.0) (sqr 2.0))");
    t.checkExpect(this.printVisitor.apply(this.bi6), "(div (mul 3.0 3.0) (plus 0.0 3.0))");
    t.checkExpect(this.printVisitor.apply(this.bi7), "(minus (mul (sqr 2.0) (sqr 2.0)) 2.0)");
  }

  // to test the method apply on the DoublerVisitor class in the interface
  // IArithVisitor
  void testDoublerVisitor(Tester t) {
    t.checkExpect(this.doublerVisitor.apply(this.const1), new Const(2.0));
    t.checkExpect(this.doublerVisitor.apply(this.const3), new Const(6.0));
    t.checkExpect(this.doublerVisitor.apply(this.un1),
        new UnaryFormula(this.neg, "neg", new Const(2.0)));
    t.checkExpect(this.doublerVisitor.apply(this.bi2),
        new BinaryFormula(this.minus, "minus", new Const(4.0), this.const2));
    t.checkExpect(this.doublerVisitor.apply(this.bi6),
        new BinaryFormula(this.div, "div",
            new BinaryFormula(this.mul, "mul", new Const(6.0), new Const(6.0)),
            new BinaryFormula(this.plus, "plus", this.const0, new Const(6.0))));
  }

  // to test the method apply on the NoNegativeResults class in the interface
  // IArithVisitor
  void testNoNegativeResults(Tester t) {
    t.checkExpect(this.noNegativeResults.apply(this.const1), true);
    t.checkExpect(this.noNegativeResults.apply(this.un1), false);
    t.checkExpect(this.noNegativeResults.apply(this.bi8), false);
    t.checkExpect(this.noNegativeResults.apply(this.bi4), true);
  }

  // to test the method visitConstant in the interface IArithVisitor
  void testVisitConstant(Tester t) {
    t.checkInexact(this.evalVisitor.visitConstant(new Const(2)), 2.0, 0.001);
    t.checkExpect(this.printVisitor.visitConstant(new Const(0)), "0.0");
    t.checkExpect(this.doublerVisitor.visitConstant(new Const(1)), new Const(2));
    t.checkExpect(this.noNegativeResults.visitConstant(new Const(-6)), false);
  }

  // to test the method visitUnary in the interface IArithVisitor
  void testVisitUnary(Tester t) {
    t.checkInexact(this.evalVisitor.visitUnary(new UnaryFormula(this.neg, "neg", new Const(3))),
        -3.0, 0.001);
    t.checkExpect(this.printVisitor.visitUnary(new UnaryFormula(this.sqr, "sqr", new Const(3))),
        "(sqr 3.0)");
    t.checkExpect(this.doublerVisitor.visitUnary(new UnaryFormula(this.sqr, "sqr", new Const(3))),
        new UnaryFormula(this.sqr, "sqr", new Const(6)));
    t.checkExpect(
        this.noNegativeResults.visitUnary(new UnaryFormula(this.sqr, "sqr", new Const(3))), true);
  }

  // to test the method visitBinary in the interface IArithVisitor
  void testVisitBinary(Tester t) {
    t.checkInexact(this.evalVisitor
        .visitBinary(new BinaryFormula(this.plus, "plus", new Const(4), new Const(5))), 9.0, 0.001);
    t.checkExpect(this.printVisitor.visitBinary(
        new BinaryFormula(this.minus, "minus", new Const(4), new Const(5))), "(minus 4.0 5.0)");
    t.checkExpect(
        this.doublerVisitor
            .visitBinary(new BinaryFormula(this.mul, "mul", new Const(4), new Const(5))),
        new BinaryFormula(this.mul, "mul", new Const(8), new Const(10)));
    t.checkExpect(this.noNegativeResults
        .visitBinary(new BinaryFormula(this.div, "div", new Const(-4), new Const(5))), false);
  }
}
