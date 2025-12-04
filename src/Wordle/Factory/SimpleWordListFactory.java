package Wordle.Factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple concrete implementation of the WordListFactory.
 * In a real game, this could read from a file or database.
 */
public class SimpleWordListFactory implements WordListFactory {

    @Override
    public List<String> createWordList(int wordLength) {
        // Basic hard-coded word list for the example.
        List<String> allWords = Arrays.asList(
                "APPLE", "GRAPE", "HOUSE", "PLANT", "SMILE",
                "BRAVE", "GHOST", "LIGHT", "MONEY", "MOUSE",
                "TRAIN", "PLANE", "CHAIR", "TABLE", "WORLD"
        );

        List<String> result = new ArrayList<>();
        for (String w : allWords) {
            if (w.length() == wordLength) {
                result.add(w);
            }
        }
        return result;
    }
}
