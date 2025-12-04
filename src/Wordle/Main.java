package Wordle;

import Wordle.Builder.WordleGameBuilder;
import Wordle.Factory.ColorFactory;
import Wordle.Factory.SimpleWordFactory;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;
import Wordle.UI.SettingsFrame;
import Wordle.UI.WordleFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Start Swing on the Wordle.Event Dispatch Thread
        SwingUtilities.invokeLater(Main::playGame);
    }

    private static void playGame() {
        SettingsFrame settings = new SettingsFrame();

        // Wordle.Builder pattern: configure and build the game object
        WordleGameBuilder builder = new WordleGameBuilder()
                .withAttempts(settings.getMaxAttempts())
                .withWordLength(settings.getWordLength())
                .withWordFactory(new SimpleWordFactory());

        WordleGame game = builder.build();

        // Wordle.Strategy pattern: choose how to color the tiles
        ColorStrategy colorStrategy = ColorFactory.createColorStrategy(settings.getTheme());

        // Create and show the main window (also an Observer)
        WordleFrame frame = new WordleFrame(game, colorStrategy);
        frame.setVisible(true);



        frame.setGameRestart(() -> {

            WordleGameBuilder newBuilder = new WordleGameBuilder()
                    .withAttempts(settings.getMaxAttempts())
                    .withWordLength(settings.getWordLength())
                    .withWordFactory(new SimpleWordFactory());

            WordleGame newGame = newBuilder.build();

            frame.resetGame(newGame, colorStrategy);
        });
    }
}
