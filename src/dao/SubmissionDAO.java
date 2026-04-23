package dao;

import db.DBConnection;
import java.sql.*;

public class SubmissionDAO {

    public boolean submitSolution(int challengeId,
                                  String developerEmail,
                                  String solution) {

        try {
            Connection con = DBConnection.getConnection();

            String q = "INSERT INTO submissions(challengeId, developerEmail, solution, status) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(q);

            ps.setInt(1, challengeId);
            ps.setString(2, developerEmail);
            ps.setString(3, solution);
            ps.setString(4, "Pending");

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllSubmissions() {

        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM submissions";

            PreparedStatement ps = con.prepareStatement(q);

            return ps.executeQuery();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getSubmissionsByChallenge(int challengeId) {

        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM submissions WHERE challengeId=?";

            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, challengeId);

            return ps.executeQuery();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateStatus(int submissionId, String status) {

        try {
            Connection con = DBConnection.getConnection();

            String q = "UPDATE submissions SET status=? WHERE id=?";

            PreparedStatement ps = con.prepareStatement(q);

            ps.setString(1, status);
            ps.setInt(2, submissionId);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}