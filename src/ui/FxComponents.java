package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Factory class for reusable JavaFX UI components.
 *
 * <p>Every method here returns a pre-styled node so that dashboard and dialog
 * classes don't repeat CSS class assignments. All styling is driven by the CSS
 * classes added to nodes (e.g. {@code "primary-btn"}, {@code "metric-card"})
 * which are resolved by {@code solvestack.css}.</p>
 *
 * <p>This is a {@code final} non-instantiable utility class; all methods are
 * {@code static}.</p>
 */
public final class FxComponents {

    /** Prevent instantiation. */
    private FxComponents() {
    }

    /**
     * Runs a {@link Runnable} on the JavaFX Application Thread.
     *
     * <p>If the calling thread is already the FX thread, the action is executed
     * immediately. Otherwise it is queued via {@link Platform#runLater}, which
     * is the safe way to update the UI from a background thread.</p>
     *
     * @param action The UI action to run on the FX thread.
     */
    public static void runFx(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run(); // already on the FX thread, run directly
        } else {
            Platform.runLater(action); // schedule for the next FX pulse
        }
    }

    /**
     * Builds the top navigation bar with the SolveStack logo, a role badge,
     * and a sign-out button.
     *
     * @param role      The current user's role (used to build the badge label).
     * @param onLogout  Callback invoked when the user clicks "Sign out".
     * @return A styled {@link HBox} navigation bar.
     */
    public static HBox navbar(String role, Runnable onLogout) {
        // Logo panel without icon-only mode; font size 24px.
        LogoPanel logo = new LogoPanel(false, 24);

        // Sub-title shown below the logo in the nav bar.
        Label subtitle = new Label("Open Innovation Collaboration Platform");
        subtitle.getStyleClass().add("muted");

        // Stack logo and subtitle vertically, aligned to the left.
        VBox brandBox = new VBox(0, logo, subtitle);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        Label badge = roleBadge(role);            // coloured role chip
        Button logout = smallBtn("Sign out", onLogout); // small ghost-style button

        // Spacer pushes the badge and logout to the right edge.
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox nav = new HBox(14, brandBox, spacer, badge, logout);
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.getStyleClass().add("top-nav"); // CSS class for background + padding
        return nav;
    }

    /**
     * Creates a coloured role badge label.
     *
     * <p>The CSS class added (e.g. {@code "developer-badge"}) gives the badge
     * its unique colour per role.</p>
     *
     * @param role The role string to display and colour-code.
     * @return A {@link Label} styled as a role badge.
     */
    public static Label roleBadge(String role) {
        String normalizedRole = DashboardRouter.normalizeRole(role); // canonical form
        Label l = new Label(normalizedRole);
        l.getStyleClass().add("role-badge"); // shared badge base styles

        // Add a role-specific colour modifier class.
        switch (normalizedRole.toLowerCase()) {
            case "developer" -> l.getStyleClass().add("developer-badge");
            case "evaluator" -> l.getStyleClass().add("evaluator-badge");
            case "admin"     -> l.getStyleClass().add("admin-badge");
            default          -> l.getStyleClass().add("company-badge"); // Company + unknown
        }
        return l;
    }

    /**
     * Creates a circular avatar with the given initials.
     *
     * @param initials 1-2 character string shown inside the avatar circle.
     * @param tone     Optional CSS tone class (e.g. "avatar-sky"). Falls back
     *                 to {@code "avatar-sky"} if blank or null.
     * @return A {@link StackPane} rendered as a circular avatar.
     */
    public static StackPane avatar(String initials, String tone) {
        Label text = new Label(initials.toUpperCase()); // always uppercase initials
        text.getStyleClass().add("avatar-text");

        StackPane pane = new StackPane(text);
        // Add base class + optional tone; default to "avatar-sky" if no tone given.
        pane.getStyleClass().addAll("avatar", tone == null || tone.isBlank() ? "avatar-sky" : tone);
        pane.setPrefSize(34, 34); // fixed circle size
        pane.setMinSize(34, 34);
        return pane;
    }

    /**
     * Creates a primary (filled) action button.
     *
     * @param text   Button label text.
     * @param action Callback invoked on click; may be {@code null}.
     * @return A styled primary {@link Button}.
     */
    public static Button primaryBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("primary-btn");
        if (action != null) {
            btn.setOnAction(e -> action.run()); // wrap Runnable in EventHandler
        }
        return btn;
    }

    /**
     * Creates an outline (bordered, transparent background) button.
     *
     * @param text   Button label text.
     * @param action Callback invoked on click; may be {@code null}.
     * @return A styled outline {@link Button}.
     */
    public static Button outlineBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("outline-btn");
        if (action != null) {
            btn.setOnAction(e -> action.run());
        }
        return btn;
    }

    /**
     * Creates a ghost (minimal / text-only) button.
     *
     * @param text   Button label text.
     * @param action Callback invoked on click; may be {@code null}.
     * @return A styled ghost {@link Button}.
     */
    public static Button ghostBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("ghost-btn");
        if (action != null) {
            btn.setOnAction(e -> action.run());
        }
        return btn;
    }

    /**
     * Creates a compact ghost button (smaller padding than a regular ghost
     * button). Used in navigation bars and card footers.
     *
     * @param text   Button label text.
     * @param action Callback invoked on click; may be {@code null}.
     * @return A small ghost {@link Button}.
     */
    public static Button smallBtn(String text, Runnable action) {
        Button btn = ghostBtn(text, action); // reuse ghost styles
        btn.getStyleClass().add("compact-btn"); // override padding to be smaller
        return btn;
    }

    /**
     * Creates a metric display card with a large value and a descriptive label.
     *
     * @param label Descriptive text below the value (e.g. "Total Submissions").
     * @param value The numeric or string value to display prominently.
     * @return A {@link VBox} styled as a metric card.
     */
    public static VBox metric(String label, String value) {
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("metric-value"); // large, bold styling

        Label labelLabel = new Label(label);
        labelLabel.getStyleClass().add("metric-label"); // smaller, muted styling

        VBox card = new VBox(4, valueLabel, labelLabel); // 4px gap between value and label
        card.getStyleClass().add("metric-card");
        card.setMinWidth(200); // prevent cards from squashing in narrow layouts
        card.setAlignment(Pos.CENTER_LEFT);
        
        return card;
    }

    /**
     * Creates a surface-level content card that groups related nodes with
     * consistent padding and a subtle background.
     *
     * @param nodes Child nodes to add inside the card.
     * @return A {@link VBox} styled as a surface card.
     */
    public static VBox card(Node... nodes) {
        VBox card = new VBox(12); // 12px gap between children
        card.getStyleClass().add("surface-card");
        card.setPadding(new Insets(16));
        if (nodes != null) {
            card.getChildren().addAll(nodes);
        }
        return card;
    }

    /**
     * Creates a glass-morphism card (slightly transparent with a blur effect).
     * Used for hero / featured sections.
     *
     * @param nodes Child nodes to add inside the card.
     * @return A {@link VBox} styled as a glass card.
     */
    public static VBox glassCard(Node... nodes) {
        VBox card = new VBox(12);
        card.getStyleClass().add("glass-card");
        card.setPadding(new Insets(24)); // extra padding for the featured look
        if (nodes != null) {
            card.getChildren().addAll(nodes);
        }
        return card;
    }

    /**
     * Creates a bold section heading label.
     *
     * @param text The heading text.
     * @return A {@link Label} styled as a section title.
     */
    public static Label sectionHeader(String text) {
        Label header = new Label(text);
        header.getStyleClass().add("section-title");
        return header;
    }

    /**
     * Creates a form field label (lighter, smaller than a section header).
     *
     * @param text The label text.
     * @return A {@link Label} styled as a form field label.
     */
    public static Label formLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    /**
     * Creates a horizontal rule separator.
     *
     * @return A styled {@link Separator}.
     */
    public static Separator sep() {
        return new Separator();
    }

    /**
     * Creates a styled single-line text field.
     *
     * @param prompt Placeholder text shown when the field is empty.
     * @return A {@link TextField} with the {@code "text-input"} CSS class.
     */
    public static TextField textField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("text-input");
        return field;
    }

    /**
     * Creates a styled password field that masks input.
     *
     * @param prompt Placeholder text shown when the field is empty.
     * @return A {@link PasswordField} with the {@code "password-input"} CSS class.
     */
    public static PasswordField passwordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.getStyleClass().add("password-input");
        return field;
    }

    /**
     * Creates a styled multi-line text area with word wrap enabled and a
     * default row height of 4 lines.
     *
     * @param prompt Placeholder text shown when the area is empty.
     * @return A {@link TextArea} with the {@code "text-area-input"} CSS class.
     */
    public static TextArea textArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setWrapText(true);   // wrap long lines instead of horizontal scrolling
        area.setPrefRowCount(4);  // default height equivalent to 4 text rows
        area.getStyleClass().add("text-area-input");
        return area;
    }

    /**
     * Creates an inline notification banner (wrap-enabled label).
     *
     * @param text The notification message.
     * @return A {@link Label} styled as a notice banner.
     */
    public static Label notifBanner(String text) {
        Label notice = new Label(text);
        notice.getStyleClass().add("notice");
        notice.setWrapText(true); // allow multi-line messages
        return notice;
    }

    /**
     * Creates a horizontal row of nodes with 10px spacing, aligned to the
     * left.
     *
     * @param nodes Nodes to place in the row.
     * @return A left-aligned {@link HBox}.
     */
    public static HBox row(Node... nodes) {
        HBox row = new HBox(10); // 10px horizontal gap
        row.setAlignment(Pos.CENTER_LEFT);
        if (nodes != null) {
            row.getChildren().addAll(nodes);
        }
        return row;
    }

    /**
     * Shows a blocking INFORMATION alert dialog.
     *
     * <p>Must only be called on the JavaFX Application Thread.</p>
     *
     * @param title   Dialog header text.
     * @param message Dialog body text.
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait(); // block until the user dismisses the dialog
    }

    /**
     * Shows a blocking ERROR alert dialog.
     *
     * <p>Must only be called on the JavaFX Application Thread.</p>
     *
     * @param title   Dialog header text.
     * @param message Dialog body / error message.
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
