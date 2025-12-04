package Wordle.Factory;

import java.util.List;

/**
 * Wordle.Factory pattern: this interface defines how to create
 * a list of candidate words for the game.
 */
public interface WordListFactory {

    /**
     * Creates and returns a list of candidate words.
     *
     * @param wordLength the desired length of each word.
     * @return a list of uppercase words with the given length.
     */
    String chooseWord(int wordLength);
}
