package com.adliye;

import javax.swing.*;
import java.awt.*;

public class CourtAddForm extends JFrame {

    private final CourtListForm parent;
    private JTextField cityField;
    private JTextField typeField;

    public CourtAddForm(CourtListForm parent) {
        this.parent = parent;

        setTitle("Yeni Mahkeme Ekle");
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);

        JLabel title = new JLabel("Yeni Mahkeme Kaydı");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(15));

        panel.add(labelBold("Şehir:"));
        cityField = new JTextField();
        cityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(cityField);

        panel.add(Box.createVerticalStrut(12));

        panel.add(labelBold("Mahkeme Türü:"));
        typeField = new JTextField();
        typeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(typeField);

        panel.add(Box.createVerticalStrut(18));

        JButton saveBtn = new JButton("Kaydet");
        JButton cancelBtn = new JButton("İptal");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        panel.add(btnPanel);

        saveBtn.addActionListener(e -> saveCourt());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }

    private void saveCourt() {
        String city = cityField.getText().trim();
        String type = typeField.getText().trim();

        if (city.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanlar zorunlu!");
            return;
        }

        Court c = new Court();
        c.setCity(city);
        c.setCourtType(type);

        if (new CourtDAO().addCourt(c)) {
            JOptionPane.showMessageDialog(this, "Mahkeme eklendi!");
            if (parent != null) parent.loadCourts();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hata oluştu!");
        }
    }
}
