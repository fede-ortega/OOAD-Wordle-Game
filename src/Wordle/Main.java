package Wordle;

import Wordle.Builder.WordleGameBuilder;
import Wordle.Factory.SimpleWordListFactory;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.DefaultColorStrategy;
import Wordle.UI.WordleFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Start Swing on the Wordle.Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Wordle.Builder pattern: configure and build the game object
            WordleGameBuilder builder = new WordleGameBuilder()
                    .withAttempts(5)
                    .withWordLength(5)
                    .withWordListFactory(new SimpleWordListFactory());

            WordleGame game = builder.build();

            // Wordle.Strategy pattern: choose how to color the tiles
            ColorStrategy colorStrategy = new DefaultColorStrategy();

            // Create and show the main window (also an Observer)
            WordleFrame frame = new WordleFrame(game, colorStrategy);
            frame.setVisible(true);
        });
    }
}
