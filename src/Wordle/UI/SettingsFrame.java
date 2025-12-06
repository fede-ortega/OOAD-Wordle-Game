package Wordle.UI;

import javax.swing.*;
import java.util.Objects;

public class SettingsFrame extends JFrame {
    private static final int STRUT_SIZE = 10;

    private static final String[] LENGTH_OPTIONS = {"4", "5", "6"};
    private static final String[] ATTEMPT_OPTIONS = {"2", "3", "4", "5", "6", "7", "8", "9"};
    private static final String[] THEME_OPTIONS = {"Default", "Festive", "Halloween"};

    private static final String DEFAULT_LENGTH = "5";
    private static final String DEFAULT_ATTEMPTS = "5";
    private static final String DEFAULT_THEME = "Default";


    private final int wordLength;
    private final int maxAttempts;
    private final String theme;

    public SettingsFrame() {
        JComboBox<String> lengthBox = new JComboBox<>(LENGTH_OPTIONS);
        lengthBox.setSelectedItem(DEFAULT_LENGTH);

        JComboBox<String> attemptBox = new JComboBox<>(ATTEMPT_OPTIONS);
        attemptBox.setSelectedItem(DEFAULT_ATTEMPTS);

        JComboBox<String> themeBox = new JComboBox<>(THEME_OPTIONS);
        themeBox.setSelectedItem(DEFAULT_THEME);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Length:"));
        panel.add(lengthBox);
        panel.add(Box.createVerticalStrut(STRUT_SIZE));
        panel.add(new JLabel("Attempts:"));
        panel.add(attemptBox);
        panel.add(Box.createVerticalStrut(STRUT_SIZE));
        panel.add(new JLabel("Theme:"));
        panel.add(themeBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Wordle Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            throw new IllegalStateException("Setup fail");
        }

        wordLength = Integer.parseInt(Objects.requireNonNull(lengthBox.getSelectedItem()).toString());
        maxAttempts = Integer.parseInt(Objects.requireNonNull(attemptBox.getSelectedItem()).toString());
        theme =  Objects.requireNonNull(themeBox.getSelectedItem()).toString();
    }

    public int getWordLength() {
        return wordLength;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public String getTheme() {
        return theme;
    }
}
