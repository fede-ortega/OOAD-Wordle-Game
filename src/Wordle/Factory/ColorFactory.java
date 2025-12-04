package Wordle.Factory;

import Wordle.Strategy.ColorStrategy;
import Wordle.Strategy.DefaultColorStrategy;
import Wordle.Strategy.FestiveColorStrategy;

public class ColorFactory {
    public static ColorStrategy createColorStrategy(String theme) {
        if (theme == null) {
            return new DefaultColorStrategy();
        }

        return switch(theme) {
            case "Festive" -> new FestiveColorStrategy();
            case "Something" -> new DefaultColorStrategy();
            default -> new DefaultColorStrategy();
        };
    }
}
