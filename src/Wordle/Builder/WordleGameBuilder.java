package Wordle.Builder;

import Wordle.Factory.SimpleWordFactory;
import Wordle.Factory.WordFactory;
import Wordle.Model.WordleGame;

/**
 * Wordle.Builder pattern: responsible for constructing a configured WordleGame.
 * It hides the details of which word list is used and how the secret word is chosen.
 */
public class WordleGameBuilder {

    private int wordLength;
    private int maxAttempts;
    private WordFactory wordFactory = new SimpleWordFactory();

    public WordleGameBuilder withWordLength(int wordLength) {
        this.wordLength = wordLength;
        return this;
    }

    public WordleGameBuilder withAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public WordleGameBuilder withWordFactory(WordFactory factory) {
        this.wordFactory = factory;
        return this;
    }

    public WordleGame build() {
        String word = wordFactory.chooseWord(wordLength);
        if (word == null) {
            throw new IllegalStateException("Word list is empty for length " + wordLength);
        }
        return new WordleGame(word, maxAttempts);
    }
}
