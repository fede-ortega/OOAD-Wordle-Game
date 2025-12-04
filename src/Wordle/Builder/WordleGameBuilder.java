package Wordle.Builder;

import Wordle.Factory.SimpleWordListFactory;
import Wordle.Factory.WordListFactory;
import Wordle.Model.WordleGame;

import java.util.List;
import java.util.Random;

/**
 * Wordle.Builder pattern: responsible for constructing a configured WordleGame.
 * It hides the details of which word list is used and how the secret word is chosen.
 */
public class WordleGameBuilder {

    private int wordLength;
    private int maxAttempts;
    private WordListFactory wordListFactory = new SimpleWordListFactory();

    public WordleGameBuilder withWordLength(int wordLength) {
        this.wordLength = wordLength;
        return this;
    }

    public WordleGameBuilder withAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public WordleGameBuilder withWordListFactory(WordListFactory factory) {
        this.wordListFactory = factory;
        return this;
    }

    public WordleGame build() {
        String word = wordListFactory.chooseWord(wordLength);
        if (word == null) {
            throw new IllegalStateException("Word list is empty for length " + wordLength);
        }
        return new WordleGame(word, maxAttempts);
    }
}
