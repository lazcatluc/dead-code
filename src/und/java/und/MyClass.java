package und;

public class MyClass {
    private int undSomeField;
    double defaultField = 1;
    protected String protectedField = "protected";
    public final InnerClass somePublicField = new InnerClass();
    
    private static int unusedStaticMethod() {
        return 2;
    }
    
    private static int usedStaticMethod() {
        return 3;
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
            return defaultField + usedStaticMethod();
        }
    }
}
