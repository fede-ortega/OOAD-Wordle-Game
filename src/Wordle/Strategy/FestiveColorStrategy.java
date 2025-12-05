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
            case ABSENT -> new Color(220, 61, 42);
            case PRESENT -> new Color(193, 165, 90);
            case CORRECT -> new Color(13, 89, 1);
            default -> Color.WHITE;
        };
    }
}