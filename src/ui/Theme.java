package ui;

import javafx.scene.paint.Color;

/**
 * Global visual constants for SolveStack's JavaFX UI.
 *
 * <p>All colour tokens and default window dimensions are defined here so that
 * the look and feel can be updated in one place. Components reference these
 * constants instead of hard-coding hex strings.</p>
 *
 * <p>This is a {@code final} utility class with a private constructor; it
 * cannot be instantiated or subclassed.</p>
 */
public final class Theme {

    // ── Default window size ────────────────────────────────────────────────

    /** Default width of the main application window in pixels. */
    public static final double WINDOW_WIDTH  = 1280;

    /** Default height of the main application window in pixels. */
    public static final double WINDOW_HEIGHT = 820;

    // ── Brand palette ──────────────────────────────────────────────────────

    /** Primary brand blue used for buttons, links, and accents. */
    public static final Color PRIMARY       = Color.web("#2482FF");

    /** Darker shade of the primary blue, used for pressed/hover states. */
    public static final Color PRIMARY_DARK  = Color.web("#0F3F8D");

    /** Light tint of the primary blue, used for highlighted backgrounds. */
    public static final Color PRIMARY_LIGHT = Color.web("#DCEBFF");

    // ── Text colours ──────────────────────────────────────────────────────

    /** Main body text colour — dark navy for high contrast on white. */
    public static final Color TEXT_PRIMARY = Color.web("#0F2744");

    /** Secondary / muted text colour for captions and helper text. */
    public static final Color TEXT_MUTED   = Color.web("#52718F");

    // ── Status / semantic colours ─────────────────────────────────────────

    /** Green — used for "Accepted", success banners, and positive indicators. */
    public static final Color SUCCESS = Color.web("#2EA873");

    /** Orange — used for warnings, "Under Review" status, and caution states. */
    public static final Color WARNING = Color.web("#D8862F");

    /** Red — used for errors, "Rejected" status, and destructive actions. */
    public static final Color DANGER  = Color.web("#D44B4B");

    /** Private constructor prevents instantiation. */
    private Theme() {
    }
}
