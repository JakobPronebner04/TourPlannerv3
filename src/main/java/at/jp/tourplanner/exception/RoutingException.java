package at.jp.tourplanner.exception;

public class RoutingException extends RuntimeException {
    public RoutingException(String message) { super(message); }
    public RoutingException(String message, Throwable cause) { super(message, cause); }
}
