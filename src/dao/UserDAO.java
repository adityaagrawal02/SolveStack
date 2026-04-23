package dao;

import db.DBConnection;
import java.sql.*;

public class UserDAO {

    public boolean register(String name, String email, String password, String role) {
        try {
            Connection con = DBConnection.getConnection();

            String q = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String email, String password) {
        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM users WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getRole(String email) {
        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT role FROM users WHERE email=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return rs.getString("role");

        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}