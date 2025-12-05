package WordleTest.EventTest;

import Wordle.Event.EventBus;
import Wordle.Event.GameEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {

    private static class TestListener implements GameEventListener {
        private final List<String> received = new ArrayList<>();

        @Override
        public void onGameEvent(String message) {
            received.add(message);
        }

        public List<String> getReceived() {
            return received;
        }
    }

    @BeforeEach
    void resetEventBusListeners() throws Exception {
        EventBus bus = EventBus.getInstance();
        Field f = EventBus.class.getDeclaredField("listeners");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<GameEventListener> listeners =
                (List<GameEventListener>) f.get(bus);
        listeners.clear();
    }

    @Test
    void getInstanceAlwaysReturnsSameSingleton() {
        EventBus bus1 = EventBus.getInstance();
        EventBus bus2 = EventBus.getInstance();

        assertNotNull(bus1);
        assertNotNull(bus2);
        assertSame(bus1, bus2, "getInstance should return the same singleton");
    }

    @Test
    void registerAndPublishSendsMessageToListener() {
        EventBus bus = EventBus.getInstance();
        TestListener listener = new TestListener();

        bus.register(listener);
        String msg = "GAME_STARTED";

        bus.publish(msg);

        assertEquals(1, listener.getReceived().size(),
                "Listener should receive exactly one message");
        assertEquals(msg, listener.getReceived().getFirst(),
                "Listener should receive the published message");
    }

    @Test
    void registerLullListenerDoesNothing() throws Exception {
        EventBus bus = EventBus.getInstance();

        // capture size before
        Field f = EventBus.class.getDeclaredField("listeners");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<GameEventListener> listeners =
                (List<GameEventListener>) f.get(bus);
        int sizeBefore = listeners.size();

        bus.register(null);

        int sizeAfter = listeners.size();
        assertEquals(sizeBefore, sizeAfter,
                "Registering null should not change the size of listeners list");
    }

    @Test
    void unregisterRemovesListener() {
        EventBus bus = EventBus.getInstance();
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();

        bus.register(listener1);
        bus.register(listener2);

        bus.unregister(listener1);

        String msg = "ROUND_ENDED";
        bus.publish(msg);

        // listener1 was unregistered, should receive nothing
        assertTrue(listener1.getReceived().isEmpty(),
                "Unregistered listener should not receive messages");

        // listener2 still registered, should receive the message
        assertEquals(1, listener2.getReceived().size());
        assertEquals(msg, listener2.getReceived().getFirst());
    }

    @Test
    void publishWithNoListenersDoesNotThrow() {
        EventBus bus = EventBus.getInstance();

        assertDoesNotThrow(() -> bus.publish("ANY_EVENT"),
                "Publishing with no listeners should not throw");
    }

    @Test
    void publishAllowsListenersToUnregisterThemselves() {
        EventBus bus = EventBus.getInstance();

        GameEventListener selfRemovingListener = new GameEventListener() {
            @Override
            public void onGameEvent(String message) {
                bus.unregister(this);
            }
        };

        bus.register(selfRemovingListener);
        assertDoesNotThrow(
                () -> bus.publish("TEST_EVENT"),
                "publish should not throw even if listener unregisters itself"
        );
    }

    @Test
    void multipleListenersAllReceiveMessage() {
        EventBus bus = EventBus.getInstance();
        TestListener l1 = new TestListener();
        TestListener l2 = new TestListener();

        bus.register(l1);
        bus.register(l2);

        String msg = "GUESS_SUBMITTED";
        bus.publish(msg);

        assertEquals(1, l1.getReceived().size());
        assertEquals(msg, l1.getReceived().getFirst());

        assertEquals(1, l2.getReceived().size());
        assertEquals(msg, l2.getReceived().getFirst());
    }
}
