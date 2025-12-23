package com.adliye;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasePartyDAO {

    public boolean addParty(int caseId, String partyName, String partyRole, Integer lawyerId) {
        String sql = """
            INSERT INTO case_parties (case_id, party_name, party_role, lawyer_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, caseId);
            ps.setString(2, partyName);
            ps.setString(3, partyRole);

            if (lawyerId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, lawyerId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CaseParty> getPartiesByCase(int caseId) {
        List<CaseParty> list = new ArrayList<>();
        String sql = """
            SELECT id, case_id, party_name, party_role, lawyer_id
            FROM case_parties
            WHERE case_id=?
            ORDER BY id ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, caseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CaseParty p = new CaseParty();
                p.setId(rs.getInt("id"));
                p.setCaseId(rs.getInt("case_id"));
                p.setPartyName(rs.getString("party_name"));
                p.setPartyRole(rs.getString("party_role"));
                int lw = rs.getInt("lawyer_id");
                p.setLawyerId(rs.wasNull() ? null : lw);
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
