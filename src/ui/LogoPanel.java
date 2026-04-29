package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * A reusable logo component that renders the SolveStack brand mark.
 *
 * <p>The logo is displayed in two layers:</p>
 * <ol>
 *   <li>An image loaded from {@code assets/logo.png} (if available on the classpath).</li>
 *   <li>A CSS-styled cube icon fallback if the image file is not found.</li>
 * </ol>
 *
 * <p>Below the icon, two {@link Label} nodes spell out "Solve" (in a blue
 * colour) and "Stack" (in a contrasting colour) side by side. Pass
 * {@code iconOnly = true} to skip the word mark.</p>
 *
 * <p>This class extends {@link VBox} so it can be added directly to any
 * layout container.</p>
 */
public class LogoPanel extends VBox {

    /**
     * Creates a logo panel with default settings (not icon-only, 24px font).
     */
    public LogoPanel() {
        this(false, 24); // default: show word mark at 24px
    }

    /**
     * Creates a logo panel with a custom icon-only flag and default font size.
     *
     * @param iconOnly If {@code true}, only the icon/image is shown; the
     *                 "SolveStack" word mark is hidden.
     */
    public LogoPanel(boolean iconOnly) {
        this(iconOnly, 24);
    }

    /**
     * Creates a fully customised logo panel.
     *
     * @param iconOnly If {@code true}, only the icon/image is shown.
     * @param fontSize Font size in pixels for the "Solve" and "Stack" labels.
     */
    public LogoPanel(boolean iconOnly, double fontSize) {
        // Configure the VBox itself (this class IS the container).
        setAlignment(Pos.CENTER);
        setSpacing(16); // space between the icon row and the word-mark row

        // ── Step 1: Try to load the logo image from the classpath ──────────
        javafx.scene.image.ImageView logoView = null;
        try {
            javafx.scene.image.Image logoImage =
                    new javafx.scene.image.Image(
                            getClass().getResourceAsStream("assets/logo.png")
                    );

            // Only use the image if it loaded without errors.
            if (logoImage != null && !logoImage.isError()) {
                logoView = new javafx.scene.image.ImageView(logoImage);
                logoView.setFitHeight(fontSize * 2.2); // scale height relative to font size
                logoView.setPreserveRatio(true);        // keep original aspect ratio
                logoView.setSmooth(true);               // enable anti-aliasing
                logoView.setCache(true);                // cache the rendered image
            }
        } catch (Exception e) {
            // Image load failed (file missing, stream null, etc.) — fall through to CSS fallback.
        }

        // ── Step 2: Add either the image or the CSS cube fallback ───────────
        if (logoView != null) {
            getChildren().add(logoView); // use the loaded PNG image
        } else {
            // CSS fallback: a StackPane with class "logo-cube" styled in CSS.
            StackPane cube = new StackPane();
            cube.getStyleClass().add("logo-cube");
            cube.setMinSize(fontSize, fontSize);  // size it relative to the font
            cube.setPrefSize(fontSize, fontSize);
            getChildren().add(cube);
        }

        // ── Step 3: Optionally add the "SolveStack" word mark ───────────────
        if (iconOnly) return; // skip the word mark if icon-only mode is requested

        // "Solve" label — styled in the primary brand blue.
        Label solve = new Label("Solve");
        solve.getStyleClass().add("logo-solve");
        solve.setStyle("-fx-font-size: " + fontSize + "px;");

        // "Stack" label — styled in a contrasting colour.
        Label stack = new Label("Stack");
        stack.getStyleClass().add("logo-stack");
        stack.setStyle("-fx-font-size: " + fontSize + "px;");

        // Place both labels side-by-side with no gap between them.
        HBox word = new HBox(0, solve, stack);
        word.setAlignment(Pos.CENTER);

        getChildren().add(word); // add the word mark below the icon
    }
}
