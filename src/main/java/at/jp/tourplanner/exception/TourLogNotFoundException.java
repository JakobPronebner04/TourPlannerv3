package at.jp.tourplanner.exception;

public class TourLogNotFoundException extends RuntimeException {
    public TourLogNotFoundException(String message) {
        super(message);
    }
}
