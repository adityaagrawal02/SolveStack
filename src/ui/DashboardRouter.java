package ui;

import javax.swing.*;

public final class DashboardRouter {

    private DashboardRouter() {
        throw new UnsupportedOperationException("DashboardRouter cannot be instantiated.");
    }

    public static String normalizeRole(String role) {
        if (role == null) {
            return "";
        }

        String normalized = role.trim().toLowerCase();
        if (normalized.startsWith("models.")) {
            normalized = normalized.substring("models.".length());
        }

        return switch (normalized) {
            case "developer", "role_developer" -> "Developer";
            case "evaluator", "role_evaluator" -> "Evaluator";
            case "admin", "role_admin" -> "Admin";
            case "company", "role_company" -> "Company";
            default -> role.trim();
        };
    }

    public static JFrame createDashboard(String role) {
        String normalizedRole = normalizeRole(role);
        if (normalizedRole.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        return switch (normalizedRole.toLowerCase()) {
            case "developer" -> new DeveloperDashboardUI();
            case "evaluator" -> new EvaluatorDashboardUI();
            case "admin" -> new AdminDashboardUI();
            case "company" -> new CompanyDashboardUI();
            default -> throw new IllegalArgumentException("Unknown role: " + normalizedRole);
        };
    }

    public static void openDashboard(JFrame currentFrame, String role) {
        JFrame dashboard = createDashboard(role);
        dashboard.setVisible(true);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }
}
