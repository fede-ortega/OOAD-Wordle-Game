package WordleTest.FactoryTest;

import Wordle.Factory.SimpleWordFactory;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleWordFactoryTest {

    private final SimpleWordFactory factory = new SimpleWordFactory();

    private static final List<String> fourLetterWords = List.of(
            "RUBY", "BLUE", "NEON", "KING", "SOFT",
            "RAIN", "BOSS", "SANG", "JAZZ", "WILD",
            "BEEN", "THIS", "COLD", "HAND", "NAME"
    );

    private static final List<String> fiveLetterWords = List.of(
            "APPLE", "GRAPE", "HOUSE", "PLANT", "SMILE",
            "BRAVE", "GHOST", "LIGHT", "MONEY", "MOUSE",
            "TRAIN", "PLANE", "CHAIR", "TABLE", "WORLD"
    );

    private static final List<String> sixLetterWords = List.of(
            "BETTER", "SUMMER", "POCKET", "HONEST", "WINDOW",
            "ORCHID", "FINGER", "SCREAM", "INSIDE", "FRIEND",
            "ENOUGH", "DAMAGE", "ANYONE", "PRETTY", "LONELY"
    );

    @Test
    void chooseWordReturnsFourLetterWordWhenWordLengthIsFour() {
        String word = factory.chooseWord(4);

        assertNotNull(word, "Factory should never return null.");
        assertEquals(4, word.length(), "Word length should be 4 for input length 4.");
        assertTrue(fourLetterWords.contains(word),
                "Returned word should be one of the predefined four-letter words.");
    }

    @Test
    void chooseWordReturnsFiveLetterWordWhenWordLengthIsFive() {
        String word = factory.chooseWord(5);

        assertNotNull(word, "Factory should never return null.");
        assertEquals(5, word.length(), "Word length should be 5 for input length 5.");
        assertTrue(fiveLetterWords.contains(word),
                "Returned word should be one of the predefined five-letter words.");
    }

    @Test
    void chooseWordReturnsSixLetterWordWhenWordLengthIsSix() {
        String word = factory.chooseWord(6);

        assertNotNull(word, "Factory should never return null.");
        assertEquals(6, word.length(), "Word length should be 6 for input length 6.");
        assertTrue(sixLetterWords.contains(word),
                "Returned word should be one of the predefined six-letter words.");
    }

    @Test
    void chooseWordDefaultsToFiveLetterWordWhenWordLengthIsUnsupported() {
        String word = factory.chooseWord(10); // unsupported length

        assertNotNull(word, "Factory should never return null.");
        assertEquals(5, word.length(), "Unsupported lengths should default to a five-letter word.");
        assertTrue(fiveLetterWords.contains(word),
                "Returned word should still come from the five-letter word list.");
    }

    @RepeatedTest(20)
    void chooseWordIsRandomWithinTheCorrectListForFiveLetters() {
        String word = factory.chooseWord(5);
        assertTrue(fiveLetterWords.contains(word),
                "Randomly chosen word should always be in the five-letter list.");
    }

    @RepeatedTest(20)
    void chooseWordIsRandomWithinTheCorrectListForFourLetters() {
        String word = factory.chooseWord(4);
        assertTrue(fourLetterWords.contains(word),
                "Randomly chosen word should always be in the four-letter list.");
    }

    @RepeatedTest(20)
    void chooseWordIsRandomWithinTheCorrectListForSixLetters() {
        String word = factory.chooseWord(6);
        assertTrue(sixLetterWords.contains(word),
                "Randomly chosen word should always be in the six-letter list.");
    }
}
