package Wordle.Factory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimpleWordFactory implements WordFactory {
    static private final Random rand = new Random();
    private static final int FOUR_LETTER = 4;
    private static final int SIX_LETTER = 6;

    @Override
    public String chooseWord(int wordLength) {
        List<String> fourLetterWords = Arrays.asList(
                "RUBY", "BLUE", "NEON", "KING", "SOFT",
                "RAIN", "BOSS", "SANG", "JAZZ", "WILD",
                "BEEN", "THIS", "COLD", "HAND", "NAME"
        );
        List<String> fiveLetterWords = Arrays.asList(
                "APPLE", "GRAPE", "HOUSE", "PLANT", "SMILE",
                "BRAVE", "GHOST", "LIGHT", "MONEY", "MOUSE",
                "TRAIN", "PLANE", "CHAIR", "TABLE", "WORLD"
        );
        List<String> sixLetterWords = Arrays.asList(
                "BETTER", "SUMMER", "POCKET", "HONEST", "WINDOW",
                "ORCHID", "FINGER", "SCREAM", "INSIDE", "FRIEND",
                "ENOUGH", "DAMAGE", "ANYONE", "PRETTY", "LONELY"
        );

        return switch (wordLength) {
            case FOUR_LETTER -> fourLetterWords.get(rand.nextInt(fourLetterWords.size()));
            case SIX_LETTER -> sixLetterWords.get(rand.nextInt(sixLetterWords.size()));
            default -> fiveLetterWords.get(rand.nextInt(fiveLetterWords.size()));
        };
    }
}
