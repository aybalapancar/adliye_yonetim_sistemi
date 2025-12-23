package com.adliye;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public boolean addDocument(Document d) {
        String sql = "INSERT INTO documents (case_id, doc_name, filepath) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, d.getCaseId());
            ps.setString(2, d.getDocName());
            ps.setString(3, d.getFilepath());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDocument(Document d) {
        String sql = "UPDATE documents SET doc_name=?, filepath=? WHERE doc_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getDocName());
            ps.setString(2, d.getFilepath());
            ps.setInt(3, d.getDocId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDocument(int docId) {
        String sql = "DELETE FROM documents WHERE doc_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, docId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Document> getDocumentsByCase(int caseId) {
        List<Document> list = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE case_id=? ORDER BY uploaded_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, caseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Document d = new Document();
                d.setDocId(rs.getInt("doc_id"));
                d.setCaseId(rs.getInt("case_id"));
                d.setDocName(rs.getString("doc_name"));
                d.setFilepath(rs.getString("filepath"));
                d.setUploadedAt(rs.getTimestamp("uploaded_at"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
