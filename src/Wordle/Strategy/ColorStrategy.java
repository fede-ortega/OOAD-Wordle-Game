package Wordle.Strategy;

import Wordle.Model.LetterState;

import java.awt.Color;

/**
 * Wordle.Strategy pattern: different implementations of this interface
 * can decide how a given LetterState should be shown on the screen.
 * By changing the ColorStrategy, we can change how letters move
 * from white -> yellow -> green, etc.
 */
public interface ColorStrategy {

    /**
     * Returns the background color to use for a letter with the given state.
     */
    Color getColorForState(LetterState state);
}
