package Wordle.Model;

import Wordle.Event.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Core game logic for Wordle.
 * It knows the secret word and evaluates guesses.
 */
public class WordleGame {

    private final String secretWord;
    private final int maxAttempts;
    private int attempts;
    private boolean won;

    public WordleGame(String secretWord, int maxAttempts) {
        this.secretWord = secretWord.toUpperCase();
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
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

    /**
     * Applies a guess and returns the resulting state for each letter.
     * This method also uses the EventBus (Observer pattern) to notify
     * observers about messages such as errors or end of game.
     */
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
                    "Your guess must have exactly " + secretWord.length() + " letters."
            );
            throw new IllegalArgumentException("Guess has wrong length.");
        }

        attempts++;

        LetterState[] result = evaluateGuess(guess);

        if (guess.equals(secretWord)) {
            won = true;
            EventBus.getInstance().publish("Congratulations! You guessed the word!");
        } else if (attempts >= maxAttempts) {
            EventBus.getInstance().publish("No more attempts. The word was: " + secretWord);
        } else {
            // This is where we "display things like that letter is not correct"
            // via the Observer pattern.
            EventBus.getInstance().publish(
                    "That word is not correct. Attempt " + attempts + " of " + maxAttempts + "."
            );
        }

        return result;
    }

    /**
     * Evaluates a guess and returns the LetterState for each character.
     * This implementation correctly handles duplicate letters.
     */
    private LetterState[] evaluateGuess(String guess) {
        int length = secretWord.length();
        LetterState[] states = new LetterState[length];

        // Count how many times each letter appears in the secret word.
        Map<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < length; i++) {
            char c = secretWord.charAt(i);
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        // First pass: mark CORRECT positions and update counts.
        for (int i = 0; i < length; i++) {
            char g = guess.charAt(i);
            char s = secretWord.charAt(i);
            if (g == s) {
                states[i] = LetterState.CORRECT;
                counts.put(g, counts.get(g) - 1);
            }
        }

        // Second pass: mark PRESENT or ABSENT for the remaining letters.
        for (int i = 0; i < length; i++) {
            if (states[i] == LetterState.CORRECT) {
                continue;
            }
            char g = guess.charAt(i);
            Integer count = counts.get(g);
            if (count != null && count > 0) {
                states[i] = LetterState.PRESENT;
                counts.put(g, count - 1);
            } else {
                states[i] = LetterState.ABSENT;
            }
        }

        return states;
    }
}
