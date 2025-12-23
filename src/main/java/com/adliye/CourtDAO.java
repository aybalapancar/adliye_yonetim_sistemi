package com.adliye;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourtDAO {

    public boolean addCourt(Court court) {
        String sql = "INSERT INTO courts (city, court_type) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, court.getCity());
            ps.setString(2, court.getCourtType());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourt(Court court) {
        String sql = "UPDATE courts SET city=?, court_type=? WHERE court_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, court.getCity());
            ps.setString(2, court.getCourtType());
            ps.setInt(3, court.getCourtId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Court> getAllCourts() {
        List<Court> list = new ArrayList<>();
        String sql = "SELECT * FROM courts ORDER BY court_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Court c = new Court();
                c.setCourtId(rs.getInt("court_id"));
                c.setCity(rs.getString("city"));
                c.setCourtType(rs.getString("court_type"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Court getCourtById(int id) {
        String sql = "SELECT * FROM courts WHERE court_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Court c = new Court();
                c.setCourtId(rs.getInt("court_id"));
                c.setCity(rs.getString("city"));
                c.setCourtType(rs.getString("court_type"));
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteCourt(int id) {
        String sql = "DELETE FROM courts WHERE court_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
