package checks;

import java.util.ArrayList;
import java.util.List;

public class VarCanBeUsedCheck {
  void f() {
    String s = "ABC"; // Noncompliant
    String s1 = new String("ABC"); // Noncompliant
    int i = 10; // Noncompliant
    long l = 10L; // Noncompliant

    ArrayList<String> arrayList = new ArrayList<>(5); // Noncompliant
    List<String> list = new ArrayList<>(5); // Compliant
    
    String format = String.format("%s %s %s", "a", "b", "c"); // Noncompliant
    
    
    Object o = new Object(); // Noncompliant
    Object ooo = getObject(); // Noncompliant
    
    
    var varObject = getObject(); // Compliant
    
    var sss = "STRING"; // Compliant
  }
  
  
  
  private Object getObject() {
    return new Object();
  }
}
