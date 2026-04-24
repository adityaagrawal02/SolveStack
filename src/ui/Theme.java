package ui;

import java.awt.*;

public class Theme {
    // Brand colors
    public static final Color PRIMARY       = new Color(15, 108, 189);
    public static final Color PRIMARY_DARK  = new Color(11, 78, 136);
    public static final Color PRIMARY_LIGHT = new Color(225, 240, 255);

    // Backgrounds and surfaces
    public static final Color BG_WHITE      = new Color(255, 255, 255);
    public static final Color BG_LIGHT      = new Color(240, 245, 252);
    public static final Color BG_SECONDARY  = new Color(231, 239, 249);
    public static final Color BORDER        = new Color(188, 203, 221);

    // Typography colors
    public static final Color TEXT_PRIMARY  = new Color(17, 33, 60);
    public static final Color TEXT_MUTED    = new Color(88, 106, 134);

    public static final Color GREEN_BG   = new Color(223, 245, 230);
    public static final Color GREEN_TEXT = new Color(21, 107, 61);
    public static final Color AMBER_BG   = new Color(255, 241, 214);
    public static final Color AMBER_TEXT = new Color(145, 95, 20);
    public static final Color TEAL_BG    = new Color(218, 241, 248);
    public static final Color TEAL_TEXT  = new Color(16, 103, 138);
    public static final Color CORAL_BG   = new Color(252, 228, 221);
    public static final Color CORAL_TEXT = new Color(145, 67, 43);
    public static final Color GRAY_BG    = new Color(233, 238, 245);
    public static final Color GRAY_TEXT  = new Color(92, 106, 126);

    // Status
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color WARNING = new Color(245, 158, 11);

    // Fonts
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_METRIC = new Font("Segoe UI", Font.BOLD, 24);

    // Dimension helpers
    public static final Dimension WINDOW = new Dimension(1200, 800);

    private Theme() {}
}
