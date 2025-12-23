package com.adliye;

import javax.swing.*;
import java.awt.*;

public class DocumentAddForm extends JFrame {

    private final int caseId;
    private final CaseDetailForm parentForm;

    public DocumentAddForm(int caseId, CaseDetailForm parentForm) {
        this.caseId = caseId;
        this.parentForm = parentForm;

        setTitle("Evrak Ekle");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        panel.add(labelBold("Evrak Adı:"));
        JTextField txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtName);

        panel.add(Box.createVerticalStrut(15));

        panel.add(labelBold("Dosya Yolu:"));
        JTextField txtFile = new JTextField();
        txtFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(txtFile);

        panel.add(Box.createVerticalStrut(20));

        JButton saveBtn = new JButton("Kaydet");
        JButton backBtn = new JButton("Geri");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);
        panel.add(btnPanel);

        saveBtn.addActionListener(e -> {
            String name = txtName.getText().trim();
            String file = txtFile.getText().trim();

            if (name.isEmpty() || file.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tüm alanları doldurun!");
                return;
            }

            Document d = new Document();
            d.setCaseId(caseId);
            d.setDocName(name);
            d.setFilepath(file);

            if (new DocumentDAO().addDocument(d)) {
                JOptionPane.showMessageDialog(this, "Evrak eklendi!");
                parentForm.refreshDocumentTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Hata oluştu!");
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
