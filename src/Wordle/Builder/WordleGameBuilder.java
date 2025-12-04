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

    private int wordLength = 5;
    private int maxAttempts = 6;
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
        List<String> words = wordListFactory.createWordList(wordLength);
        if (words.isEmpty()) {
            throw new IllegalStateException("Word list is empty for length " + wordLength);
        }
        // Choose a random secret word
        String secret = words.get(new Random().nextInt(words.size()));
        return new WordleGame(secret, maxAttempts);
    }
}
