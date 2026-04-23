package dao;

import db.DBConnection;
import java.sql.*;

public class ChallengeDAO {

    public boolean addChallenge(String title, String desc, String company, double reward) {
        try {
            Connection con = DBConnection.getConnection();

            String q = "INSERT INTO challenges(title,description,companyEmail,reward) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(q);

            ps.setString(1,title);
            ps.setString(2,desc);
            ps.setString(3,company);
            ps.setDouble(4,reward);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}