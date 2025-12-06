package WordleTest.StrategyTest;

import Wordle.Model.LetterState;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.HalloweenColorStrategy;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class HalloweenColorStrategyTest {

    @Test
    void nullStateReturnsWhite() {
        ColorStrategy strategy = new HalloweenColorStrategy();

        Color color = strategy.getColorForState(null);

        assertEquals(Color.WHITE, color, "Null state should map to WHITE");
    }

    @Test
    void absentReturnsGray() {
        ColorStrategy strategy = new HalloweenColorStrategy();

        Color color = strategy.getColorForState(LetterState.ABSENT);

        assertEquals(Color.GRAY, color, "ABSENT should map to GRAY in Halloween strategy");
    }

    @Test
    void presentReturnsOrange() {
        ColorStrategy strategy = new HalloweenColorStrategy();

        Color color = strategy.getColorForState(LetterState.PRESENT);

        assertEquals(Color.ORANGE, color, "PRESENT should map to ORANGE in Halloween strategy");
    }

    @Test
    void correctReturnsPurple() {
        ColorStrategy strategy = new HalloweenColorStrategy();

        Color color = strategy.getColorForState(LetterState.CORRECT);

        Color expectedPurple = new Color(128, 0, 128);
        assertEquals(expectedPurple, color, "CORRECT should map to purple (128,0,128) in Halloween strategy");
    }

    @Test
    void unknownReturnsBlack() {
        ColorStrategy strategy = new HalloweenColorStrategy();

        Color color = strategy.getColorForState(LetterState.UNKNOWN);

        assertEquals(Color.BLACK, color, "UNKNOWN (default) should map to BLACK in Halloween strategy");
    }
}

