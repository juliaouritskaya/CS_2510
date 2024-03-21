import java.util.ArrayList;

import tester.Tester;

// to represent a student college major profile 
class CollegeMajorInfo {
  String major;
  int startYear;
  int endYear;

  // the constructor
  CollegeMajorInfo(String major, int startYear, int endYear) {
    this.major = major;
    this.startYear = startYear;
    this.endYear = endYear;
  }

  // to checks if this is the same as the given objects
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof CollegeMajorInfo)) {
      return false;
    }

    CollegeMajorInfo that = (CollegeMajorInfo) other;
    return this.major.equals(that.major) && this.startYear == that.startYear
        && this.endYear == that.endYear;
  }

  // returns a hashcode for this object
  public int hashCode() {
    return this.major.hashCode() * 1000 + this.startYear + this.endYear;
  }
}

// to represent a database of college major profiles 
class CollegeMajorDatabase {
  ArrayList<CollegeMajorInfo> list;

  // the constructor
  CollegeMajorDatabase() {
    this.list = new ArrayList<CollegeMajorInfo>();
  }

  // adds the provided college major info object to the database
  void add(CollegeMajorInfo c) {
    this.list.add(c);
  }

  // determines whether the provided college major info object is present in the
  // database or not
  boolean contains(CollegeMajorInfo c) {
    if (this.list != null) {
      return this.list.contains(c);
    }
    else {
      return false;
    }
  }
}

// to represent tests and examples for CollegeMajor
class ExamplesCollegeMajorDatabase {
  ExamplesCollegeMajorDatabase() {
  }

  CollegeMajorInfo cs;
  CollegeMajorInfo cs2;

  CollegeMajorInfo ce;
  CollegeMajorInfo ce2;

  CollegeMajorInfo me;
  CollegeMajorInfo me2;

  CollegeMajorInfo che;
  CollegeMajorInfo che2;

  ArrayList<CollegeMajorInfo> database;
  CollegeMajorDatabase majorInfoDatabase;

  // sets up the initial conditions
  void initialConditions() {
    database = new ArrayList<CollegeMajorInfo>();
    majorInfoDatabase = new CollegeMajorDatabase();

    cs = new CollegeMajorInfo("CS", 2019, 2023);
    cs2 = new CollegeMajorInfo("CS", 2019, 2023);

    ce = new CollegeMajorInfo("CE", 2020, 2024);
    ce2 = new CollegeMajorInfo("CE", 2021, 2025);

    me = new CollegeMajorInfo("ME", 2023, 2027);
    me2 = new CollegeMajorInfo("ME", 2023, 2027);

    che = new CollegeMajorInfo("CHE", 2015, 2019);
    che2 = new CollegeMajorInfo("CHE", 2016, 2020);
  }

  // to test the add method in the class CollegeMajorDatabase
  void testAdd(Tester t) {
    initialConditions();

    t.checkExpect(this.majorInfoDatabase.list.size(), 0);
    this.majorInfoDatabase.add(this.cs);
    t.checkExpect(this.majorInfoDatabase.contains(this.cs), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.cs2), true);
    t.checkExpect(this.majorInfoDatabase.list.get(0), this.cs);

    this.majorInfoDatabase.add(this.ce);
    this.majorInfoDatabase.add(this.me);
    this.majorInfoDatabase.add(this.che);

    t.checkExpect(this.majorInfoDatabase.list.size(), 4);
    t.checkExpect(this.majorInfoDatabase.contains(this.ce), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.ce2), false);
    t.checkExpect(this.majorInfoDatabase.contains(this.me), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.me2), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.che), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.che2), false);
  }

  // to test the contains method in the class CollegeMajorDatabase
  void testContains(Tester t) {
    initialConditions();

    t.checkExpect(this.majorInfoDatabase.list.size(), 0);
    this.majorInfoDatabase.add(this.cs);
    this.majorInfoDatabase.add(this.ce);
    this.majorInfoDatabase.add(this.me);
    this.majorInfoDatabase.add(this.che);

    t.checkExpect(this.majorInfoDatabase.contains(this.cs2), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.ce2), false);
    t.checkExpect(this.majorInfoDatabase.contains(this.me2), true);
    t.checkExpect(this.majorInfoDatabase.contains(this.che2), false);
  }

  // to test the equals method in the class CollegeMajorInfo
  void testEquals(Tester t) {
    initialConditions();

    t.checkExpect(this.cs.equals(this.cs), true);
    t.checkExpect(this.cs.equals(this.cs2), true);

    t.checkExpect(this.ce.equals(this.ce), true);
    t.checkExpect(this.ce.equals(this.ce2), false);

    t.checkExpect(this.me.equals(this.me), true);
    t.checkExpect(this.me.equals(this.me2), true);

    t.checkExpect(this.che.equals(this.che), true);
    t.checkExpect(this.che.equals(this.che2), false);
  }

  // to test the hashcode method in the class CollegeMajorInfo
  void testHashCode(Tester t) {
    initialConditions();

    t.checkExpect(this.cs.hashCode(), 2164042);
    t.checkExpect(this.cs2.hashCode(), 2164042);

    t.checkExpect(this.ce.hashCode(), 2150044);
    t.checkExpect(this.ce2.hashCode(), 2150046);
  }
}