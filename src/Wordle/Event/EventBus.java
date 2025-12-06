package Wordle.Event;

import java.util.ArrayList;
import java.util.List;

public class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    private final List<GameEventListener> listeners = new ArrayList<>();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void register(GameEventListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(listener);
    }

    public void unregister(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(String message) {
        for (GameEventListener listener : new ArrayList<>(listeners)) {
            listener.onGameEvent(message);
        }
    }
}
