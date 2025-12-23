package com.adliye;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    public LoginForm() {
        setTitle("Adliye Yönetim Sistemi - Giriş");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        add(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 70, 140));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("Adliye Yönetim Sistemi");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(lblTitle, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        root.add(form, BorderLayout.CENTER);

        JLabel lblLogin = new JLabel("Kullanıcı Girişi");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 16));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(lblLogin);

        form.add(Box.createVerticalStrut(15));

        JLabel lblEmail = new JLabel("E-posta:");
        JTextField txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        form.add(lblEmail);
        form.add(txtEmail);

        form.add(Box.createVerticalStrut(10));

        JLabel lblPass = new JLabel("Şifre:");
        JPasswordField txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        form.add(lblPass);
        form.add(txtPass);

        form.add(Box.createVerticalStrut(20));

        JButton btnLogin = new JButton("Giriş Yap");
        JButton btnClose = new JButton("Programı Kapat");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnLogin);
        btnPanel.add(btnClose);
        form.add(btnPanel);

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            User user = new UserDAO().login(email, pass);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı!");
                dispose();
                new MainMenu(user);
            } else {
                JOptionPane.showMessageDialog(this, "E-posta veya şifre hatalı!");
            }
        });

        btnClose.addActionListener(e -> System.exit(0));
        setVisible(true);
    }
}
