package WordleTest.StrategyTest;

import Wordle.Model.LetterState;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.FestiveColorStrategy;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;


public class FestiveColorStrategyTest {

    @Test
    void nullStateReturnsWhite() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(null);

        assertEquals(Color.WHITE, color, "Null state should map to WHITE");
    }

    @Test
    void absentReturnsRed() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.ABSENT);

        assertEquals(Color.RED, color, "ABSENT should map to RED in festive strategy");
    }

    @Test
    void presentReturnsOrange() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.PRESENT);

        assertEquals(Color.ORANGE, color, "PRESENT should map to ORANGE in festive strategy");
    }

    @Test
    void correctReturnsGreen() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.CORRECT);

        assertEquals(Color.GREEN, color, "CORRECT should map to GREEN in festive strategy");
    }

    @Test
    void unknownReturnsWhite() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.UNKNOWN);

        assertEquals(Color.WHITE, color, "UNKNOWN (default) should map to WHITE in festive strategy");
    }
}
