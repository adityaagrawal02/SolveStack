package ui;

import java.net.URL;
import javafx.scene.Scene;

/**
 * Utility class responsible for applying the SolveStack CSS stylesheet to
 * JavaFX scenes.
 *
 * <p>All visual styling is driven by {@code solvestack.css}, which lives in the
 * same package as this class. Keeping styles in CSS keeps the Java code clean
 * and makes it easy to update the look-and-feel without recompiling.</p>
 *
 * <p>This is a {@code final} non-instantiable utility class; call
 * {@link #apply(Scene)} statically.</p>
 */
public final class FxStyles {

    /** Private constructor — no instances should ever be created. */
    private FxStyles() {
    }

    /**
     * Locates the bundled CSS file and adds it to the given scene's stylesheet
     * list.
     *
     * <p>If the CSS file cannot be found on the classpath (e.g. during
     * development or a misconfigured build), this method silently does nothing
     * rather than crashing. The UI will still appear but without custom styles.</p>
     *
     * @param scene The JavaFX {@link Scene} to style.
     */
    public static void apply(Scene scene) {
        // Look for the CSS file relative to this class's package location.
        URL cssUrl = FxStyles.class.getResource("solvestack.css");

        if (cssUrl != null) {
            // Add the stylesheet URL to the scene so JavaFX applies it to all
            // nodes whose styleClass matches a CSS selector.
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        // If cssUrl is null (file not found), we skip silently to avoid crashing.
    }
}
