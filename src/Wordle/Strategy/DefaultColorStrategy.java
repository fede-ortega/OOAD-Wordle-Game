package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.Color;

/**
 * Default implementation of the ColorStrategy.
 * This is the classic Wordle color scheme:
 * - UNKNOWN  -> white
 * - ABSENT   -> light gray
 * - PRESENT  -> yellow
 * - CORRECT  -> green
 */
public class DefaultColorStrategy implements ColorStrategy {

    @Override
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