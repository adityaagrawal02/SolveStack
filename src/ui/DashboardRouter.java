package ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Central router that translates a user role string into the correct JavaFX
 * dashboard scene and sets it on the primary stage.
 *
 * <p>This is a static-only utility class (private constructor). The two main
 * responsibilities are:</p>
 * <ol>
 *   <li><b>Role normalisation</b> — {@link #normalizeRole(String)} converts
 *       any recognised role variant (all-caps, prefixed, etc.) into the
 *       canonical capitalised form ("Developer", "Admin", etc.).</li>
 *   <li><b>Dashboard opening</b> — {@link #openDashboard(Stage, String)}
 *       builds the appropriate scene via {@link FxDashboardUI} and swaps it
 *       onto the provided or primary stage.</li>
 * </ol>
 */
public final class DashboardRouter {

    /** Prevent instantiation — this is a static-only utility class. */
    private DashboardRouter() {
        throw new UnsupportedOperationException("DashboardRouter cannot be instantiated.");
    }

    /**
     * Converts any recognised role variant into a canonical, capitalised form.
     *
     * <p>Handles:</p>
     * <ul>
     *   <li>All-uppercase strings: "DEVELOPER" → "Developer"</li>
     *   <li>Fully-qualified class name prefixes: "models.Developer" → "Developer"</li>
     *   <li>Spring/constants-style prefixes: "role_developer" → "Developer"</li>
     *   <li>Unknown strings: returned unchanged (trimmed).</li>
     * </ul>
     *
     * @param role The raw role string to normalise; may be {@code null}.
     * @return The normalised role string, or an empty string if {@code role} is null.
     */
    public static String normalizeRole(String role) {
        if (role == null) {
            return ""; // guard against null to avoid NullPointerException below
        }

        // Trim whitespace and convert to lowercase for a uniform comparison baseline.
        String normalized = role.trim().toLowerCase();

        // Strip the "models." package prefix if the fully-qualified class name was passed.
        if (normalized.startsWith("models.")) {
            normalized = normalized.substring("models.".length());
        }

        // Map every known variant to the canonical display form.
        return switch (normalized) {
            case "developer", "role_developer" -> "Developer";
            case "evaluator", "role_evaluator" -> "Evaluator";
            case "admin", "role_admin"         -> "Admin";
            case "company", "role_company"     -> "Company";
            // Unrecognised roles are returned trimmed but otherwise unchanged.
            default -> role.trim();
        };
    }

    /**
     * Creates the JavaFX {@link Scene} for the given role's dashboard.
     *
     * @param role  The role string (will be normalised internally).
     * @param stage The stage the scene will be placed on (needed by some panels
     *              for size bindings).
     * @return The fully built dashboard {@link Scene}.
     * @throws IllegalArgumentException if the normalised role is blank.
     */
    public static Scene createDashboard(String role, Stage stage) {
        String normalizedRole = normalizeRole(role);

        // Blank role would result in an invalid state — fail early with a clear message.
        if (normalizedRole.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        // Delegate scene construction to FxDashboardUI which knows the layout.
        return FxDashboardUI.createScene(stage, normalizedRole);
    }

    /**
     * Builds the dashboard scene for the given role and swaps it onto the
     * target stage, then ensures the stage is visible.
     *
     * <p>If {@code stage} is {@code null}, the application's primary stage
     * (from {@link FxSolveStackApp#getPrimaryStage()}) is used instead.</p>
     *
     * @param stage The stage to navigate to, or {@code null} for the primary stage.
     * @param role  The role string determining which dashboard to show.
     * @throws IllegalStateException if neither a stage nor a primary stage is available.
     */
    public static void openDashboard(Stage stage, String role) {
        // Fall back to the global primary stage if none was provided.
        Stage targetStage = stage != null ? stage : FxSolveStackApp.getPrimaryStage();

        if (targetStage == null) {
            throw new IllegalStateException("Primary stage is not initialized.");
        }

        // Build the scene and assign it to the stage.
        Scene dashboard = createDashboard(role, targetStage);

        // Update the window title to reflect the active dashboard.
        targetStage.setTitle("SolveStack - " + normalizeRole(role) + " Dashboard");
        targetStage.setScene(dashboard); // swap the scene (replaces current content)
        targetStage.show();              // bring the window to the front
    }

    /**
     * Compatibility overload retained for legacy call-sites that still pass a
     * Swing frame (or any other object) as the "current frame".
     *
     * <p>The first parameter is completely ignored; the primary stage is always
     * used.</p>
     *
     * @param ignoredCurrentFrame Ignored; kept only for API compatibility.
     * @param role                The role string.
     */
    public static void openDashboard(Object ignoredCurrentFrame, String role) {
        // Always use the primary stage regardless of what was passed as the first arg.
        openDashboard(FxSolveStackApp.getPrimaryStage(), role);
    }
}
