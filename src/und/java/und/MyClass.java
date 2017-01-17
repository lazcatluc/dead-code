package und;

public class MyClass {
    private int undSomeField;
    private int innerClassCounter;
    double defaultField = 1;
    protected String protectedField = "protected";
    public final InnerClass somePublicField = new InnerClass();
    
    static {
        usedInBlockStaticMethod();
    }
    
    private static int unusedStaticMethod() {
        return 2;
    }
    
    private static int usedStaticMethod() {
        return 3;
    }
    
    private static int usedInBlockStaticMethod() {
        return 4;
    }
    
    private String undMethod1(Integer someMethodParameter) {
        return methodReferenced();
    }
    
    private String methodReferenced() {
        return "";
    }
    
    private String methodRefering1() {
        return undMethod1(1) + methodRefering1();
    }
    
    protected double protectedMethod() {
        undMethod1(2);
        return somePublicField.method() + 1 + innerClassCounter;
    }
    
    int defaultMethod(int someParameter) {
        return 2 + someParameter;
    }
    
    public int publicMethod() {
        return 3;
    }
    
    private class InnerClass {
        private int innerVariable = 2;
        
        public InnerClass() {
            innerClassCounter++;
        }
        
        private double method() {
            innerVariable++;
            return defaultField + usedStaticMethod();
        }
    }
}
