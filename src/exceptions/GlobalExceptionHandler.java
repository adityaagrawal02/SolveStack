package exceptions;

import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 * Centralized exception handling utilities for SolveStack.
 */
public final class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private GlobalExceptionHandler() {
    }

    public static void handle(SolveStackException ex) {
        logToConsole(ex);
    }

    public static void handle(Exception ex) {
        logToConsole(ex);
    }

    public static void handleAndShow(Exception ex, Component parent) {
        logToConsole(ex);
        showDialog(ex, parent);
    }

    public static void handleUserNotFound(UserNotFoundException ex, Component parent) {
        logToConsole(ex);
        JOptionPane.showMessageDialog(
                parent,
                "No account found for: \"" + ex.getIdentifier() + "\"\nPlease check your username or sign up.",
                "User Not Found",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void handleUnauthorized(UnauthorizedAccessException ex, Component parent) {
        logToConsole(ex);
        JOptionPane.showMessageDialog(
                parent,
                "Access Denied.\nYour role ('" + ex.getUserRole() + "') is not permitted to: " + ex.getAttemptedAction(),
                "Unauthorized Action",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void handleDeadlinePassed(SubmissionDeadlineException ex, Component parent) {
        logToConsole(ex);
        JOptionPane.showMessageDialog(
                parent,
                "Submission failed.\nThe deadline for \"" + ex.getChallengeTitle() + "\" passed on " + ex.getDeadline() + ".",
                "Deadline Passed",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void handleChallengeNotFound(ChallengeNotFoundException ex, Component parent) {
        logToConsole(ex);
        JOptionPane.showMessageDialog(
                parent,
                "Challenge not found (ID: " + ex.getChallengeId() + ").\nIt may have been removed.",
                "Challenge Not Found",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private static void logToConsole(Exception ex) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String code = (ex instanceof SolveStackException ssEx) ? ssEx.getErrorCode() : "UNEXPECTED_ERROR";

        System.err.println("============================================");
        System.err.println("[SolveStack ERROR] " + timestamp);
        System.err.println("Code    : " + code);
        System.err.println("Type    : " + ex.getClass().getSimpleName());
        System.err.println("Message : " + ex.getMessage());
        System.err.println("============================================");

        if (!(ex instanceof SolveStackException)) {
            ex.printStackTrace();
        }
    }

    private static void showDialog(Exception ex, Component parent) {
        String title;
        String message;
        int dialogType;

        if (ex instanceof UserNotFoundException userNotFoundException) {
            title = "User Not Found";
            message = "No account found for: \"" + userNotFoundException.getIdentifier() + "\"";
            dialogType = JOptionPane.WARNING_MESSAGE;
        } else if (ex instanceof UnauthorizedAccessException unauthorizedAccessException) {
            title = "Access Denied";
            message = "You do not have permission to perform this action.\n(" + unauthorizedAccessException.getAttemptedAction() + ")";
            dialogType = JOptionPane.ERROR_MESSAGE;
        } else if (ex instanceof SubmissionDeadlineException submissionDeadlineException) {
            title = "Deadline Passed";
            message = "The submission deadline for \"" + submissionDeadlineException.getChallengeTitle() + "\" has passed.";
            dialogType = JOptionPane.WARNING_MESSAGE;
        } else if (ex instanceof ChallengeNotFoundException challengeNotFoundException) {
            title = "Challenge Not Found";
            message = "Challenge (ID: " + challengeNotFoundException.getChallengeId() + ") could not be found.";
            dialogType = JOptionPane.WARNING_MESSAGE;
        } else if (ex instanceof SolveStackException solveStackException) {
            title = "Application Error";
            message = solveStackException.getMessage();
            dialogType = JOptionPane.ERROR_MESSAGE;
        } else {
            title = "Unexpected Error";
            message = "Something went wrong. Please restart the application.\n\nDetails: " + ex.getMessage();
            dialogType = JOptionPane.ERROR_MESSAGE;
        }

        JOptionPane.showMessageDialog(parent, message, title, dialogType);
    }
}