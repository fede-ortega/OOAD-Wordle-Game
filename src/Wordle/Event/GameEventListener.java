package Wordle.Event;

/**
 * Listener used in the Observer pattern.
 * Any object that wants to receive messages from the EventBus
 * must implement this interface.
 */
public interface GameEventListener {

    /**
     * Called when the EventBus publishes a new message.
     *
     * @param message text describing the event.
     */
    void onGameEvent(String message);
}
