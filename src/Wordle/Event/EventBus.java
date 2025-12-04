package Wordle.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * EventBus is both:
 * - a Singleton (only one instance in the whole app), and
 * - the "subject" for the Observer pattern.
 * It stores a list of observers and broadcasts messages to all of them.
 */
public class EventBus {

    // Singleton instance
    private static final EventBus INSTANCE = new EventBus();

    // List of registered observers
    private final List<GameEventListener> listeners = new ArrayList<>();

    // Private constructor so nobody can create more instances.
    private EventBus() {
    }

    /**
     * Returns the single global instance of the EventBus.
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a new listener/observer.
     */
    public void register(GameEventListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(listener);
    }

    /**
     * Unregisters a listener/observer.
     */
    public void unregister(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Publishes a message to all observers.
     */
    public void publish(String message) {
        // Copy the list to avoid ConcurrentModificationException
        for (GameEventListener listener : new ArrayList<>(listeners)) {
            listener.onGameEvent(message);
        }
    }
}
