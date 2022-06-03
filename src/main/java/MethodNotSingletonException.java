public class MethodNotSingletonException extends RuntimeException {
    public MethodNotSingletonException(String method) {
        super("\"The method " + method + " should be singleton\"");
    }
}
