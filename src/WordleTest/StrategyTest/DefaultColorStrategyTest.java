package WordleTest.StrategyTest;

import Wordle.Model.LetterState;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.DefaultColorStrategy;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultColorStrategyTest {

    @Test
    void nullStateReturnsWhite() {
        ColorStrategy strategy = new DefaultColorStrategy();

        Color color = strategy.getColorForState(null);

        assertEquals(Color.WHITE, color, "Null state should map to WHITE");
    }

    @Test
    void absentReturnsLightGray() {
        ColorStrategy strategy = new DefaultColorStrategy();

        Color color = strategy.getColorForState(LetterState.ABSENT);

        assertEquals(Color.LIGHT_GRAY, color, "ABSENT should map to LIGHT_GRAY");
    }

    @Test
    void presentReturnsYellow() {
        ColorStrategy strategy = new DefaultColorStrategy();

        Color color = strategy.getColorForState(LetterState.PRESENT);

        assertEquals(Color.YELLOW, color, "PRESENT should map to YELLOW");
    }

    @Test
    void correctReturnsGreen() {
        ColorStrategy strategy = new DefaultColorStrategy();

        Color color = strategy.getColorForState(LetterState.CORRECT);

        assertEquals(Color.GREEN, color, "CORRECT should map to GREEN");
    }

    @Test
    void unknownReturnsWhite() {
        ColorStrategy strategy = new DefaultColorStrategy();

        Color color = strategy.getColorForState(LetterState.UNKNOWN);

        assertEquals(Color.WHITE, color, "UNKNOWN (or any default case) should map to WHITE");
    }
}