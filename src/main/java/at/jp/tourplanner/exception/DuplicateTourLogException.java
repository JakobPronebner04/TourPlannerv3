package at.jp.tourplanner.exception;

public class DuplicateTourLogException extends RuntimeException {
    public DuplicateTourLogException(String message) {
        super(message);
    }
}
