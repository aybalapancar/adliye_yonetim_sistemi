package com.adliye;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private final User currentUser;

    public MainMenu(User user) {
        this.currentUser = user;

        setTitle("Ana Menü - " + user.getName() + " (" + user.getRole() + ")");
        setSize(520, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        add(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 70, 140));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("Adliye Yönetim Sistemi - Ana Menü");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(lblTitle, BorderLayout.WEST);

        JLabel lblUser = new JLabel(user.getName() + " - " + user.getRole());
        lblUser.setForeground(Color.WHITE);
        header.add(lblUser, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        root.add(center, BorderLayout.CENTER);

        JButton btnCaseList = new JButton("Dava Listesi");
        btnCaseList.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCaseList.addActionListener(e -> new CaseListForm(currentUser));
        center.add(Box.createVerticalStrut(25));
        center.add(btnCaseList);

        if (user.getRole().equals("savcı") || user.getRole().equals("personel")) {
            JButton btnAddCase = new JButton("Yeni Dava Aç");
            btnAddCase.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnAddCase.addActionListener(e -> new CaseAddForm(currentUser));
            center.add(Box.createVerticalStrut(15));
            center.add(btnAddCase);
        }

        if (user.getRole().equals("personel")) {
            JButton btnCourts = new JButton("Mahkeme Listesi");
            btnCourts.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnCourts.addActionListener(e -> new CourtListForm());
            center.add(Box.createVerticalStrut(15));
            center.add(btnCourts);
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Çıkış Yap");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        bottom.add(btnLogout);
        root.add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
