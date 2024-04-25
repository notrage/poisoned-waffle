package Gaufre.Configuration;

public class Config {
    private static boolean estJar;

    static private boolean muet = true;

    private static final boolean debug = true;

    public Config() {
        estJar = System.getProperty("java.class.path").endsWith("jar");
    }

    static public boolean estJar() {
        return estJar;
    }

    static public boolean estMuet() {
        return muet;
    }

    static public void toggleSon() {
        muet = !muet;
    }

    public static void debug(Object... args) {
        if (debug) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length > 2) { // Skip the current function and our caller (Debug)
                StackTraceElement caller = stackTrace[2];
                String className = caller.getClassName();
                String methodName = caller.getMethodName();
                int lineNumber = caller.getLineNumber();
                System.out.printf("Debug at %s.%s:%d: ", className, methodName, lineNumber);

                // Print arguments
                for (Object arg : args) {
                    System.out.print(arg + " ");
                }
                System.out.println();
            }
        }
    }
}
