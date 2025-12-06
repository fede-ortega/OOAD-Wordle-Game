package Wordle.Model;

import Wordle.Event.EventBus;

import java.util.HashMap;
import java.util.Map;

public class WordleGame {

    private static final int INITIAL_ATTEMPTS = 0;
    private static final int NO_OCCURRENCES = 0;

    private String secretWord;
    private final int maxAttempts;
    private int attempts;
    private boolean won;

    public WordleGame(String secretWord, int maxAttempts) {
        this.secretWord = secretWord.toUpperCase();
        this.maxAttempts = maxAttempts;
        this.attempts = INITIAL_ATTEMPTS;
        this.won = false;
    }

    public int getWordLength() {
        return secretWord.length();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isGameWon() {
        return won;
    }

    public boolean isGameOver() {
        return won || attempts >= maxAttempts;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public LetterState[] guess(String guess) {
        if (isGameOver()) {
            EventBus.getInstance().publish("The game is already over.");
            throw new IllegalStateException("Game is already over.");
        }

        if (guess == null) {
            throw new IllegalArgumentException("Guess cannot be null.");
        }

        guess = guess.trim().toUpperCase();

        if (guess.length() != secretWord.length()) {
            EventBus.getInstance().publish(
                    "<html>Your guess must have exactly <br>" + secretWord.length() + " letters.</html>"
            );
            throw new IllegalArgumentException("Guess has wrong length.");
        }
        else if (!guess.matches("[a-zA-Z]+")) {
            EventBus.getInstance().publish(
                    "<html>Your guess must<br>only have letters.</html>"
            );
            throw new IllegalArgumentException("Guess was invalid.");
        }

        attempts++;

        LetterState[] result = evaluateGuess(guess);

        if (guess.equals(secretWord)) {
            won = true;
            EventBus.getInstance().publish("<html>Congratulations!<br>You guessed the word!</html>");
        } else if (attempts >= maxAttempts) {
            EventBus.getInstance().publish("<html>No more attempts.<br>The word was: " + secretWord + "</html>");
        } else {
            // This is where we "display things like that letter is not correct"
            // via the Observer pattern.
            EventBus.getInstance().publish(
                    "<html>That word is not correct.<br>Attempt " + attempts + " of " + maxAttempts + ".</html>"
            );
        }

        return result;
    }

    private LetterState[] evaluateGuess(String guess) {
        int length = secretWord.length();
        LetterState[] states = new LetterState[length];

        Map<Character, Integer> counts = new HashMap<>();
        for (int i = NO_OCCURRENCES; i < length; i++) {
            char character = secretWord.charAt(i);
            counts.put(character, counts.getOrDefault(character, NO_OCCURRENCES) + 1);
        }

        for (int i = NO_OCCURRENCES; i < length; i++) {
            char guessCharacter = guess.charAt(i);
            char secretCharacter = secretWord.charAt(i);
            if (guessCharacter == secretCharacter) {
                states[i] = LetterState.CORRECT;
                counts.put(guessCharacter, counts.get(guessCharacter) - 1);
            }
        }

        for (int i = 0; i < length; i++) {
            if (states[i] == LetterState.CORRECT) {
                continue;
            }
            char guessCharacter = guess.charAt(i);
            Integer count = counts.get(guessCharacter);
            if (count != null && count > NO_OCCURRENCES) {
                states[i] = LetterState.PRESENT;
                counts.put(guessCharacter, count - 1);
            } else {
                states[i] = LetterState.ABSENT;
            }
        }

        return states;
    }

    public void reset(String secretWord) {
        this.secretWord = secretWord.toUpperCase();
        won = false;
        attempts = INITIAL_ATTEMPTS;
    }
}
