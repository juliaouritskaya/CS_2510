import java.util.function.BiPredicate;
import tester.Tester;

// to represent a list of generic data type T
interface IList<T> {
  // adds an item to a list
  IList<T> insert(T item);

  // returns the amount of courses a student is enrolled in
  <U> int contains(BiPredicate<T, U> courses, U students);

  // checks if a student is enrolled in a course
  boolean objectContains(BiPredicate<T, T> course, T student);
}

// to represent an empty list of generic data type T
class MtList<T> implements IList<T> {
  MtList() {
  }

  // checks if a student is enrolled in a course
  public boolean objectContains(BiPredicate<T, T> c, T s) {
    return false;
  }

  // adds an item to an empty list
  public IList<T> insert(T item) {
    return new ConsList<T>(item, this);
  }

  // returns the amount of courses a student is enrolled in
  public <U> int contains(BiPredicate<T, U> c, U s) {
    return 0;
  }
}

// to represent a non-empty list of generic data type T
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  // the constructor
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // adds an item to a non-empty list
  public IList<T> insert(T item) {
    return new ConsList<T>(item, this);
  }

  // checks if a student is enrolled in a course
  public boolean objectContains(BiPredicate<T, T> course, T student) {
    return course.test(this.first, student) || this.rest.objectContains(course, student);
  }

  // returns the amount of courses a student is enrolled in
  public <U> int contains(BiPredicate<T, U> c, U s) {
    int count = 0;
    if (c.test(this.first, s)) {
      count = 1 + this.rest.contains(c, s);
    }
    else {
      count = this.rest.contains(c, s);
    }
    return count;
  }
}

//to represent an instructor 
class Instructor {
  String name;
  IList<Course> courses;

  // main constructor
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // new constructor
  Instructor(String name, IList<Course> courses) {
    this(name);
    this.courses = courses;
  }

  // checks if this instructor is the same as the given instructor
  boolean same(Instructor i) {
    return this.name.equals(i.name);
  }

  // adds the given course to the instructor's list of courses
  void addCourse(Course course) {
    this.courses = this.courses.insert(course);
  }

  // determines whether the given student is in more than one of this instructor's
  // courses
  boolean dejavu(Student s) {
    return this.courses.contains(new CourseStudentEnrolled(), s) > 1;
  }
}

// to represent a course 
class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  // main constructor
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    prof.addCourse(this);
    this.students = new MtList<Student>();
  }

  // new constructor
  Course(String name, Instructor prof, IList<Student> students) {
    this.name = name;
    this.prof = prof;
    this.students = students;
  }

  // enrolls a student in a course
  void enrollStudentInCourse(Student s) {
    this.students = students.insert(s);
  }

  // checks if a student is in a course
  public boolean studentInCourse(Student s) {
    return this.students.objectContains(new SameStudent(), s);
  }
}

// to represent a student
class Student {
  String name;
  int id;
  IList<Course> courses;

  // the constructor
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // new constructor
  Student(String name, int id, IList<Course> courses) {
    this(name, id);
    this.courses = new MtList<Course>();
  }

  // checks if this student is the same as the given student
  boolean same(Student s) {
    return this.name.equals(s.name) && this.id == s.id;
  }

  // enrolls a student in the given course
  void enroll(Course c) {
    this.courses = courses.insert(c);
    c.enrollStudentInCourse(this);
  }

  // determines whether the given student is an any of the same classes as this
  // student
  boolean classmates(Student s) {
    return this.courses.contains(new CourseStudentEnrolled(), s) > 0
        && this.courses.contains(new CourseStudentEnrolled(), this) > 0;
  }
}

//to represent a duplicate student 
class SameStudent implements BiPredicate<Student, Student> {
  // checks if the first given student is the same as the second given student
  public boolean test(Student s1, Student s2) {
    return s1.same(s2);
  }
}

//to represent a student enrolled in a course 
class CourseStudentEnrolled implements BiPredicate<Course, Student> {
  // checks if the given student is enrolled in the given course
  public boolean test(Course c, Student s) {
    return c.studentInCourse(s);
  }
}

// to represent examples and tests for the registrar
class ExamplesRegistrar {
  ExamplesRegistrar() {
  }

  Instructor matt = new Instructor("Matt");
  Instructor jill = new Instructor("Jill");

