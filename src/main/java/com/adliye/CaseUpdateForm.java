package com.adliye;

import javax.swing.*;
import java.awt.*;

public class CaseUpdateForm extends JFrame {

    private final Case editingCase;
    private final CaseListForm parentForm;

    private JTextField txtTitle;
    private JTextArea txtDesc;
    private JComboBox<String> comboStatus;

    public CaseUpdateForm(Case editingCase, CaseListForm parentForm) {
        this.editingCase = editingCase;
        this.parentForm = parentForm;

        setTitle("Dava Güncelle - ID: " + editingCase.getCaseId());
        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        add(panel);

        panel.add(labelBold("Dava Başlığı:"));
        txtTitle = new JTextField(editingCase.getTitle());
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtTitle);

        panel.add(Box.createVerticalStrut(12));

        panel.add(labelBold("Açıklama:"));
        txtDesc = new JTextArea(editingCase.getDescription());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(txtDesc);
        sp.setPreferredSize(new Dimension(440, 160));
        panel.add(sp);

        panel.add(Box.createVerticalStrut(12));

        panel.add(labelBold("Durum:"));
        comboStatus = new JComboBox<>();
        comboStatus.addItem("açık");
        comboStatus.addItem("beklemede");
        comboStatus.addItem("kapalı");
        comboStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboStatus.setSelectedItem(editingCase.getStatus() == null ? "açık" : editingCase.getStatus());
        panel.add(comboStatus);

        panel.add(Box.createVerticalStrut(18));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Kaydet");
        JButton btnCancel = new JButton("İptal");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        panel.add(btnPanel);

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }

    private void onSave() {
        String title = txtTitle.getText().trim();
        String desc = txtDesc.getText().trim();
        String status = (String) comboStatus.getSelectedItem();

        if (title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Başlık ve açıklama boş olamaz!");
            return;
        }
        if (status == null || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Durum seçiniz!");
            return;
        }

        editingCase.setTitle(title);
        editingCase.setDescription(desc);
        editingCase.setStatus(status);

        CaseDAO dao = new CaseDAO();
        boolean ok = dao.updateCase(editingCase);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Dava güncellendi!");
            if (parentForm != null) {
                parentForm.loadInitial();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Güncelleme başarısız!");
        }
    }
}
