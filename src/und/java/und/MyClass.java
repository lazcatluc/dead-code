package und;

public class MyClass {
    private int undSomeField;
    double defaultField = 1;
    protected String protectedField = "protected";
    public final InnerClass somePublicField = new InnerClass();
    
    private String undMethod1(Integer someMethodParameter) {
        return methodReferenced();
    }
    
    private String methodRefering1() {
        return undMethod1(1);
    }
    
    private String methodReferenced() {
        return "";
    }
    
    protected double protectedMethod() {
        return somePublicField.method() + 1;
    }
    
    int defaultMethod(int someParameter) {
        return 2 + someParameter;
    }
    
    public int publicMethod() {
        return 3;
    }
    
    private class InnerClass {
        private double method() {
            return defaultField;
        }
    }
}
