public class Main {

    public static void main(String[] args) {
        
        //Utils.getApplicationPath();
        GeneratorJavaClasses.createJavaFile();
        GeneratorJavaClasses.compileJavaFile();
        Class<?> clazz = GeneratorJavaClasses.loadJavaClass();
        GeneratorJavaClasses.runJavaClass(clazz);
    }
    
}
