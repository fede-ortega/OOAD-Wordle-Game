package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.Color;

abstract public class ColorStrategy {

    public Color getColorForState(LetterState state) {
        if (state == null) {
            return Color.WHITE;
        }

        return switch (state) {
            case ABSENT -> Color.LIGHT_GRAY;
            case PRESENT -> Color.YELLOW;
            case CORRECT -> Color.GREEN;
            default -> Color.WHITE;
        };
    }
}
