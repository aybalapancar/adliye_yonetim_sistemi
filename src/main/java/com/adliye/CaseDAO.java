package com.adliye;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CaseDAO {

    // ================= MAP =================
    private Case map(ResultSet rs) throws SQLException {
        Case c = new Case();
        c.setCaseId(rs.getInt("case_id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        c.setCourtId(rs.getInt("court_id"));
        c.setCreatedBy(rs.getInt("created_by"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }

    // ================= TÜM DAVALAR =================
    public List<Case> getAllCases() {

        List<Case> list = new ArrayList<>();
        String sql = "SELECT * FROM cases ORDER BY case_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= STATUS'A GÖRE (FUNCTION) =================
    public List<Case> getCasesByStatusFunction(String status) {

        List<Case> list = new ArrayList<>();

        String sql = """
        SELECT *
        FROM cases
        WHERE UPPER(status) = UPPER(?)
        ORDER BY case_id DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public int addCaseReturnId(Case c) {
        String sql = """
            INSERT INTO cases (title, description, status, court_id, created_by)
            VALUES (?, ?, ?, ?, ?) RETURNING case_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getStatus());
            ps.setInt(4, c.getCourtId());
            ps.setInt(5, c.getCreatedBy());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("case_id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ================= ID İLE TEK DAVA =================
    public Case getCaseById(int id) {

        String sql = "SELECT * FROM cases WHERE case_id = ?";
        Case c = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public List<Case> getCasesByLawyer(int lawyerId) {

        List<Case> list = new ArrayList<>();

        String sql = """
        SELECT DISTINCT c.*
        FROM cases c
        JOIN case_parties cp ON c.case_id = cp.case_id
        WHERE cp.lawyer_id = ?
        ORDER BY c.case_id DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lawyerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Case> getCasesByLawyerAndStatus(int lawyerId, String status) {

        List<Case> list = new ArrayList<>();

        String sql = """
        SELECT DISTINCT c.*
        FROM cases c
        JOIN case_parties cp ON c.case_id = cp.case_id
        WHERE cp.lawyer_id = ?
          AND UPPER(c.status) = UPPER(?)
        ORDER BY c.case_id DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lawyerId);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean updateCase(Case c) {

        String sql = "UPDATE cases SET title = ?, description = ?, status = ? WHERE case_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getStatus());
            ps.setInt(4, c.getCaseId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean closeCase(int caseId) {
        String sql = "UPDATE cases SET status='kapalı' WHERE case_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, caseId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}