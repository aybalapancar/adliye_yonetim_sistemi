package com.adliye;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CaseAddForm extends JFrame {

    private JTextField txtTitle;
    private JTextArea txtDesc;
    private JTextField txtDavaci;
    private JTextField txtDavali;
    private JComboBox<Court> comboCourt;

    // === SADECE PERSONEL İÇİN ===
    private JComboBox<User> comboDavaciLawyer;
    private JComboBox<User> comboDavaliLawyer;

    private final User currentUser;

    public CaseAddForm(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Yeni Dava Aç");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        // ================= MAHKEME =================
        panel.add(labelBold("Mahkeme:"));
        comboCourt = new JComboBox<>();
        for (Court ct : new CourtDAO().getAllCourts()) {
            comboCourt.addItem(ct);
        }
        comboCourt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(comboCourt);

        panel.add(Box.createVerticalStrut(12));

        // ================= BAŞLIK =================
        panel.add(labelBold("Dava Başlığı:"));
        txtTitle = new JTextField();
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtTitle);

        panel.add(Box.createVerticalStrut(12));

        // ================= AÇIKLAMA =================
        panel.add(labelBold("Açıklama:"));
        txtDesc = new JTextArea(4, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtDesc));

        panel.add(Box.createVerticalStrut(12));

        // ================= DAVACI =================
        panel.add(labelBold("Davacı (Ad Soyad):"));
        txtDavaci = new JTextField();
        txtDavaci.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtDavaci);

        // ================= DAVALI =================
        panel.add(Box.createVerticalStrut(12));
        panel.add(labelBold("Davalı (Ad Soyad):"));
        txtDavali = new JTextField();
        txtDavali.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtDavali);

        // ================= AVUKAT SEÇİMİ (SADECE PERSONEL) =================
        if (currentUser.getRole().equals("personel")) {

            UserDAO userDAO = new UserDAO();
            List<User> lawyers = userDAO.getAllLawyers();

            panel.add(Box.createVerticalStrut(15));
            panel.add(labelBold("Davacı Avukatı:"));
            comboDavaciLawyer = new JComboBox<>();
            for (User u : lawyers) comboDavaciLawyer.addItem(u);
            comboDavaciLawyer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            panel.add(comboDavaciLawyer);

            panel.add(Box.createVerticalStrut(12));
            panel.add(labelBold("Davalı Avukatı:"));
            comboDavaliLawyer = new JComboBox<>();
            for (User u : lawyers) comboDavaliLawyer.addItem(u);
            comboDavaliLawyer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            panel.add(comboDavaliLawyer);
        }

        // ================= BUTONLAR =================
        panel.add(Box.createVerticalStrut(18));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveBtn = new JButton("Kaydet");
        JButton backBtn = new JButton("Geri");
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);
        panel.add(btnPanel);

        saveBtn.addActionListener(e -> saveCase());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }

    // ================= KAYDET =================
    private void saveCase() {

        String title = txtTitle.getText().trim();
        String desc = txtDesc.getText().trim();
        String davaciName = txtDavaci.getText().trim();
        String davaliName = txtDavali.getText().trim();
        Court selectedCourt = (Court) comboCourt.getSelectedItem();

        if (title.isEmpty() || desc.isEmpty() || davaciName.isEmpty() || davaliName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanları doldurun!");
            return;
        }
        if (selectedCourt == null) {
            JOptionPane.showMessageDialog(this, "Mahkeme seçiniz!");
            return;
        }
        if (davaciName.equalsIgnoreCase(davaliName)) {
            JOptionPane.showMessageDialog(this, "Davacı ve davalı aynı olamaz!");
            return;
        }

        Case c = new Case();
        c.setTitle(title);
        c.setDescription(desc);
        c.setStatus("açık");
        c.setCourtId(selectedCourt.getCourtId());
        c.setCreatedBy(currentUser.getUserId());

        CaseDAO dao = new CaseDAO();
        int newCaseId = dao.addCaseReturnId(c);

        if (newCaseId > 0) {
            CasePartyDAO partyDAO = new CasePartyDAO();

            Integer davaciLawyerId = null;
            Integer davaliLawyerId = null;

            if (currentUser.getRole().equals("personel")) {
                User dl = (User) comboDavaciLawyer.getSelectedItem();
                User vl = (User) comboDavaliLawyer.getSelectedItem();
                if (dl != null) davaciLawyerId = dl.getUserId();
                if (vl != null) davaliLawyerId = vl.getUserId();
            }

            partyDAO.addParty(newCaseId, davaciName, "davaci", davaciLawyerId);
            partyDAO.addParty(newCaseId, davaliName, "davali", davaliLawyerId);

            JOptionPane.showMessageDialog(this, "Dava başarıyla açıldı!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hata oluştu!");
        }
    }
}
