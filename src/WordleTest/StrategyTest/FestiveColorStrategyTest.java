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

        assertEquals(new Color(220, 61, 42), color, "ABSENT should map to RED in festive strategy");
    }

    @Test
    void presentReturnsOrange() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.PRESENT);

        assertEquals(new Color(193, 165, 90), color, "PRESENT should map to ORANGE in festive strategy");
    }

    @Test
    void correctReturnsGreen() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.CORRECT);

        assertEquals(new Color(13, 89, 1), color, "CORRECT should map to GREEN in festive strategy");
    }

    @Test
    void unknownReturnsWhite() {
        ColorStrategy strategy = new FestiveColorStrategy();

        Color color = strategy.getColorForState(LetterState.UNKNOWN);

        assertEquals(Color.WHITE, color, "UNKNOWN (default) should map to WHITE in festive strategy");
    }
}
