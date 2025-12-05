package WordleTest.FactoryTest;

import Wordle.Factory.ColorFactory;
import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.DefaultColorStrategy;
import Wordle.Strategy.FestiveColorStrategy;
import Wordle.Strategy.HalloweenColorStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorFactoryTest {

    @Test
    void createColorStrategyReturnsDefaultColorStrategyWhenThemeIsNull() {
        ColorStrategy strategy = ColorFactory.createColorStrategy(null);

        assertNotNull(strategy, "Strategy should never be null.");
        assertInstanceOf(DefaultColorStrategy.class, strategy, "Null theme should result in DefaultColorStrategy.");
    }

    @Test
    void createColorStrategyReturnsFestiveColorStrategyWhenThemeIsFestive() {
        ColorStrategy strategy = ColorFactory.createColorStrategy("Festive");

        assertNotNull(strategy, "Strategy should never be null.");
        assertInstanceOf(FestiveColorStrategy.class, strategy, "Theme 'Festive' should create a FestiveColorStrategy.");
    }

    @Test
    void createColorStrategyReturnsHalloweenColorStrategyWhenThemeIsHalloween() {
        ColorStrategy strategy = ColorFactory.createColorStrategy("Halloween");

        assertNotNull(strategy, "Strategy should never be null.");
        assertInstanceOf(HalloweenColorStrategy.class, strategy, "Theme 'Halloween' should create a HalloweenColorStrategy.");
    }

    @Test
    void createColorStrategyReturnsDefaultColorStrategyForUnknownTheme() {
        ColorStrategy strategy = ColorFactory.createColorStrategy("UnknownTheme");

        assertNotNull(strategy, "Strategy should never be null.");
        assertInstanceOf(DefaultColorStrategy.class, strategy, "Unknown theme should fall back to DefaultColorStrategy.");
    }

    @Test
    void createColorStrategyReturnsNewInstancesEachTime() {
        ColorStrategy strategy1 = ColorFactory.createColorStrategy("Festive");
        ColorStrategy strategy2 = ColorFactory.createColorStrategy("Festive");

        assertNotSame(strategy1, strategy2,
                "Factory should return a new strategy instance on each call.");
    }
}
