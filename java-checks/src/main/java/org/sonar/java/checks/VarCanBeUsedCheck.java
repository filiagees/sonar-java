package org.sonar.java.checks;

import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.JavaVersionAwareVisitor;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaVersion;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "S6212")
public class VarCanBeUsedCheck extends IssuableSubscriptionVisitor implements JavaVersionAwareVisitor {
  
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.VARIABLE);
  }

  @Override
  public boolean isCompatibleWithJavaVersion(JavaVersion version) {
    return version.isJava10Compatible();
  }

  @Override
  public void visitNode(Tree tree) {
    VariableTree variableTree = (VariableTree) tree;
    if (variableTree.initializer() == null || variableTree.type().is(Tree.Kind.VAR_TYPE)) {
      return;
    }

    Type type = variableTree.type().symbolType();
    Type initializerType = variableTree.initializer().symbolType();
    
    if (type.fullyQualifiedName().equals(initializerType.fullyQualifiedName())) {
      reportIssue(variableTree.simpleName(), "");
    }
  }
}