  Course phil = new Course("Philosophy", this.matt);
  Course calculus = new Course("Calculus", this.matt);
  Course compsci = new Course("Computer Science", this.matt);
  Course reading = new Course("Reading", this.jill);
  Course writing = new Course("Writing", this.jill);

  Student ally = new Student("Ally", 22);
  Student bill = new Student("Bill", 33);
  Student charlie = new Student("Charlie", 44);
  Student dom = new Student("Dom", 55);
  Student eric = new Student("Eric", 66);

  IList<Course> mtListCourses = new MtList<Course>();
  IList<Student> mtListStudents = new MtList<Student>();

  // sets up the initial conditions for our tests, by re-initializing
  void initTestConditions() {
    this.ally = new Student("Ally", 22, this.mtListCourses);
    this.bill = new Student("Bill", 33, new ConsList<Course>(this.calculus, this.mtListCourses));
    this.charlie = new Student("Charlie", 44, new ConsList<Course>(this.calculus,
        new ConsList<Course>(this.compsci, this.mtListCourses)));
    this.dom = new Student("Dom", 55,
        new ConsList<Course>(this.compsci, new ConsList<Course>(this.reading, this.mtListCourses)));
    this.eric = new Student("Eric", 66,
        new ConsList<Course>(this.calculus,
            new ConsList<Course>(this.compsci, new ConsList<Course>(this.reading,
                new ConsList<Course>(this.writing, this.mtListCourses)))));

  }

  // tests the initial conditions
  boolean testInitConditions(Tester t) {
    initTestConditions();
    t.checkExpect(ally.courses, mtListCourses);
    ally.enroll(calculus);
    t.checkExpect(ally.courses, new ConsList<Course>(calculus, mtListCourses));
    t.checkExpect(bill.courses, mtListCourses);
    bill.enroll(compsci);
    t.checkExpect(bill.courses, new ConsList<Course>(compsci, mtListCourses));
    t.checkExpect(charlie.courses, mtListCourses);
    charlie.enroll(reading);
    t.checkExpect(charlie.courses, new ConsList<Course>(reading, mtListCourses));
    t.checkExpect(dom.courses, mtListCourses);
    dom.enroll(writing);
    t.checkExpect(dom.courses, new ConsList<Course>(writing, mtListCourses));
    t.checkExpect(eric.courses, mtListCourses);
    eric.enroll(compsci);
    t.checkExpect(eric.courses, new ConsList<Course>(compsci, mtListCourses));
    ally.enroll(phil);
    t.checkExpect(ally.courses,
        new ConsList<Course>(phil, new ConsList<Course>(calculus, mtListCourses)));

    return t.checkExpect(calculus.studentInCourse(this.ally), true)
        && t.checkExpect(writing.studentInCourse(bill), false)
        && t.checkExpect(bill.classmates(eric), true) && t.checkExpect(ally.classmates(dom), false)
        && t.checkExpect(matt.dejavu(ally), true) && t.checkExpect(jill.dejavu(charlie), false)
        && t.checkExpect(new SameStudent().test(dom, ally), false)
        && t.checkExpect(new SameStudent().test(bill, bill), true)
        && t.checkExpect(new CourseStudentEnrolled().test(compsci, bill), true)
        && t.checkExpect(new CourseStudentEnrolled().test(writing, ally), false)
        && t.checkExpect(bill.same(bill), true) && t.checkExpect(charlie.same(eric), false);
  }

  // tests insert function
  boolean testInsert(Tester t) {
    return t.checkExpect(new ConsList<Student>(charlie, mtListStudents).insert(ally),
        new ConsList<Student>(ally, new ConsList<Student>(charlie, mtListStudents)))
        && t.checkExpect(new ConsList<Course>(writing, mtListCourses).insert(compsci),
            new ConsList<Course>(compsci, new ConsList<Course>(writing, mtListCourses)))
        && t.checkExpect(mtListCourses.insert(writing),
            new ConsList<Course>(writing, mtListCourses));
  }

  // test the test method in the class SameStudent
  void testTest(Tester t) {
    t.checkExpect(new SameStudent().test(this.ally, this.ally), true);
    t.checkExpect(new SameStudent().test(this.ally, this.bill), false);
  }
}
