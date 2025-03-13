package at.jp.tourplanner.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final Map<Events, List<EventListener>> eventListeners;

    public EventManager() {
        this.eventListeners = new HashMap<>();
    }

    public void subscribe(Events event, EventListener listener) {
        List<EventListener> listeners
                = eventListeners.getOrDefault(event, new ArrayList<>());

        listeners.add(listener);

        eventListeners.put(event, listeners);
    }

    public void publish(Events event, String message) {
        List<EventListener> listeners
                = eventListeners.getOrDefault(event, new ArrayList<>());

        for (EventListener listener: listeners) {
            listener.event(message);
        }
    }
}
