package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.Color;

public class FestiveColorStrategy extends ColorStrategy {

    @Override
    public Color getColorForState(LetterState state) {
        if (state == null) {
            return Color.WHITE;
        }

        return switch (state) {
            case ABSENT -> Color.RED;
            case PRESENT -> Color.ORANGE;
            case CORRECT -> Color.GREEN;
            default -> Color.WHITE;
        };
    }
}