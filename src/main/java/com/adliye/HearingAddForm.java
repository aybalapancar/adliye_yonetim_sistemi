package com.adliye;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class HearingAddForm extends JFrame {

    private final int caseId;
    private final CaseDetailForm parentForm;

    public HearingAddForm(int caseId, CaseDetailForm parentForm) {
        this.caseId = caseId;
        this.parentForm = parentForm;

        setTitle("Duruşma Ekle");
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        panel.add(labelBold("Tarih (YYYY-MM-DD HH:MM):"));
        JTextField txtDate = new JTextField();
        txtDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtDate);

        panel.add(Box.createVerticalStrut(12));

        panel.add(labelBold("Oda:"));
        JTextField txtRoom = new JTextField();
        txtRoom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtRoom);

        panel.add(Box.createVerticalStrut(12));

        panel.add(labelBold("Açıklama:"));
        JTextField txtDesc = new JTextField();
        txtDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtDesc);

        panel.add(Box.createVerticalStrut(18));

        JButton saveBtn = new JButton("Kaydet");
        JButton backBtn = new JButton("Geri");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);
        panel.add(btnPanel);

        saveBtn.addActionListener(e -> {
            String dateStr = txtDate.getText().trim();
            String room = txtRoom.getText().trim();
            String desc = txtDesc.getText().trim();

            if (dateStr.isEmpty() || room.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tüm alanlar zorunlu!");
                return;
            }

            try {
                Timestamp ts = Timestamp.valueOf(dateStr + ":00");

                Hearing h = new Hearing();
                h.setCaseId(caseId);
                h.setDate(ts);
                h.setRoom(room);
                h.setDescription(desc);

                if (new HearingDAO().addHearing(h)) {
                    JOptionPane.showMessageDialog(this, "Duruşma eklendi!");
                    parentForm.refreshHearingTable();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Hata oluştu!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tarih formatı hatalı!\nÖrnek: 2025-01-20 15:30");
            }
        });

        backBtn.addActionListener(e -> dispose());
        setVisible(true);
    }

    private JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }
}
