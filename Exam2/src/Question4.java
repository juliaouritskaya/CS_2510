import java.util.ArrayList;

import tester.Tester;

/*
 * This problem concerns the set of classes found below.
Polly is about to work on an essay assignment. She writes the given classes to help her work on the essay. Polly's vision for these classes is as follows:
She works on a draft of the essay.
Before she writes an important part, she backs up the current draft. Then she continues working on the draft.
If she does not like what she wrote, she can restore an earlier draft that was saved, and then work off of that version.
Multiple drafts can be backed up and restored.
However, there are one or more bugs in the code, that do not allow Polly to use these classes as intended above, even with a small number of drafts. 
You must find them, and then fix them.
 
For this part write a test that illustrates the problem, and in your own words describe what the bug is. 
A correctly written test should *fail* on the code as-provided, but should pass when you fix the code. 
This is for you to show that you have found the problematic scenario that prevents Polly from using these classes as intended above. 
In a comment above the test, please state the purpose of the test (i.e. the scenario you are testing).
 */

// represents a single essay 
class Essay {
  String text;

  // the constructor (starts with some content)
  Essay(String text) {
    this.text = text;
  }

  // new constructor (makes an essay have the same text as a given essay)
  Essay(Essay other) {
    this.text = other.text;
  }

  // new constructor (starts with a blank page)
  Essay() {
    this("");
  }

  // adds to the content of the essay
  // EFFECT: the essay contents have the supplied string appended
  void addContent(String s) {
    this.text = this.text + s;
  }
}

// represents an essay with the facility to save versions and revert to them
class IncrementalEssay {
  ArrayList<Essay> versions; // version(s) of the essay

  IncrementalEssay() {
    this.versions = new ArrayList<Essay>();
    // the default first draft of the essay
    this.versions.add(new Essay());
  }

  // Return the latest version of the essay
  Essay latestVersion() {
    // the last entry in the list is always the latest version
    return this.versions.get(this.versions.size() - 1);
  }

  // EFFECT: appends the supplied string to the current essay draft
  void addToCurrentText(String content) {
    versions.get(versions.size() - 1).addContent(content);
  }

  // EFFECT: archives the current draft as a new version
  // that can be retrieved later
  void backup() {
//    this.versions.add(this.latestVersion());

    // adds a new essay with the text from the latest version, creating an archive
    // of the current draft as a new version with the same text
    this.versions.add(new Essay(this.latestVersion().text));
  }

  // EFFECT: revert to the most recently
  // backed up version; nothing should happen
  // if there is only one version
  void restore() {
    if (this.versions.size() > 1) {
      this.versions.remove(this.versions.size() - 1);
    }
  }
}

// to represent examples and tests for the Essay 
class ExamplesEssay {
  ExamplesEssay() {
  }

  Essay hello;
  Essay helloPerson;

  Essay changed;

  Essay newEssay;

  ArrayList<Essay> version1;
  ArrayList<Essay> version2;

  IncrementalEssay v1;

  void initialConditions() {
    hello = new Essay("hello");
    helloPerson = new Essay("hello person");

    changed = new Essay(this.helloPerson);

    newEssay = new Essay();

    v1 = new IncrementalEssay();
    v1.versions.add(hello);
    v1.versions.add(helloPerson);

  }

  // to test the method backup in the class IncrementalEssay
  void testBackup(Tester t) {
    initialConditions();

    // checks that the latest version of 
    t.checkExpect(v1.latestVersion(), this.helloPerson);
    v1.backup();
    t.checkExpect(v1.versions.size(), 4);
    t.checkExpect(v1.latestVersion().equals(v1.versions.get(2)), false);
    t.checkExpect(v1.versions.get(3).equals(v1.versions.get(2)), false);
    t.checkExpect(v1.versions.get(3), v1.latestVersion());
  }
}

///*
// * The bug in this test is that when the backup method is called, it does not archive the current draft as a new version; 
// * rather, the archive remains the same as the latest version. Instead, it needs to make the archive of the current draft 
// * contain the same text as that of the latest version while containing a different reference point. This means that when the 
// * equals method is called on the latest version and the latest version after backup, the test should return false. However,
// * as the method is defined right now, it returns true, which should not happen. Thus, in order to fix this issue, we need 
// * add the test of the latest version as a NewEssay constructor. This would keep the text of the current draft and the latest version
// * the same while making the two versions different objects. 
// */
//*/