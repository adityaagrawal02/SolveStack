package exceptions;

/**
 * Thrown when a user attempts an action they are not authorized to perform.
 */
public class UnauthorizedAccessException extends SolveStackException {

    private final String userRole;
    private final String attemptedAction;

    public UnauthorizedAccessException(String userRole, String attemptedAction) {
        super(
                "Unauthorized: Role '" + userRole + "' cannot perform action: '" + attemptedAction + "'",
                "UNAUTHORIZED_ACCESS"
        );
        this.userRole = userRole;
        this.attemptedAction = attemptedAction;
    }

    public UnauthorizedAccessException(String customMessage) {
        super(customMessage, "UNAUTHORIZED_ACCESS");
        this.userRole = "UNKNOWN";
        this.attemptedAction = "UNKNOWN";
    }

    public String getUserRole() {
        return userRole;
    }

    public String getAttemptedAction() {
        return attemptedAction;
    }
}