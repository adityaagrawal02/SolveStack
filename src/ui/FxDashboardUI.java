package ui;

import dao.ChallengeDAO;
import dao.EvaluationDAO;
import dao.SubmissionDAO;
import db.DBConnection;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 FULL REWRITTEN FXDASHBOARDUI
 SAME PREMIUM UI STRUCTURE
 REAL JDBC DATA
 NO CSS CHANGES
*/

public final class FxDashboardUI {

    private static final ChallengeDAO challengeDAO =
            new ChallengeDAO();

    private static final SubmissionDAO submissionDAO =
            new SubmissionDAO();

    private static final EvaluationDAO evaluationDAO =
            new EvaluationDAO();

    private FxDashboardUI() {
    }

    public static Scene createScene(Stage stage,
                                    String role) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        BorderPane contentArea =
                new BorderPane();

        contentArea.setTop(
                buildTopBar(stage, role)
        );

        contentArea.setCenter(
                buildBody(stage, role, "Dashboard")
        );

        VBox sidebar =
                buildSidebar(
                        stage,
                        role,
                        contentArea
                );

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene =
                new Scene(
                        root,
                        Theme.WINDOW_WIDTH,
                        Theme.WINDOW_HEIGHT
                );

        FxStyles.apply(scene);

        return scene;
    }

    /* ===================================================
       SIDEBAR
       =================================================== */

    private static VBox buildSidebar(Stage stage,
                                     String role,
                                     BorderPane area) {

        VBox sidebar =
                new VBox(8);

        sidebar.getStyleClass()
                .add("sidebar");

        sidebar.setPrefWidth(240);

        sidebar.setPadding(
                new Insets(32,16,24,16)
        );

        Label brand =
                new Label("SOLVESTACK");

        brand.getStyleClass()
                .addAll("brand","logo-solve");

        brand.setPadding(
                new Insets(0,0,40,12)
        );

        Button dash =
                navItem("▣ Dashboard", true);

        Button explore =
                navItem("🔍 Explore", false);

        Button myWork =
                navItem("📁 My Projects", false);

        Button messages =
                navItem("📤 Messages", false);

        Button settings =
                navItem("⚙ Settings", false);

        List<Button> nav =
                List.of(
                        dash,
                        explore,
                        myWork,
                        messages,
                        settings
                );

        dash.setOnAction(e ->
                updateNav(nav,dash,area,stage,role,
                        "Dashboard"));

        explore.setOnAction(e ->
                updateNav(nav,explore,area,stage,role,
                        "Explore"));

        myWork.setOnAction(e ->
                updateNav(nav,myWork,area,stage,role,
                        "My Projects"));

        messages.setOnAction(e ->
                updateNav(nav,messages,area,stage,role,
                        "Messages"));

        settings.setOnAction(e ->
                updateNav(nav,settings,area,stage,role,
                        "Settings"));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout =
                navItem("↪ Sign Out", false);

        logout.setOnAction(e -> {
            UserSession.getInstance().logout();
            new LoginUI().show(stage);
        });

        sidebar.getChildren().addAll(
                brand,dash,explore,myWork,
                messages,settings,
                spacer,logout
        );

        return sidebar;
    }

    private static void updateNav(
            List<Button> all,
            Button active,
            BorderPane area,
            Stage stage,
            String role,
            String view) {

        all.forEach(
                b -> b.getStyleClass()
                        .remove("nav-item-active")
        );

        active.getStyleClass()
                .add("nav-item-active");

        area.setCenter(
                buildBody(stage,role,view)
        );
    }

    private static Button navItem(
            String text,
            boolean active) {

        Button btn = new Button(text);

        btn.getStyleClass()
                .add("nav-item");

        if(active)
            btn.getStyleClass()
                    .add("nav-item-active");

        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setPadding(
                new Insets(12,16,12,16)
        );

        return btn;
    }

    /* ===================================================
       TOP BAR
       =================================================== */

    private static HBox buildTopBar(
            Stage stage,
            String role) {

        HBox top =
                new HBox(24);

        top.getStyleClass()
                .add("top-bar");

        top.setAlignment(Pos.CENTER_LEFT);

        top.setPadding(
                new Insets(16,32,16,32)
        );

        TextField search =
                new TextField();

        search.setPromptText(
                "Search anything..."
        );

        search.setPrefWidth(320);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label user =
                new Label(
                        UserSession
                                .getInstance()
                                .getCurrentUser()
                                .getUsername()
                );

        user.getStyleClass()
                .add("field-label");

        top.getChildren().addAll(
                search,
                spacer,
                user,
                FxComponents.roleBadge(role)
        );

        return top;
    }

    /* ===================================================
       ROUTER
       =================================================== */

    private static Node buildBody(
            Stage stage,
            String role,
            String view) {

        return switch (view) {

            case "Explore" ->
                    buildExploreView();

            case "My Projects" ->
                    buildMyProjectsView();

            case "Messages" ->
                    buildMessagesView();

            case "Settings" ->
                    buildSettingsView();

            default ->
                    buildDashboardView(
                            stage,
                            role
                    );
        };
    }

    /* ===================================================
       DASHBOARD
       =================================================== */

    private static ScrollPane buildDashboardView(
            Stage stage,
            String role) {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(32)
        );

        Label heading =
                new Label(role + " Overview");

        heading.getStyleClass()
                .add("page-title");

        Label sub =
                new Label(
                        roleSubtitle(role)
                );

        sub.getStyleClass()
                .add("muted");

        HBox metrics =
                new HBox(16);

        for(int i=0;i<4;i++) {

            metrics.getChildren().add(
                    FxComponents.metric(
                            metricLabel(role,i),
                            metricValue(role,i)
                    )
            );
        }

        HBox actions =
                new HBox(12);

        actions.getChildren()
                .addAll(
                        actionButtons(
                                stage,
                                role
                        )
                );

        VBox activity =
                FxComponents.glassCard();

        activity.getChildren()
                .add(
                        FxComponents
                                .sectionHeader(
                                        "Recent Activity"
                                )
                );

        for(String s :
                activityItems(role)) {

            activity.getChildren()
                    .add(activityRow(s));
        }

        HBox lower =
                new HBox(
                        16,
                        activity
                );

        HBox.setHgrow(
                activity,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                heading,
                sub,
                metrics,
                actions,
                lower
        );

        animateIn(content);

        ScrollPane sp =
                new ScrollPane(content);

        sp.setFitToWidth(true);

        return sp;
    }

    /* ===================================================
       EXPLORE
       =================================================== */

    private static ScrollPane buildExploreView() {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(32)
        );

        Label title =
                new Label(
                        "Explore Challenges"
                );

        title.getStyleClass()
                .add("page-title");

        VBox list =
                FxComponents.glassCard();

        List<String[]> rows =
                challengeDAO
                        .getAllOpenChallenges();

        for(String[] row : rows) {

            list.getChildren().add(
                    new Label(
                            row[0] + " | "
                                    + row[1] + " | ₹"
                                    + row[3]
                    )
            );
        }

        content.getChildren()
                .addAll(title,list);

        ScrollPane sp =
                new ScrollPane(content);

        sp.setFitToWidth(true);

        return sp;
    }

    /* ===================================================
       MY PROJECTS
       =================================================== */

    private static ScrollPane buildMyProjectsView() {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(32)
        );

        Label title =
                new Label("My Projects");

        title.getStyleClass()
                .add("page-title");

        VBox list =
                FxComponents.glassCard();

        User user =
                UserSession.getInstance()
                        .getCurrentUser();

        List<String[]> rows =
                submissionDAO
                        .getSubmissionsByDeveloper(
                                user.getUserId()
                        );

        for(String[] row : rows) {

            list.getChildren().add(
                    new Label(
                            row[0] + " | "
                                    + row[1] + " | "
                                    + row[2]
                    )
            );
        }

        content.getChildren()
                .addAll(title,list);

        ScrollPane sp =
                new ScrollPane(content);

        sp.setFitToWidth(true);

        return sp;
    }

    /* ===================================================
       SETTINGS
       =================================================== */

    private static ScrollPane buildSettingsView() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label heading = new Label("Settings");
        heading.getStyleClass().add("page-title");

        Label sub = new Label("Manage your account profile and account actions.");
        sub.getStyleClass().add("muted");

        models.User currentUser =
                UserSession.getInstance()
                        .getCurrentUser();

        TextField usernameField =
                FxComponents.textField("Username");

        TextField emailField =
                FxComponents.textField("Email");

        TextArea bioField =
                FxComponents.textArea("Short bio...");

        if (currentUser != null) {

            usernameField.setText(
                    currentUser.getUsername()
            );

            emailField.setText(
                    currentUser.getEmail()
            );

            if (currentUser.getProfileBio() != null) {
                bioField.setText(
                        currentUser.getProfileBio()
                );
            }
        }

        Button save =
                FxComponents.primaryBtn(
                        "Save Changes",
                        () -> {

                            boolean ok =
                                    UserRepository
                                            .getInstance()
                                            .updateUserProfile(
                                                    currentUser.getUsername(),
                                                    usernameField.getText(),
                                                    emailField.getText(),
                                                    bioField.getText()
                                            );

                            if (ok) {
                                FxComponents.showInfo(
                                        "Updated",
                                        "Profile updated successfully."
                                );
                            } else {
                                FxComponents.showError(
                                        "Failed",
                                        "Could not update profile."
                                );
                            }
                        }
                );

        Button delete =
                FxComponents.outlineBtn(
                        "Delete Account",
                        () -> {

                            boolean ok =
                                    UserRepository
                                            .getInstance()
                                            .deleteUser(
                                                    currentUser.getUserId()
                                            );

                            if (ok) {

                                FxComponents.showInfo(
                                        "Deleted",
                                        "Account removed."
                                );

                                UserSession
                                        .getInstance()
                                        .logout();

                                new LoginUI().setVisible(true);

                            } else {

                                FxComponents.showError(
                                        "Failed",
                                        "Could not delete account."
                                );
                            }
                        }
                );

        VBox form =
                FxComponents.card(
                        FxComponents.formLabel("Username"),
                        usernameField,

                        FxComponents.formLabel("Email"),
                        emailField,

                        FxComponents.formLabel("Bio"),
                        bioField,

                        new HBox(10, save, delete)
                );

        form.getStyleClass().add("form-card");
        form.setMaxWidth(650);

        content.getChildren().addAll(
                heading,
                sub,
                form
        );

        ScrollPane scroller =
                new ScrollPane(content);

        scroller.setFitToWidth(true);
        scroller.getStyleClass().add("page-scroll");

        return scroller;
    }

    /* ===================================================
       MESSAGES
       =================================================== */

    private static ScrollPane buildMessagesView() {

        VBox box =
                new VBox(20);

        box.setPadding(
                new Insets(32)
        );

        box.getChildren().add(
                new Label("Messages")
        );

        return new ScrollPane(box);
    }

    /* ===================================================
       METRICS
       =================================================== */

    private static String metricLabel(
            String role,
            int i) {

        return switch (
                role.toLowerCase()) {

            case "developer" ->
                    List.of(
                            "Active Challenges",
                            "My Submissions",
                            "Accepted",
                            "Success Rate"
                    ).get(i);

            case "company" ->
                    List.of(
                            "Live Challenges",
                            "Submissions",
                            "Pipeline",
                            "Conversion"
                    ).get(i);

            case "evaluator" ->
                    List.of(
                            "Pending",
                            "Reviewed",
                            "Queue",
                            "Efficiency"
                    ).get(i);

            default ->
                    List.of(
                            "Users",
                            "Challenges",
                            "Submissions",
                            "Health"
                    ).get(i);
        };
    }

    private static String metricValue(
            String role,
            int i) {

        try {

            User user =
                    UserSession.getInstance()
                            .getCurrentUser();

            SubmissionDAO sdao =
                    new SubmissionDAO();

            ChallengeDAO cdao =
                    new ChallengeDAO();

            switch (role.toLowerCase()) {

                case "developer" -> {

                    List<String[]> my =
                            sdao.getSubmissionsByDeveloper(
                                    user.getUserId()
                            );

                    long accepted =
                            my.stream()
                                    .filter(r ->
                                            r[3].equalsIgnoreCase("ACCEPTED"))
                                    .count();

                    return switch (i) {
                        case 0 -> String.valueOf(
                                cdao.getOpenChallengesCount()
                        );
                        case 1 -> String.valueOf(
                                my.size()
                        );
                        case 2 -> String.valueOf(
                                accepted
                        );
                        default -> my.isEmpty()
                                ? "0%"
                                : (accepted * 100 / my.size()) + "%";
                    };
                }

                case "company" -> {

                    List<String[]> my =
                            cdao.getChallengesByCompany(
                                    user.getUserId()
                            );

                    return switch (i) {
                        case 0 -> String.valueOf(my.size());
                        case 1 -> String.valueOf(
                                sdao.getTotalSubmissions()
                        );
                        case 2 -> "--";
                        default -> "--";
                    };
                }

                case "evaluator" -> {

                    return switch (i) {
                        case 0 -> String.valueOf(
                                sdao.getTotalSubmissions()
                        );
                        default -> "--";
                    };
                }

                default -> {

                    return switch (i) {
                        case 0 -> String.valueOf(
                                getTotalUsers()
                        );
                        case 1 -> String.valueOf(
                                cdao.getTotalChallenges()
                        );
                        case 2 -> String.valueOf(
                                sdao.getTotalSubmissions()
                        );
                        default -> "100%";
                    };
                }
            }

        } catch (Exception e) {

            return "--";
        }
    }

    /* ===================================================
       ACTIONS
       =================================================== */

    private static List<Button> actionButtons(
            Stage stage,
            String role) {

        return switch (role.toLowerCase()) {

            case "developer" -> List.of(

                    FxComponents.primaryBtn(
                            "Browse Challenges",
                            () -> new ChallengeUI(stage)
                                    .setVisible(true)
                    ),

                    FxComponents.outlineBtn(
                            "My Submissions",
                            () -> new SubmissionsUI(
                                    stage,
                                    "My Work"
                            ).setVisible(true)
                    )
            );

            case "company" -> List.of(

                    FxComponents.primaryBtn(
                            "Create Challenge",
                            () -> new CreateChallengeUI(stage)
                                    .setVisible(true)
                    ),

                    FxComponents.outlineBtn(
                            "View Challenges",
                            () -> new ChallengeUI(stage)
                                    .setVisible(true)
                    )
            );

            case "evaluator" -> List.of(

                    FxComponents.primaryBtn(
                            "Review Queue",
                            () -> new SubmissionsUI(
                                    stage,
                                    "Pending Reviews"
                            ).setVisible(true)
                    )
            );

            default -> List.of(

                    FxComponents.primaryBtn(
                            "Leaderboard",
                            () -> new LeaderboardUI(stage)
                                    .setVisible(true)
                    )
            );
        };
    }

    /* ===================================================
       HELPERS
       =================================================== */

    private static int getTotalUsers() {

        try(Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT COUNT(*) FROM users"
                    );

            ResultSet rs =
                    ps.executeQuery()) {

            if(rs.next())
                return rs.getInt(1);

        } catch(Exception ignored){}

        return 0;
    }

    private static Node activityRow(
            String text) {

        return new Label(text);
    }

    private static List<String> activityItems(String role) {

        List<String> items =
                new ArrayList<>();

        try {

            SubmissionDAO dao =
                    new SubmissionDAO();

            List<String[]> rows =
                    dao.getAllSubmissions();

            for (int i = 0;
                 i < rows.size() && i < 3;
                 i++) {

                String[] r = rows.get(i);

                items.add(
                        r[2] + " submitted to "
                                + r[1]
                                + " | "
                                + r[4]
                );
            }

        } catch (Exception ignored) {
        }

        if (items.isEmpty()) {

            items.add("No recent activity.");
        }

        return items;
    }

    private static String roleSubtitle(
            String role) {

        return "Welcome to SolveStack.";
    }

    private static void animateIn(
            VBox box) {

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(350),
                        box
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(350),
                        box
                );

        slide.setFromY(15);
        slide.setToY(0);

        fade.play();
        slide.play();
    }
}