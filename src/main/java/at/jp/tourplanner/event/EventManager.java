package at.jp.tourplanner.event;

import java.util.*;

public class EventManager {
    private final Map<Events, List<EventListener<?>>> eventListeners;

    public EventManager() {
        this.eventListeners = new HashMap<>();
    }

    public <T> void subscribe(Events event, EventListener<T> listener) {
        eventListeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    public <T> void publish(Events event, T value) {
        List<EventListener<?>> listeners = eventListeners.getOrDefault(event, new ArrayList<>());
        for (EventListener<?> listener : listeners) {
            EventListener<T> typedListener = (EventListener<T>) listener;
            typedListener.event(value);
        }
    }
}
