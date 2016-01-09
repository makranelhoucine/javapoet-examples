
public class Utils {

    public static String getApplicationPath() {
        
        String path = System.getProperty("user.dir");
        System.out.println("Current dir using System: " + path);
        System.out.println("");
        return path;
        
    }
    
}
