package com.adliye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourtListForm extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public CourtListForm() {
        setTitle("Mahkeme Listesi");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"ID", "Şehir", "Tür"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Yeni Mahkeme Ekle");
        JButton delBtn = new JButton("Sil");
        JButton backBtn = new JButton("Ana Menüye Dön");

        addBtn.addActionListener(e -> new CourtAddForm(this));
        delBtn.addActionListener(e -> deleteSelected());
        backBtn.addActionListener(e -> dispose());

        bottom.add(addBtn);
        bottom.add(delBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);

        loadCourts();
        setVisible(true);
    }

    public void loadCourts() {
        model.setRowCount(0);
        for (Court c : new CourtDAO().getAllCourts()) {
            model.addRow(new Object[]{c.getCourtId(), c.getCity(), c.getCourtType()});
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silmek için bir satır seçin!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this, "Seçili mahkeme silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (new CourtDAO().deleteCourt(id)) {
                JOptionPane.showMessageDialog(this, "Mahkeme silindi!");
                loadCourts();
            }
        }
    }
}
