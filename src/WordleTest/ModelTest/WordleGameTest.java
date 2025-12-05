package WordleTest.ModelTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Wordle.Event.EventBus;
import Wordle.Event.GameEventListener;
import Wordle.Model.LetterState;
import Wordle.Model.WordleGame;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    private static class CapturingListener implements GameEventListener {
        private String lastMessage;
        private int callCount;

        @Override
        public void onGameEvent(String message) {
            this.lastMessage = message;
            this.callCount++;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public int getCallCount() {
            return callCount;
        }
    }

    private CapturingListener listener;

    @BeforeEach
    void setUp() {
        listener = new CapturingListener();
        EventBus.getInstance().register(listener);
    }

    @AfterEach
    void tearDown() {
        EventBus.getInstance().unregister(listener);
    }

    @Test
    void correctGuessShouldWinGameAndPublishMessage() {
        WordleGame game = new WordleGame("APPLE", 6);

        LetterState[] states = game.guess("APPLE");

        assertTrue(game.isGameWon(), "Game should be marked as won.");
        assertTrue(game.isGameOver(), "Game should be over after correct guess.");
        assertEquals(1, game.getAttempts(), "Attempts should be incremented.");
        // All letters should be marked as CORRECT:
        for (LetterState s : states) {
            assertEquals(LetterState.CORRECT, s, "Each letter should be CORRECT.");
        }
        assertNotNull(listener.getLastMessage(), "A message should be published.");
        assertTrue(listener.getLastMessage().contains("Congratulations"),
                "Winning message should contain 'Congratulations'.");
    }

    @Test
    void incorrectGuessShouldIncrementAttemptsAndNotWin() {
        WordleGame game = new WordleGame("APPLE", 6);

        LetterState[] states = game.guess("MONEY");

        assertFalse(game.isGameWon(), "Game should not be won after wrong guess.");
        assertFalse(game.isGameOver(), "Game should not be over after first wrong guess.");
        assertEquals(1, game.getAttempts(), "Attempts should have been incremented.");
        assertEquals(game.getWordLength(), states.length, "States array length should match word length.");
    }

    @Test
    void maxAttemptsShouldEndGame() {
        WordleGame game = new WordleGame("APPLE", 2);

        game.guess("MONEY");
        game.guess("PLANE");

        assertTrue(game.isGameOver(), "Game should be over after max attempts.");
        assertEquals(2, game.getAttempts(), "Attempts should equal maxAttempts.");
        assertNotNull(listener.getLastMessage(), "A message should be published when game ends.");
        assertTrue(listener.getLastMessage().contains("No more attempts"),
                "End-of-game message should mention no more attempts.");
    }

    @Test
    void guessWithWrongLengthShouldThrowAndPublishMessage() {
        WordleGame game = new WordleGame("APPLE", 6);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> game.guess("TOO"),
                "Guess with wrong length should throw IllegalArgumentException.");

        assertEquals("Guess has wrong length.", ex.getMessage());
        assertNotNull(listener.getLastMessage(), "A message should be published for wrong length.");
        assertTrue(listener.getLastMessage().startsWith("<html>Your guess must have exactly <br>"));
    }

    @Test
    void duplicateLettersShouldBeEvaluatedCorrectly() {
        WordleGame game = new WordleGame("APPLE", 6);

        LetterState[] states = game.guess("ALLEY");

        assertEquals(LetterState.CORRECT, states[0]);
        assertEquals(LetterState.PRESENT, states[1]);
        assertEquals(LetterState.ABSENT, states[2]);
        assertEquals(LetterState.PRESENT, states[3]);
        assertEquals(LetterState.ABSENT, states[4]);
    }

    @Test
    void guessingAfterGameIsOverShouldThrow() {
        WordleGame game = new WordleGame("APPLE", 1);

        game.guess("MONEY");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> game.guess("APPLE"),
                "Guessing after the game is over should throw IllegalStateException.");

        assertEquals("Game is already over.", ex.getMessage());
        assertTrue(listener.getLastMessage().contains("already over"),
                "Message should notify that the game is already over.");
    }
}
