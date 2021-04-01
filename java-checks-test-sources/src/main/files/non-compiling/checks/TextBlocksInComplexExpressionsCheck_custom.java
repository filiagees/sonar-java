package test;

class TextBlocksInComplexExpressionsCheck {
  
  void fun() {
    listOfString.stream()
      // Noncompliant@+1
      .map(str -> !"""
        <project>
          <modelVersion>4.0.0</modelVersion>
          <parent>
            <groupId>com.mycompany.app</groupId>
            <artifactId>my-app</artifactId>
            <version>1</version>
          </parent>
         
          <groupId>com.mycompany.app</groupId>
          <artifactId>my-module</artifactId>
          <version>1</version>
        </project>
        ABC
        ABC
        CBA
        """.equals(str));
  }

  void fun() {
    listOfString.stream()
      // Compliant
      .map(str -> !"""
    <project>
      <modelVersion>4.0.0</modelVersion>
      <parent>
        <groupId>com.mycompany.app</groupId>
        <artifactId>my-app</artifactId>
        <version>1</version>
      </parent>
     
      <groupId>com.mycompany.app</groupId>
      <artifactId>my-module</artifactId>
      <version>1</version>
    </project>
    ABC
    CBA    """.equals(str));



    String myTextBlock = """
    <project>
      <modelVersion>4.0.0</modelVersion>
      <parent>
        <groupId>com.mycompany.app</groupId>
        <artifactId>my-app</artifactId>
        <version>1</version>
      </parent>
     
      <groupId>com.mycompany.app</groupId>
      <artifactId>my-module</artifactId>
      <version>1</version>
    </project>
    """;

    listOfString.stream()
      .map(str -> !myTextBlock.equals(str)); // Compliant
  }
  
}
