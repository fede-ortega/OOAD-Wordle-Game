package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.*;

public class HalloweenColorStrategy extends ColorStrategy{
    @Override
    public Color getColorForState(LetterState state) {
        if (state == null) {
            return Color.WHITE;
        }

        return switch (state){
            case ABSENT -> Color.GRAY;
            case PRESENT  -> Color.ORANGE;
            case CORRECT -> new Color(128, 0, 128);
            default -> Color.BLACK;
        };
    }
}
