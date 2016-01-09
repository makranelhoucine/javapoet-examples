import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.StandardLocation;

@SuppressWarnings("restriction")
public class GeneratorJavaClasses {

    private static String PACKAGE_NAME = "com.hascode.tutorial";
    private static String CLASS_NAME = "CustomerService";

    private static String FILES = "/files";
    private static String SRC = "/src";
    private static String BIN = "/bin";

    private static String PATH_CURR = Utils.getApplicationPath();
    private static String PATH_FILES = PATH_CURR + FILES;
    private static String PATH_SRC = PATH_FILES + SRC;
    private static String PATH_BIN = PATH_FILES + BIN;

    {
        // System.out.println("PATH_CURR = " + PATH_CURR);
    }

    // http://www.hascode.com/2015/02/generating-java-source-files-with-javapoet/
    public static void createJavaFile() {

        MethodSpec greetCustomer = MethodSpec.methodBuilder("greetCustomer")
                .addModifiers(Modifier.PUBLIC).returns(String.class)
                .addParameter(String.class, "name")
                .addStatement("return $S+$N", "Welcome, ", "name").build();

        TypeSpec customerService = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC).addMethod(greetCustomer).build();

        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, customerService)
                .build();

        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File file = new File(PATH_SRC);

        try {
            javaFile.writeTo(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void compileJavaFile() {

        // make sure file currency.data exist in c:\Program Files\Java\jdk\lib\
        // if not, copy it from c:\Program Files\Java\jdk\jre\lib\
        // else you will get Exception in thread "main" java.lang.InternalError

        System.setProperty("java.home", "c:/Program Files/Java/jdk1.8.0_66/");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler
                .getStandardFileManager(null, null, null);

        compiler.run(null, null, null, "-sourcepath", "." + FILES + SRC, "-d",
                "." + FILES + BIN,
                "." + FILES + SRC + "/" + PACKAGE_NAME.replace('.', '/') + "/"
                        + CLASS_NAME + ".java");

        try {
            fileManager.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Compiled!");
    }

    public static Class<?> loadJavaClass() {

        // File file = new File(PATH_BIN);
        File file = new File("." + FILES + BIN);
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        URL[] urls = new URL[] { url };

        ClassLoader classLoader = new URLClassLoader(urls);

        String path = PACKAGE_NAME + "." + CLASS_NAME;
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(path);
            System.out.println("aClass.getName() = " + clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    // link: https://en.wikibooks.org/wiki/Java_Programming/Reflection/Dynamic_Invocation
    public static void runJavaClass(Class<?> clazz) {
        
        // create object: approach 1
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // create object: approach 2
        //Class<?>[] constrpartypes = new Class[]{String.class, String.class};
        Class<?>[] constrpartypes = new Class[]{};
        Constructor<?> constr = null;
        try {
            constr = clazz.getConstructor(constrpartypes);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Object dummyto = null;
        try {
            //dummyto = constr.newInstance(new Object[]{"Java Programmer", "India"});
            dummyto = constr.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // create method
        Class<?>[] partypes = new Class[]{String.class};
        Method meth = null;
        try {
            meth = clazz.getMethod("greetCustomer", partypes);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        // invoke method from object
        Object[] arglist = new Object[]{"I am"};
        String output = "empty";
        try {
            output = (String) meth.invoke(o, arglist);
//            output = (String) meth.invoke(dummyto, arglist);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(output);
        
    }

}
