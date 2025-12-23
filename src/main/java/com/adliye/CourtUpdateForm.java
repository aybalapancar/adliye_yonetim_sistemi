package com.adliye;

import javax.swing.*;
import java.awt.*;

public class CourtUpdateForm extends JFrame {

    private JTextField cityField;
    private JTextField typeField;

    private Court court;
    private CourtListForm parentForm;

    public CourtUpdateForm(Court court, CourtListForm parentForm) {
        this.court = court;
        this.parentForm = parentForm;

        setTitle("Mahkeme Güncelle");
        setSize(400, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);

        JLabel title = new JLabel("Mahkeme Güncelle");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(20));

        // Şehir
        panel.add(new JLabel("Şehir:"));
        cityField = new JTextField(court.getCity());
        cityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(cityField);

        panel.add(Box.createVerticalStrut(10));

        // Tür
        panel.add(new JLabel("Mahkeme Türü:"));
        typeField = new JTextField(court.getCourtType());
        typeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(typeField);

        panel.add(Box.createVerticalStrut(20));

        JButton btnUpdate = new JButton("Güncelle");
        btnUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnUpdate);

        // === OLAY ===
        btnUpdate.addActionListener(e -> updateCourt());

        setVisible(true);
    }

    private void updateCourt() {
        String city = cityField.getText().trim();
        String type = typeField.getText().trim();

        if (city.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanları doldurun!");
            return;
        }

        court.setCity(city);
        court.setCourtType(type);

        CourtDAO dao = new CourtDAO();
        if (dao.updateCourt(court)) {
            JOptionPane.showMessageDialog(this, "Mahkeme başarıyla güncellendi!");
            parentForm.loadCourts();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hata! Güncelleme başarısız.");
        }
    }
}
