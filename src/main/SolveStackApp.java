package main;

import javax.swing.*;
import ui.LoginUI;
import ui.UserSession;

public class SolveStackApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
                System.err.println("Unable to apply system look and feel: " + e.getMessage());
            }

            // Initialize session singleton early for consistent auth state handling across screens.
            UserSession.getInstance();
            new LoginUI().setVisible(true);
        });
    }
}
