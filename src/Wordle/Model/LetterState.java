package Wordle.Model;

/**
 * Represents the state of a letter after a guess.
 */
public enum LetterState {
    UNKNOWN,   // No information yet
    ABSENT,    // Letter does not appear in the word
    PRESENT,   // Letter appears but in a different position
    CORRECT    // Letter appears in this exact position
}
