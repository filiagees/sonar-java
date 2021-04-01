package org.sonar.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import static org.sonar.java.checks.verifier.TestUtils.testSourcesPath;

class VarCanBeUsedCheckTest {

  @Test
  void test() {
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/VarCanBeUsedCheck.java"))
      .withCheck(new VarCanBeUsedCheck())
      .withJavaVersion(10)
      .verifyIssues();
  }

  @Test
  void test_no_version() {
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/VarCanBeUsedCheck.java"))
      .withCheck(new VarCanBeUsedCheck())
      .verifyNoIssues();
  }

  @Test
  void test_old_version() {
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/VarCanBeUsedCheck.java"))
      .withJavaVersion(9)
      .withCheck(new VarCanBeUsedCheck())
      .verifyNoIssues();
  }
  
  @Test
  void test_no_semantic() {
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/VarCanBeUsedCheck.java"))
      .withoutSemantic()
      .withCheck(new VarCanBeUsedCheck())
      .verifyNoIssues();
  }
  
}
