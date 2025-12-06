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

    private static final int PANEL_PADDING = 10;
    private static final int BORDER_LAYOUT = 5;
    private static final int TITLE_SIZE = 50;
    private static final int NO_ROWS = 0;
    private static final float FONT_SIZE = 24f;

    private WordleGame game;
    private final ColorStrategy colorStrategy;

    private final JTextField[][] gridFields;
    private final JTextField inputField;
    private final JButton guessButton;
    private final JLabel messageLabel;
    private GameRestartListener listener;

    private int currentRow = NO_ROWS;

    public WordleFrame(WordleGame game, ColorStrategy colorStrategy) {
        this.game = game;
        this.colorStrategy = colorStrategy;

        int rows = game.getMaxAttempts();
        int cols = game.getWordLength();
        this.gridFields = new JTextField[rows][cols];

        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        int mainPanelPadding = PANEL_PADDING;
        JPanel mainPanel = new JPanel(new BorderLayout(mainPanelPadding, mainPanelPadding));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(mainPanelPadding, mainPanelPadding, mainPanelPadding, mainPanelPadding));

        JPanel gridPanel = createGridPanel(rows, cols);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(BORDER_LAYOUT, BORDER_LAYOUT));
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
        JPanel panel = new JPanel(new GridLayout(rows, cols, BORDER_LAYOUT, BORDER_LAYOUT));
        for (int row = NO_ROWS; row < rows; row++) {
            for (int col = NO_ROWS; col < cols; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setEditable(false);
                cell.setFocusable(false);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD, FONT_SIZE));

                int tileSize = TITLE_SIZE;
                Dimension dimension = new Dimension(tileSize, tileSize);
                cell.setPreferredSize(dimension);
                cell.setMaximumSize(dimension);
                cell.setMinimumSize(dimension);

                gridFields[row][col] = cell;
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

        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid guess: " + exception.getMessage());
        } catch (IllegalStateException exception) {
            System.out.println("Game state error: " + exception.getMessage());
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
        for (int col = NO_ROWS; col < cols; col++) {
            if (col < guess.length()) {
                char ch = guess.charAt(col);
                gridFields[currentRow][col].setText(String.valueOf(ch));
                gridFields[currentRow][col].setBackground(
                        colorStrategy.getColorForState(states[col])
                );
            } else {
                gridFields[currentRow][col].setText("");
                gridFields[currentRow][col].setBackground(
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
        currentRow = NO_ROWS;

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
        public void actionPerformed(ActionEvent event) {
            submitGuess();
        }
    }
}
