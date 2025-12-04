package Wordle.UI;

import javax.swing.*;
import java.util.Objects;

public class SettingsFrame extends JFrame {

    private final int wordLength;
    private final int maxAttempts;
    private final String theme;

    public SettingsFrame() {
        String[] lengths = {"4", "5", "6"};
        String[] attempts = {"2", "3", "4", "5", "6", "7", "8", "9"};
        String[] themes = {"Default", "Festive", "Halloween"};

        JComboBox<String> lengthBox = new JComboBox<>(lengths);
        JComboBox<String> attemptBox = new JComboBox<>(attempts);
        JComboBox<String> themeBox = new JComboBox<>(themes);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Length:"));
        panel.add(lengthBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Attempts:"));
        panel.add(attemptBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Theme:"));
        panel.add(themeBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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
