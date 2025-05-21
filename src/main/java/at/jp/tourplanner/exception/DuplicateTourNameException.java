package at.jp.tourplanner.exception;

public class DuplicateTourNameException extends RuntimeException {
    public DuplicateTourNameException(String message) {
        super(message);
    }
}
