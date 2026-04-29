package ui;

import models.User;

/**
 * Singleton class that holds the currently authenticated user's session data.
 *
 * <p>Only one instance of {@code UserSession} exists at any time (Singleton
 * pattern). Every part of the UI accesses the same object via
 * {@link #getInstance()}, so the logged-in user is globally accessible without
 * passing it through constructor arguments.</p>
 *
 * <p>The session is cleared when {@link #logout()} is called, ensuring that
 * no user data lingers after sign-out.</p>
 */
public class UserSession {

    /** The sole instance of this class; created lazily on first access. */
    private static UserSession instance;

    /** The currently logged-in user model object, or {@code null} if signed out. */
    private User currentUser;

    /**
     * The normalised role string of the logged-in user (e.g. "Developer").
     * Stored separately so the UI can quickly check the role without casting
     * the user object.
     */
    private String userRole;

    /** Private constructor enforces Singleton — no external instantiation. */
    private UserSession() {
    }

    /**
     * Returns the singleton {@code UserSession} instance.
     *
     * <p>If no instance exists yet, one is created now (lazy initialisation).
     * Note: this implementation is not thread-safe; SolveStack runs on a single
     * JavaFX thread, so this is acceptable for this project.</p>
     *
     * @return The application-wide {@code UserSession} instance.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession(); // create on first access
        }
        return instance;
    }

    /**
     * Stores the authenticated user and their normalised role.
     *
     * <p>Called by {@link LoginUI} immediately after a successful login so that
     * all subsequent UI code can retrieve user details without hitting the
     * database again.</p>
     *
     * @param user The authenticated {@link User} model object.
     * @param role The raw role string returned by the authentication layer
     *             (normalised internally via {@link DashboardRouter#normalizeRole}).
     */
    public void setCurrentUser(User user, String role) {
        this.currentUser = user;
        // Normalise the role string so it always matches the expected format
        // (e.g. "Developer" instead of "DEVELOPER" or "models.Developer").
        this.userRole = DashboardRouter.normalizeRole(role);
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return The {@link User} object, or {@code null} if no one is signed in.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the normalised role string of the current user.
     *
     * @return The role, e.g. "Developer", "Admin", or {@code null} if signed out.
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Checks whether a user is currently signed in.
     *
     * @return {@code true} if {@link #currentUser} is non-null.
     */
    public boolean isLoggedIn() {
        return currentUser != null; // non-null means someone is signed in
    }

    /**
     * Signs out the current user by clearing both the user reference and the
     * role. After this call, {@link #isLoggedIn()} returns {@code false}.
     */
    public void logout() {
        currentUser = null; // remove user reference so it can be garbage-collected
        userRole = null;    // clear role
    }

    /**
     * Checks whether the current user holds a specific role.
     *
     * <p>Both the stored role and the requested role are normalised before
     * comparison, so {@code hasRole("developer")}, {@code hasRole("DEVELOPER")},
     * and {@code hasRole("Developer")} all work correctly.</p>
     *
     * @param role The role to test for.
     * @return {@code true} if the user is logged in AND their role matches.
     */
    public boolean hasRole(String role) {
        // Guard: must be logged in and have a non-null role first.
        return isLoggedIn() && userRole != null
                && userRole.equalsIgnoreCase(DashboardRouter.normalizeRole(role));
    }
}
