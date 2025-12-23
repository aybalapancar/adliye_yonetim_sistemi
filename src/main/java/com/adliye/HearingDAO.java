package com.adliye;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HearingDAO {

    public boolean addHearing(Hearing h) {
        String sql = "INSERT INTO hearings (case_id, date, room, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, h.getCaseId());
            ps.setTimestamp(2, h.getDate());
            ps.setString(3, h.getRoom());
            ps.setString(4, h.getDescription());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHearing(Hearing h) {
        String sql = "UPDATE hearings SET date=?, room=?, description=? WHERE hearing_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, h.getDate());
            ps.setString(2, h.getRoom());
            ps.setString(3, h.getDescription());
            ps.setInt(4, h.getHearingId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHearing(int hearingId) {
        String sql = "DELETE FROM hearings WHERE hearing_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hearingId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Hearing> getHearingsByCase(int caseId) {
        List<Hearing> list = new ArrayList<>();
        String sql = "SELECT * FROM hearings WHERE case_id=? ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, caseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hearing h = new Hearing();
                h.setHearingId(rs.getInt("hearing_id"));
                h.setCaseId(rs.getInt("case_id"));
                h.setDate(rs.getTimestamp("date"));
                h.setRoom(rs.getString("room"));
                h.setDescription(rs.getString("description"));
                list.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
