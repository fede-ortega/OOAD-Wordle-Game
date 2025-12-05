package WordleTest.BuilderTest;

import Wordle.Builder.WordleGameBuilder;
import Wordle.Factory.WordFactory;
import Wordle.Model.WordleGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameBuilderTest {

    private static class StubWordFactory implements WordFactory {
        private final String wordToReturn;
        private int lastRequestedLength = -1;

        StubWordFactory(String wordToReturn) {
            this.wordToReturn = wordToReturn;
        }

        @Override
        public String chooseWord(int wordLength) {
            this.lastRequestedLength = wordLength;
            return wordToReturn;
        }

        int getLastRequestedLength() {
            return lastRequestedLength;
        }
    }

    @Test
    void buildUsesConfiguredWordLengthAndAttempts() {
        int wordLength = 5;
        int maxAttempts = 6;
        String secretWord = "APPLE";

        StubWordFactory factory = new StubWordFactory(secretWord);

        WordleGameBuilder builder = new WordleGameBuilder()
                .withWordLength(wordLength)
                .withAttempts(maxAttempts)
                .withWordFactory(factory);

        WordleGame game = builder.build();

        assertNotNull(game, "Builder should create a non-null WordleGame");

        assertEquals(wordLength, factory.getLastRequestedLength(),
                "Builder should call chooseWord with the configured word length");

        assertEquals(secretWord, game.getSecretWord(),
                "Game should use the secret word returned by the factory");
        assertEquals(maxAttempts, game.getMaxAttempts(),
                "Game should use the configured max attempts");
    }

    @Test
    void buildThrowsIfFactoryReturnsNullWord() {
        WordFactory nullFactory = length -> null;

        WordleGameBuilder builder = new WordleGameBuilder()
                .withWordLength(5)
                .withAttempts(6)
                .withWordFactory(nullFactory);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                builder::build,
                "Builder should throw if the factory returns null"
        );

        assertTrue(
                ex.getMessage().contains("Word list is empty"),
                "Exception message should indicate empty word list"
        );
    }

    @Test
    void builderMethodsAreChainable() {
        StubWordFactory factory = new StubWordFactory("APPLE");

        WordleGameBuilder builder = new WordleGameBuilder()
                .withWordLength(5)
                .withAttempts(6)
                .withWordFactory(factory);

        WordleGame game = builder.build();

        assertNotNull(builder, "Builder chaining should not produce null");
        assertNotNull(game, "Builder should still build a game after chaining");
    }

    @Test
    void buildWithDifferentConfigsProducesDifferentGames() {
        StubWordFactory factory1 = new StubWordFactory("APPLE");
        StubWordFactory factory2 = new StubWordFactory("BRAIN");

        WordleGameBuilder builder1 = new WordleGameBuilder()
                .withWordLength(5)
                .withAttempts(6)
                .withWordFactory(factory1);

        WordleGameBuilder builder2 = new WordleGameBuilder()
                .withWordLength(5)
                .withAttempts(8)
                .withWordFactory(factory2);

        WordleGame game1 = builder1.build();
        WordleGame game2 = builder2.build();

        assertEquals("APPLE", game1.getSecretWord());
        assertEquals(6, game1.getMaxAttempts());

        assertEquals("BRAIN", game2.getSecretWord());
        assertEquals(8, game2.getMaxAttempts());
    }
}

