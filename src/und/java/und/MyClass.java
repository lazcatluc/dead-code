package und;

public class MyClass {
    private int undSomeField;
    double defaultField = 1;
    protected String protectedField = "protected";
    public final Object somePublicField = new Object();
    
    private String undMethod1(Integer someMethodParameter) {
        return methodReferenced();
    }
    
    private String methodRefering1() {
        return undMethod1(1);
    }
    
    private String methodReferenced() {
        return "";
    }
    
    protected int protectedMethod() {
        return 1;
    }
    
    int defaultMethod() {
        return 2;
    }
    
    public int publicMethod() {
        return 3;
    }
}
