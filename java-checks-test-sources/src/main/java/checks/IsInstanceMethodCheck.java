package checks;

public class IsInstanceMethodCheck {
  int noncompliant1(Object o) {
    if (String.class.isInstance(o)) {  // Noncompliant [[sc=9;ec=35]]{{Replace this usage of "String.class.isInstance()" with "instanceof String".}}
      return 42;
    }
    return 0;
  }

  int noncompliant2(Number n) {
    if (String.class.isInstance("ABC".substring(0, 12))) { // Noncompliant
      
    }    
    
    if ("ABC".substring(0, 12) instanceof String) {
      
    }
    
    if (String.class.isInstance(n)) {  // Noncompliant
      return 42;
    }
    return 0;
  }

  int compliant1(Object o) {
    if (o instanceof String) {// Compliant
      return 42;
    }
    return 0;
  }

  boolean compliant3(Object o, String c) throws ClassNotFoundException
  {
    Utility.clazz.isInstance(o); // Compliant
    (Utility.clazz).isInstance(o); // Compliant
    (Utility.clazz).getClass().isInstance(o); // Compliant

    if (o instanceof Utility) {// Compliant
      return false;
    }
    
    return Class.forName(c).isInstance(o); // Compliant, can't use instanceof operator here
  }
  
  
}


class Utility {
  
   static Class clazz = String.class;
  
}
