package Wordle.UI;

import Wordle.Event.EventBus;
import Wordle.Event.GameEventListener;
import Wordle.Model.LetterState;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Swing user interface for the Wordle game.
 * This class also acts as an Observer by registering itself
 * in the EventBus and showing messages on the screen.
 */
public class WordleFrame extends JFrame implements GameEventListener {

    private final WordleGame game;
    private final ColorStrategy colorStrategy;

    private final JTextField[][] gridFields;
    private final JTextField inputField;
    private final JButton guessButton;
    private final JLabel messageLabel;

    private int currentRow = 0;

    private final int TILE_SIZE = 50;

    public WordleFrame(WordleGame game, ColorStrategy colorStrategy) {
        this.game = game;
        this.colorStrategy = colorStrategy;

        int rows = game.getMaxAttempts();
        int cols = game.getWordLength();
        this.gridFields = new JTextField[rows][cols];

        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create components and layout.
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel gridPanel = createGridPanel(rows, cols);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        guessButton = new JButton("Guess");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(guessButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        messageLabel = new JLabel("<html>Enter a " + cols + "-letter word,<br>then press Guess.</html>");
        mainPanel.add(messageLabel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window on screen.

        // Register as observer in the Singleton EventBus.
        EventBus.getInstance().register(this);

        // Add event handlers.
        GuessAction guessAction = new GuessAction();
        guessButton.addActionListener(guessAction);
        inputField.addActionListener(guessAction); // Press Enter in the text field.

        // Initialize grid appearance.
        resetGridColors();
    }

    private JPanel createGridPanel(int rows, int cols) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setEditable(false);
                cell.setFocusable(false);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD, 24f));

                Dimension dimension = new Dimension(TILE_SIZE, TILE_SIZE);
                cell.setPreferredSize(dimension);
                cell.setMaximumSize(dimension);
                cell.setMinimumSize(dimension);

                gridFields[r][c] = cell;
                panel.add(cell);
            }
        }
        return panel;
    }

    private void resetGridColors() {
        for (JTextField[] row : gridFields) {
            for (JTextField cell : row) {
                cell.setBackground(colorStrategy.getColorForState(LetterState.UNKNOWN));
            }
        }
    }

    private void submitGuess() {
        if (game.isGameOver()) {
            EventBus.getInstance().publish("The game has finished. Restart the program to play again.");
            return;
        }

        String guess = inputField.getText();
        try {
            LetterState[] states = game.guess(guess);
            updateGridRow(guess.toUpperCase(), states);
            inputField.setText("");

            if (game.isGameOver()) {
                guessButton.setEnabled(false);
                inputField.setEnabled(false);
            }
        } catch (IllegalArgumentException ex) {
            // The game already sent a human-readable message through the EventBus.
            // We only log something to the console for debugging.
            System.out.println("Invalid guess: " + ex.getMessage());
        } catch (IllegalStateException ex) {
            System.out.println("Game state error: " + ex.getMessage());
        }
    }

    private void updateGridRow(String guess, LetterState[] states) {
        int cols = game.getWordLength();
        for (int c = 0; c < cols; c++) {
            if (c < guess.length()) {
                char ch = guess.charAt(c);
                gridFields[currentRow][c].setText(String.valueOf(ch));
                gridFields[currentRow][c].setBackground(
                        colorStrategy.getColorForState(states[c])
                );
            } else {
                gridFields[currentRow][c].setText("");
                gridFields[currentRow][c].setBackground(
                        colorStrategy.getColorForState(LetterState.UNKNOWN)
                );
            }
        }
        currentRow++;
    }

    @Override
    public void onGameEvent(String message) {
        // This method is called by the EventBus whenever someone publishes a message.
        // We show the text in the label at the top of the window.
        messageLabel.setText(message);
    }

    private class GuessAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            submitGuess();
        }
    }
}
