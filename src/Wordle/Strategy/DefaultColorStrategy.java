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

        switch (state) {
            case ABSENT:
                return Color.LIGHT_GRAY;
            case PRESENT:
                return Color.YELLOW;
            case CORRECT:
                return Color.GREEN;
            case UNKNOWN:
            default:
                return Color.WHITE;
        }
    }
}