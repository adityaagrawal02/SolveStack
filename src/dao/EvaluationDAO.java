package dao;

import db.DBConnection;
import java.sql.*;

public class EvaluationDAO {

    public boolean evaluateSubmission(int submissionId,
                                      int score,
                                      String feedback) {

        try {
            Connection con = DBConnection.getConnection();

            String q = "INSERT INTO evaluations(submissionId, score, feedback) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(q);

            ps.setInt(1, submissionId);
            ps.setInt(2, score);
            ps.setString(3, feedback);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllEvaluations() {

        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM evaluations";

            PreparedStatement ps = con.prepareStatement(q);

            return ps.executeQuery();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getEvaluationBySubmission(int submissionId) {

        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM evaluations WHERE submissionId=?";

            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, submissionId);

            return ps.executeQuery();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}