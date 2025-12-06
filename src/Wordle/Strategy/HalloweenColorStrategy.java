package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.*;

public class HalloweenColorStrategy extends ColorStrategy{

    private static final Color HALLOWEEN_PURPLE = new Color(128, 0, 128);
    @Override
    public Color getColorForState(LetterState state) {
        if (state == null) {
            return Color.WHITE;
        }

        return switch (state){
            case ABSENT -> Color.GRAY;
            case PRESENT  -> Color.ORANGE;
            case CORRECT -> HALLOWEEN_PURPLE;
            default -> Color.BLACK;
        };
    }
}
