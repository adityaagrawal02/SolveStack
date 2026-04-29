package exceptions;

public class UserNotFoundException extends SolveStackException {

    private final String identifier;

    public UserNotFoundException(String identifier) {
        super("User not found: '" + identifier + "'", "USER_NOT_FOUND");
        this.identifier = identifier;
    }

    public UserNotFoundException(String identifier, String customMessage) {
        super(customMessage, "USER_NOT_FOUND");
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}