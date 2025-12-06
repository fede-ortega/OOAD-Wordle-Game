package Wordle.UI;

import Wordle.Event.EventBus;
import Wordle.Event.GameEventListener;
import Wordle.Model.GameRestartListener;
import Wordle.Model.LetterState;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordleFrame extends JFrame implements GameEventListener {

    private WordleGame game;
    private final ColorStrategy colorStrategy;

    private final JTextField[][] gridFields;
    private final JTextField inputField;
    private final JButton guessButton;
    private final JLabel messageLabel;
    private GameRestartListener listener;

    private int currentRow = 0;

    public WordleFrame(WordleGame game, ColorStrategy colorStrategy) {
        this.game = game;
        this.colorStrategy = colorStrategy;

        int rows = game.getMaxAttempts();
        int cols = game.getWordLength();
        this.gridFields = new JTextField[rows][cols];

        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        int mainPanelPadding = 10;
        JPanel mainPanel = new JPanel(new BorderLayout(mainPanelPadding, mainPanelPadding));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(mainPanelPadding, mainPanelPadding, mainPanelPadding, mainPanelPadding));

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
        setLocationRelativeTo(null);

        EventBus.getInstance().register(this);

        GuessAction guessAction = new GuessAction();
        guessButton.addActionListener(guessAction);
        inputField.addActionListener(guessAction);

        resetGridColors();
    }

    private JPanel createGridPanel(int rows, int cols) {
        float font = 24f;
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setEditable(false);
                cell.setFocusable(false);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD, font));

                int tileSize = 50;
                Dimension dimension = new Dimension(tileSize, tileSize);
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
            EventBus.getInstance().publish("<html>The game has finished.<br>Restart the program to play again.</html>");
            return;
        }

        String guess = inputField.getText();
        try {
            LetterState[] states = game.guess(guess);
            updateGridRow(guess.toUpperCase(), states);
            inputField.setText("");

            if (game.isGameOver()) {
                gameOverHandler();
            }

        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid guess: " + ex.getMessage());
        } catch (IllegalStateException ex) {
            System.out.println("Game state error: " + ex.getMessage());
        }
    }

    private void gameOverHandler() {
        inputField.setEnabled(false);

        guessButton.setText("Play Again");
        getRootPane().setDefaultButton(guessButton);
        for(ActionListener actionListener : inputField.getActionListeners()) {
            inputField.removeActionListener(actionListener);
        }
        for(ActionListener actionListener : guessButton.getActionListeners()) {
            guessButton.removeActionListener(actionListener);
        }
        guessButton.addActionListener(_ -> {
            if(listener != null) {
                listener.onRestart();
            }
        });
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

    public void resetGame(WordleGame game, ColorStrategy colorStrategy) {
        this.game = game;
        int cols = game.getWordLength();
        game.reset(game.getSecretWord());
        currentRow = 0;

        for (JTextField[] row : gridFields) {
            for (JTextField cell : row) {
                cell.setText("");
                cell.setBackground(colorStrategy.getColorForState(LetterState.UNKNOWN));
            }
        }

        guessButton.setText("Guess");
        for(ActionListener actionListener : guessButton.getActionListeners()) {
            guessButton.removeActionListener(actionListener);
        }

        GuessAction guessAction = new GuessAction();
        guessButton.addActionListener(guessAction);
        inputField.addActionListener(guessAction);
        inputField.setEnabled(true);
        inputField.grabFocus();

        messageLabel.setText("<html>Enter a " + cols + " letter word,<br>then press Guess.</html>");
    }

    public void setGameRestart(GameRestartListener listener) {
        this.listener = listener;
    }

    @Override
    public void onGameEvent(String message) {
        messageLabel.setText(message);
    }

    private class GuessAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            submitGuess();
        }
    }
}
