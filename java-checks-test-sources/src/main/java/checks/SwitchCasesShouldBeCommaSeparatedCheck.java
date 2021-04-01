package checks;

public class SwitchCasesShouldBeCommaSeparatedCheck {

  public void nonCompliant(String mode) {
    // Switch Statement
    switch (mode) {
      case "a":
        doSomething();
        break;
      case "b":
      case "c": // Noncompliant [[sc=7;ec=16;secondary=-1]] {{Merge the previous cases into this one using comma-separated label.}}
        doSomething();
        doSomethingElse();
        break;
      default:
        doSomethingElse();
    }
  }

  public void compliant(String mode) {
    // Empty switch statement
    switch (mode) {
    }
    // Switch case with default only
    switch (mode) {
      default:
    }
    // Switch case with empty case
    switch (mode) {
      case "a":
    }
    // Switch case with empty case and default
    switch (mode) {
      case "a":
      default:
        doSomething();
    }
  }

  private void doSomething() {
  }

  private void doSomethingElse() {
  }
}
