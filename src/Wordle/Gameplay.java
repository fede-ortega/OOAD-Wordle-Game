package Wordle;

import Wordle.Builder.WordleGameBuilder;
import Wordle.Factory.ColorFactory;
import Wordle.Factory.SimpleWordFactory;
import Wordle.Model.WordleGame;
import Wordle.Strategy.ColorStrategy;
import Wordle.UI.SettingsFrame;
import Wordle.UI.WordleFrame;

import javax.swing.*;

public class Gameplay {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gameplay::playGame);
    }

    private static void playGame() {
        SettingsFrame settings = new SettingsFrame();

        WordleGameBuilder builder = new WordleGameBuilder()
                .withAttempts(settings.getMaxAttempts())
                .withWordLength(settings.getWordLength())
                .withWordFactory(new SimpleWordFactory());

        WordleGame game = builder.build();

        ColorStrategy colorStrategy = ColorFactory.createColorStrategy(settings.getTheme());

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
