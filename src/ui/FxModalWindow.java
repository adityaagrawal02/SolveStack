package ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Abstract base class for modal dialog windows in the SolveStack UI.
 *
 * <p>Subclasses implement {@link #buildContent()} to provide the dialog's
 * root node. The base class handles all window lifecycle concerns: creating
 * the {@link Stage}, setting modality, applying the CSS stylesheet,
 * showing/hiding, and disposing.</p>
 *
 * <h3>Usage example</h3>
 * <pre>
 * public class MyDialog extends FxModalWindow {
 *     public MyDialog(Object parent) {
 *         super("My Dialog", 600, 400, parent);
 *     }
 *     {@literal @}Override
 *     protected Parent buildContent() {
 *         return new VBox(new Label("Hello!"));
 *     }
 * }
 * // Open it:
 * new MyDialog(ownerStage).setVisible(true);
 * </pre>
 */
public abstract class FxModalWindow {

    /** Text shown in the modal window's title bar. */
    private final String title;

    /** Preferred width of the modal window in pixels. */
    private final double width;

    /** Preferred height of the modal window in pixels. */
    private final double height;

    /**
     * The JavaFX window that "owns" this modal. Modality is set relative to
     * this window, so the modal always appears on top of it.
     */
    private final Window owner;

    /** The underlying JavaFX stage; created lazily in {@link #setVisible(boolean)}. */
    private Stage stage;

    /**
     * Constructs a new modal window descriptor.
     *
     * @param title  Title bar text.
     * @param width  Preferred window width.
     * @param height Preferred window height.
     * @param parent The owning window (a {@link Window} instance) or any other
     *               object; non-Window values cause the primary stage to be used
     *               as the owner instead.
     */
    protected FxModalWindow(String title, double width, double height, Object parent) {
        this.title  = title;
        this.width  = width;
        this.height = height;
        this.owner  = resolveOwner(parent); // convert the parent param to a Window
    }

    /**
     * Subclasses must implement this to provide the modal's UI content.
     *
     * <p>This method is called each time {@link #setVisible(boolean)} opens the
     * window, so the content is freshly built on every show.</p>
     *
     * @return The root {@link Parent} node to display inside the modal.
     */
    protected abstract Parent buildContent();

    /**
     * Shows or hides the modal window.
     *
     * <p>When {@code visible} is {@code true}:</p>
     * <ol>
     *   <li>If the stage has not been created yet, it is initialised with the
     *       configured title and modality.</li>
     *   <li>{@link #buildContent()} is called to get the current UI content.</li>
     *   <li>The CSS stylesheet is applied via {@link FxStyles#apply(Scene)}.</li>
     *   <li>The stage is shown and brought to the front.</li>
     * </ol>
     *
     * <p>When {@code visible} is {@code false}, {@link #dispose()} is called to
     * close the window.</p>
     *
     * <p>This method is safe to call from any thread — it dispatches UI work to
     * the JavaFX Application Thread via {@link FxComponents#runFx(Runnable)}.</p>
     *
     * @param visible {@code true} to open, {@code false} to close.
     */
    public void setVisible(boolean visible) {
        if (!visible) {
            dispose(); // hide + release resources
            return;
        }

        FxComponents.runFx(() -> {
            // Create the Stage only on the first call to setVisible(true).
            if (stage == null) {
                stage = new Stage();
                stage.setTitle(title);
                // WINDOW_MODAL means the user cannot interact with the owner window
                // while this modal is open.
                stage.initModality(Modality.WINDOW_MODAL);
                if (owner != null) {
                    stage.initOwner(owner); // tie modality to the owner window
                }
            }

            // Build fresh content each time the modal is opened.
            Scene scene = new Scene(buildContent(), width, height);
            FxStyles.apply(scene);  // apply the SolveStack CSS stylesheet
            stage.setScene(scene);  // set the scene on the stage
            stage.show();           // make the window visible
            stage.toFront();        // ensure it appears above other windows
        });
    }

    /**
     * Closes (disposes) the modal window.
     *
     * <p>Safe to call from any thread. If the stage has not been created, this
     * is a no-op.</p>
     */
    public void dispose() {
        FxComponents.runFx(() -> {
            if (stage != null) {
                stage.close(); // close and release native resources
            }
        });
    }

    /**
     * Returns the modal's {@link Stage}, or {@code null} if it has not been
     * shown yet.
     *
     * @return The underlying {@link Stage}.
     */
    protected Stage getStage() {
        return stage;
    }

    /**
     * Resolves the {@code parent} parameter to a {@link Window} for modality
     * ownership.
     *
     * <p>If {@code parent} is already a {@link Window}, it is returned directly.
     * Otherwise the application's primary stage is used as a fallback.</p>
     *
     * @param parent The parent object provided to the constructor.
     * @return A {@link Window} to use as the modal's owner.
     */
    private Window resolveOwner(Object parent) {
        if (parent instanceof Window window) {
            return window; // parent is directly usable as an owner
        }
        // Fall back to the global primary stage if anything else was passed.
        return FxSolveStackApp.getPrimaryStage();
    }
}

