package at.jp.tourplanner.event;

public interface EventListener<T> {
    void event(T value);
}