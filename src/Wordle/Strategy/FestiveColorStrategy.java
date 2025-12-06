package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.Color;

public class FestiveColorStrategy extends ColorStrategy {

    private static final Color FESTIVE_RED = new Color(220, 61, 42);
    private static final Color FESTIVE_YELLOW = new Color(193, 165, 90);
    private static final Color FESTIVE_GREEN = new Color(13, 89, 1);

    @Override
    public Color getColorForState(LetterState state) {
        if (state == null) {
            return Color.WHITE;
        }

        return switch (state) {
            case ABSENT -> FESTIVE_RED;
            case PRESENT -> FESTIVE_YELLOW;
            case CORRECT -> FESTIVE_GREEN;
            default -> Color.WHITE;
        };
    }
}