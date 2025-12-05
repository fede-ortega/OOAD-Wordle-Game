package WordleTest.UITest;

import Wordle.Event.EventBus;
import Wordle.Model.GameRestartListener;
import Wordle.Model.LetterState;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.DefaultColorStrategy;
import Wordle.UI.WordleFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;


public class WordleFrameTest {

    @BeforeEach
    void setUp() {
        // Ensure headless mode is disabled or enabled per your environment if needed.
        // System.setProperty("java.awt.headless", "true"); // or leave as default
    }


    private JTextField[][] getGridFields(WordleFrame frame) throws Exception {
        Field f = WordleFrame.class.getDeclaredField("gridFields");
        f.setAccessible(true);
        return (JTextField[][]) f.get(frame);
    }

    private JTextField getInputField(WordleFrame frame) throws Exception {
        Field f = WordleFrame.class.getDeclaredField("inputField");
        f.setAccessible(true);
        return (JTextField) f.get(frame);
    }

    private JButton getGuessButton(WordleFrame frame) throws Exception {
        Field f = WordleFrame.class.getDeclaredField("guessButton");
        f.setAccessible(true);
        return (JButton) f.get(frame);
    }

    private JLabel getMessageLabel(WordleFrame frame) throws Exception {
        Field f = WordleFrame.class.getDeclaredField("messageLabel");
        f.setAccessible(true);
        return (JLabel) f.get(frame);
    }

    private int getCurrentRow(WordleFrame frame) throws Exception {
        Field f = WordleFrame.class.getDeclaredField("currentRow");
        f.setAccessible(true);
        return (int) f.get(frame);
    }

    @Test
    void constructorInitializesGridAndMessage() throws Exception {
        WordleGame game = new WordleGame("TEST", 6);
        ColorStrategy strategy = new DefaultColorStrategy();

        WordleFrame frame = new WordleFrame(game, strategy);

        JTextField[][] grid = getGridFields(frame);
        JLabel messageLabel = getMessageLabel(frame);

        assertEquals(game.getMaxAttempts(), grid.length, "Grid rows should equal max attempts");
        assertEquals(game.getWordLength(), grid[0].length, "Grid columns should equal word length");

        Color expectedBg = strategy.getColorForState(LetterState.UNKNOWN);
        for (JTextField[] row : grid) {
            for (JTextField cell : row) {
                assertEquals("", cell.getText(), "Initial cell text should be empty");
                assertEquals(expectedBg, cell.getBackground(), "Initial cell color should be UNKNOWN color");
            }
        }

        String msg = messageLabel.getText();
        assertTrue(msg.contains(String.valueOf(game.getWordLength())),
                "Initial message should mention word length");
    }

    @Test
    void onGameEventUpdatesMessageLabel() throws Exception {
        WordleGame game = new WordleGame("TEST", 6);
        WordleFrame frame = new WordleFrame(game, new DefaultColorStrategy());
        JLabel messageLabel = getMessageLabel(frame);

        String msg = "<html>Custom message</html>";
        frame.onGameEvent(msg);

        assertEquals(msg, messageLabel.getText(), "onGameEvent should update the message label");
    }

    @Test
    void submitGuessUpdatesGridRowAndColors() throws Exception {
        WordleGame game = new WordleGame("TEST", 3);
        ColorStrategy strategy = new DefaultColorStrategy();
        WordleFrame frame = new WordleFrame(game, strategy);

        JTextField inputField = getInputField(frame);
        JButton guessButton = getGuessButton(frame);
        JTextField[][] grid = getGridFields(frame);

        inputField.setText("TEST");

        for (ActionListener al : guessButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(guessButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertEquals("T", grid[0][0].getText());
        assertEquals("E", grid[0][1].getText());
        assertEquals("S", grid[0][2].getText());
        assertEquals("T", grid[0][3].getText());

        Color expectedCorrectColor = strategy.getColorForState(LetterState.CORRECT);
        assertEquals(expectedCorrectColor, grid[0][0].getBackground());
        assertEquals(expectedCorrectColor, grid[0][1].getBackground());
        assertEquals(expectedCorrectColor, grid[0][2].getBackground());
        assertEquals(expectedCorrectColor, grid[0][3].getBackground());

        assertEquals(1, getCurrentRow(frame));
    }

    @Test
    void resetGameClearsGridAndResetsMessage() throws Exception {
        WordleGame game = new WordleGame("TEST", 3);
        ColorStrategy strategy = new DefaultColorStrategy();
        WordleFrame frame = new WordleFrame(game, strategy);

        JTextField inputField = getInputField(frame);
        JButton guessButton = getGuessButton(frame);
        JTextField[][] grid = getGridFields(frame);

        inputField.setText("TEST");
        for (ActionListener al : guessButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(guessButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        WordleGame newGame = new WordleGame("WORD", 3);
        frame.resetGame(newGame, strategy);

        Color expectedUnknownColor = strategy.getColorForState(LetterState.UNKNOWN);
        for (JTextField[] row : grid) {
            for (JTextField cell : row) {
                assertEquals("", cell.getText(), "After reset, cell text should be empty");
                assertEquals(expectedUnknownColor, cell.getBackground(), "After reset, cell color should be UNKNOWN");
            }
        }

        assertEquals(0, getCurrentRow(frame));
    }

    @Test
    void gameOverButtonsChangeToPlayAgainAndRestartListenerIsCalled() throws Exception {
        WordleGame game = new WordleGame("HI", 1);
        ColorStrategy strategy = new DefaultColorStrategy();
        WordleFrame frame = new WordleFrame(game, strategy);

        JTextField inputField = getInputField(frame);
        JButton guessButton = getGuessButton(frame);

        final boolean[] restarted = {false};
        GameRestartListener restartListener = () -> restarted[0] = true;
        frame.setGameRestart(restartListener);

        inputField.setText("HI");
        for (ActionListener al : guessButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(guessButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertFalse(inputField.isEnabled(), "Input field should be disabled after game over");
        assertEquals("Play Again", guessButton.getText(),
                "Button text should change to 'Play Again' after game over");

        for (ActionListener al : guessButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(guessButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(restarted[0], "GameRestartListener.onRestart should be called when 'Play Again' is pressed");
    }

    @Test
    void frameRegistersAsEventBusListenerAndReceivesMessages() throws Exception {
        WordleGame game = new WordleGame("TEST", 5);
        WordleFrame frame = new WordleFrame(game, new DefaultColorStrategy());
        JLabel messageLabel = getMessageLabel(frame);

        String msg = "<html>EventBus broadcast</html>";
        EventBus.getInstance().publish(msg);

        assertEquals(msg, messageLabel.getText(),
                "Frame should receive EventBus messages via onGameEvent");
    }
}
