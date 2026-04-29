package ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application entry point and shared primary stage holder.
 *
 * <p>This class extends {@link Application} so the JavaFX runtime can
 * instantiate it and call {@link #start(Stage)} on the JavaFX Application
 * Thread (FXAT) once the platform is initialised.</p>
 *
 * <p>{@link #primaryStage} is stored statically so that any part of the
 * application can retrieve the main window without passing references around.
 * Use {@link #getPrimaryStage()} and {@link #setPrimaryStage(Stage)} for
 * access.</p>
 */
public class FxSolveStackApp extends Application {

    /** The single main window of the application, shared across all UI classes. */
    private static Stage primaryStage;

    /**
     * Returns the primary stage. Other UI classes use this to set a new scene
     * without creating a new window.
     *
     * @return The application's primary {@link Stage}, or {@code null} if not
     *         yet initialised.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Stores a reference to the primary stage. Called by {@link #start(Stage)}
     * on startup and by {@link LoginUI} and {@link SignupUI} whenever they
     * need to update it (e.g. on a new-stage navigation).
     *
     * @param stage The stage to register as the primary stage.
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * JavaFX lifecycle method — called once by the platform after
     * {@link Application#launch} has set up the FX environment.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Stores the provided {@link Stage} as the application's primary stage.</li>
     *   <li>Configures minimum and default window dimensions.</li>
     *   <li>Sets the window title.</li>
     *   <li>Delegates to {@link LoginUI#show(Stage)} to render the login screen.</li>
     * </ol>
     *
     * @param stage The primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;            // save the reference for global access

        stage.setMinWidth(1080);         // minimum width so content doesn't squash
        stage.setMinHeight(760);         // minimum height for usability
        stage.setWidth(Theme.WINDOW_WIDTH);   // preferred width from Theme constants
        stage.setHeight(Theme.WINDOW_HEIGHT); // preferred height from Theme constants
        stage.setTitle("SolveStack - Sign In"); // initial window title

        // Render the login screen into the primary stage.
        new LoginUI().show(stage);
    }
}
