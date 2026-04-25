package ui;

import db.DBConnection;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /* =====================================================
       LOGIN / AUTHENTICATION
       ===================================================== */
    public User authenticate(String username, String password) {

        String sql =
                "SELECT user_id, username, email, password_hash, role " +
                        "FROM users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                String storedPassword = rs.getString("password_hash");

                // Plain text compare for now
                // Later replace with BCrypt
                if (!storedPassword.equals(password)) {
                    return null;
                }

                String userId = rs.getString("user_id");
                String email = rs.getString("email");
                String role = rs.getString("role");

                switch (role.toUpperCase()) {

                    case "COMPANY":
                        return loadCompany(con, userId, username, email, password);

                    case "DEVELOPER":
                        return loadDeveloper(con, userId, username, email, password);

                    case "EVALUATOR":
                        return loadEvaluator(con, userId, username, email, password);

                    case "ADMIN":
                        return loadAdmin(userId, username, email, password);

                    default:
                        return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =====================================================
       GET USER ROLE
       ===================================================== */
    public String getUserRole(String username) {

        String sql = "SELECT role FROM users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return normalizeRole(rs.getString("role"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* =====================================================
       REGISTER NORMAL USER
       Used for Developer / Evaluator / Admin
       dynamicValue = skill / expertise / level
       ===================================================== */
    public boolean registerUser(String username,
                                String email,
                                String password,
                                String role,
                                String dynamicValue) {

        String userId = generateUserId(role);

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            String userSql =
                    "INSERT INTO users " +
                            "(user_id, username, email, password_hash, role) " +
                            "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(userSql)) {

                ps.setString(1, userId);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, role.toUpperCase());

                ps.executeUpdate();
            }

            switch (role.toUpperCase()) {

                case "DEVELOPER":
                    insertDeveloper(con, userId, dynamicValue);
                    break;

                case "EVALUATOR":
                    insertEvaluator(con, userId, dynamicValue);
                    break;

                case "ADMIN":
                    insertAdmin(con, userId, dynamicValue);
                    break;
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =====================================================
       REGISTER COMPANY
       ===================================================== */
    public boolean registerCompany(String username,
                                   String email,
                                   String password,
                                   String companyName,
                                   String industry) {

        String userId = generateUserId("COMPANY");

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            String userSql =
                    "INSERT INTO users " +
                            "(user_id, username, email, password_hash, role) " +
                            "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(userSql)) {

                ps.setString(1, userId);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, "COMPANY");

                ps.executeUpdate();
            }

            String companySql =
                    "INSERT INTO companies " +
                            "(user_id, company_name, industry, registration_number) " +
                            "VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(companySql)) {

                ps.setString(1, userId);
                ps.setString(2, companyName);
                ps.setString(3, industry);
                ps.setString(4, "REG-" + System.currentTimeMillis());

                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =====================================================
       LOAD OBJECTS
       ===================================================== */

    private Company loadCompany(Connection con,
                                String userId,
                                String username,
                                String email,
                                String password) throws SQLException {

        String sql =
                "SELECT company_name, industry, registration_number " +
                        "FROM companies WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Company(
                            userId,
                            username,
                            email,
                            password,
                            rs.getString("company_name"),
                            rs.getString("industry"),
                            rs.getString("registration_number")
                    );
                }
            }
        }

        return null;
    }

    private Developer loadDeveloper(Connection con,
                                    String userId,
                                    String username,
                                    String email,
                                    String password) throws SQLException {

        String sql = "SELECT skill_set FROM developers WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Developer(
                            userId,
                            username,
                            email,
                            password,
                            rs.getString("skill_set")
                    );
                }
            }
        }

        return new Developer(userId, username, email, password, "");
    }

    private Evaluator loadEvaluator(Connection con,
                                    String userId,
                                    String username,
                                    String email,
                                    String password) throws SQLException {

        String sql = "SELECT expertise FROM evaluators WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Evaluator(
                            userId,
                            username,
                            email,
                            password,
                            rs.getString("expertise")
                    );
                }
            }
        }

        return new Evaluator(userId, username, email, password, "");
    }

    private Admin loadAdmin(String userId,
                            String username,
                            String email,
                            String password) {

        return new Admin(userId, username, email, password, "STANDARD");
    }

    /* =====================================================
       INSERT HELPERS
       ===================================================== */

    private void insertDeveloper(Connection con,
                                 String userId,
                                 String skill) throws SQLException {

        String sql =
                "INSERT INTO developers(user_id, skill_set) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, skill);
            ps.executeUpdate();
        }
    }

    private void insertEvaluator(Connection con,
                                 String userId,
                                 String expertise) throws SQLException {

        String sql =
                "INSERT INTO evaluators(user_id, expertise) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, expertise);
            ps.executeUpdate();
        }
    }

    private void insertAdmin(Connection con,
                             String userId,
                             String level) throws SQLException {

        String sql =
                "INSERT INTO admins(user_id, admin_level) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, level == null || level.isBlank()
                    ? "STANDARD"
                    : level);
            ps.executeUpdate();
        }
    }

    /* =====================================================
       UTILITIES
       ===================================================== */

    private String generateUserId(String role) {

        String prefix = switch (role.toUpperCase()) {
            case "COMPANY" -> "CO";
            case "DEVELOPER" -> "DEV";
            case "EVALUATOR" -> "EVL";
            case "ADMIN" -> "ADM";
            default -> "USR";
        };

        return prefix + "-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String normalizeRole(String role) {

        return switch (role.toUpperCase()) {
            case "COMPANY" -> "Company";
            case "DEVELOPER" -> "Developer";
            case "EVALUATOR" -> "Evaluator";
            case "ADMIN" -> "Admin";
            default -> role;
        };
    }
}