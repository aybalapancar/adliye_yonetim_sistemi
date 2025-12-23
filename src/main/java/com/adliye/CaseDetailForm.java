package com.adliye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CaseDetailForm extends JFrame {

    private final Case currentCase;
    private final User currentUser;

    private JTable partyTable;
    private JTable documentTable;
    private JTable hearingTable;

    public CaseDetailForm(Case c, User u) {
        this.currentCase = c;
        this.currentUser = u;

        setTitle("Dava Detayı - " + c.getTitle());
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel);

        JLabel lbl = new JLabel("Dava ID: " + c.getCaseId() + " | Başlık: " + c.getTitle() + " | Durum: " + c.getStatus());
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lbl);

        panel.add(Box.createVerticalStrut(10));

        // TARAF
        DefaultTableModel pModel = new DefaultTableModel(new String[]{"ID","Ad Soyad","Rol","AvukatID"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        partyTable = new JTable(pModel);
        partyTable.setRowHeight(25);
        panel.add(new JLabel("Taraflar"));
        panel.add(new JScrollPane(partyTable));
        loadParties();

        panel.add(Box.createVerticalStrut(15));

        // EVRAK
        DefaultTableModel dModel = new DefaultTableModel(new String[]{"ID","Evrak","Dosya","Tarih"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        documentTable = new JTable(dModel);
        documentTable.setRowHeight(25);
        panel.add(new JLabel("Evraklar"));
        panel.add(new JScrollPane(documentTable));
        loadDocuments();

        JPanel docCrud = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnDocUpdate = new JButton("Evrak Güncelle");
        JButton btnDocDelete = new JButton("Evrak Sil");

        btnDocUpdate.addActionListener(e -> updateSelectedDocument());
        btnDocDelete.addActionListener(e -> deleteSelectedDocument());

        docCrud.add(btnDocUpdate);
        docCrud.add(btnDocDelete);
        panel.add(docCrud);

        panel.add(Box.createVerticalStrut(15));

        // DURUŞMA
        DefaultTableModel hModel = new DefaultTableModel(new String[]{"ID","Tarih","Oda","Açıklama"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        hearingTable = new JTable(hModel);
        hearingTable.setRowHeight(25);
        panel.add(new JLabel("Duruşmalar"));
        panel.add(new JScrollPane(hearingTable));
        loadHearings();

        // DURUŞMA GÜNCELLE / SİL (SADECE PERSONEL)
        if (currentUser.getRole().equals("personel")) {

            JPanel hearCrud = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JButton btnHearUpdate = new JButton("Duruşma Güncelle");
            JButton btnHearDelete = new JButton("Duruşma Sil");

            btnHearUpdate.addActionListener(e -> updateSelectedHearing());
            btnHearDelete.addActionListener(e -> deleteSelectedHearing());

            hearCrud.add(btnHearUpdate);
            hearCrud.add(btnHearDelete);
            panel.add(hearCrud);
        }


        panel.add(Box.createVerticalStrut(15));

        // ALT BUTONLAR (Yetki)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String role = currentUser.getRole();

        if (role.equals("personel") || role.equals("savcı") || role.equals("avukat")) {
            JButton addDoc = new JButton("Evrak Ekle");
            addDoc.setBackground(new Color(0, 80, 150));
            addDoc.setForeground(Color.WHITE);
            addDoc.addActionListener(e -> new DocumentAddForm(currentCase.getCaseId(), this));
            btnPanel.add(addDoc);
        }

        if (role.equals("personel")) {
            JButton addHear = new JButton("Duruşma Ekle");
            addHear.setBackground(new Color(0, 120, 80));
            addHear.setForeground(Color.WHITE);
            addHear.addActionListener(e -> new HearingAddForm(currentCase.getCaseId(), this));
            btnPanel.add(addHear);
        }

        if (role.equals("hakim") && "açık".equalsIgnoreCase(currentCase.getStatus())) {
            JButton close = new JButton("Davayı Kapat");
            close.setBackground(new Color(150, 40, 40));
            close.setForeground(Color.WHITE);
            close.addActionListener(e -> {
                if (new CaseDAO().closeCase(currentCase.getCaseId())) {
                    JOptionPane.showMessageDialog(this, "Dava kapatıldı!");
                    dispose();
                }
            });
            btnPanel.add(close);
        }

        JButton backBtn = new JButton("Geri");
        backBtn.addActionListener(e -> dispose());
        btnPanel.add(backBtn);

        panel.add(btnPanel);
        setVisible(true);
    }

    public void refreshDocumentTable() { loadDocuments(); }
    public void refreshHearingTable() { loadHearings(); }

    private void loadParties() {
        List<CaseParty> list = new CasePartyDAO().getPartiesByCase(currentCase.getCaseId());
        DefaultTableModel m = (DefaultTableModel) partyTable.getModel();
        m.setRowCount(0);
        for (CaseParty p : list) {
            m.addRow(new Object[]{
                    p.getId(),
                    p.getPartyName(),
                    p.getPartyRole(),
                    p.getLawyerId()
            });
        }
    }

    private void loadDocuments() {
        List<Document> list = new DocumentDAO().getDocumentsByCase(currentCase.getCaseId());
        DefaultTableModel m = (DefaultTableModel) documentTable.getModel();
        m.setRowCount(0);
        for (Document d : list) {
            m.addRow(new Object[]{d.getDocId(), d.getDocName(), d.getFilepath(), d.getUploadedAt()});
        }
    }

    private void loadHearings() {
        List<Hearing> list = new HearingDAO().getHearingsByCase(currentCase.getCaseId());
        DefaultTableModel m = (DefaultTableModel) hearingTable.getModel();
        m.setRowCount(0);
        for (Hearing h : list) {
            m.addRow(new Object[]{h.getHearingId(), h.getDate(), h.getRoom(), h.getDescription()});
        }
    }

    private void updateSelectedDocument() {
        int row = documentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Güncellemek için evrak seçin!");
            return;
        }

        int docId = (int) documentTable.getValueAt(row, 0);
        String oldName = (String) documentTable.getValueAt(row, 1);
        String oldPath = (String) documentTable.getValueAt(row, 2);

        JTextField nameField = new JTextField(oldName);
        JTextField pathField = new JTextField(oldPath);

        Object[] msg = {"Evrak Adı:", nameField, "Dosya Yolu:", pathField};
        int ok = JOptionPane.showConfirmDialog(this, msg, "Evrak Güncelle", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            String nn = nameField.getText().trim();
            String np = pathField.getText().trim();

            if (nn.isEmpty() || np.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alanlar boş olamaz!");
                return;
            }

            Document d = new Document();
            d.setDocId(docId);
            d.setDocName(nn);
            d.setFilepath(np);

            if (new DocumentDAO().updateDocument(d)) {
                JOptionPane.showMessageDialog(this, "Evrak güncellendi!");
                loadDocuments();
            }
        }
    }

    private void deleteSelectedDocument() {
        int row = documentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silmek için evrak seçin!");
            return;
        }
        int docId = (int) documentTable.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(this, "Seçili evrak silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (new DocumentDAO().deleteDocument(docId)) {
                JOptionPane.showMessageDialog(this, "Evrak silindi!");
                loadDocuments();
            }
        }
    }

    private void updateSelectedHearing() {
        int row = hearingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Güncellemek için duruşma seçin!");
            return;
        }

        int hearingId = (int) hearingTable.getValueAt(row, 0);
        Object oldDate = hearingTable.getValueAt(row, 1);
        String oldRoom = (String) hearingTable.getValueAt(row, 2);
        String oldDesc = (String) hearingTable.getValueAt(row, 3);

        JTextField dateField = new JTextField(String.valueOf(oldDate).replace(".0", ""));
        JTextField roomField = new JTextField(oldRoom);
        JTextField descField = new JTextField(oldDesc);

        Object[] msg = {"Tarih (YYYY-MM-DD HH:MM):", dateField, "Oda:", roomField, "Açıklama:", descField};
        int ok = JOptionPane.showConfirmDialog(this, msg, "Duruşma Güncelle", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            try {
                java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dateField.getText().trim() + ":00");
                Hearing h = new Hearing();
                h.setHearingId(hearingId);
                h.setDate(ts);
                h.setRoom(roomField.getText().trim());
                h.setDescription(descField.getText().trim());

                if (new HearingDAO().updateHearing(h)) {
                    JOptionPane.showMessageDialog(this, "Duruşma güncellendi!");
                    loadHearings();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tarih formatı hatalı!\nÖrnek: 2025-01-20 15:30");
            }
        }
    }

    private void deleteSelectedHearing() {
        int row = hearingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silmek için duruşma seçin!");
            return;
        }
        int hearingId = (int) hearingTable.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(this, "Seçili duruşma silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (new HearingDAO().deleteHearing(hearingId)) {
                JOptionPane.showMessageDialog(this, "Duruşma silindi!");
                loadHearings();
            }
        }
    }
}
