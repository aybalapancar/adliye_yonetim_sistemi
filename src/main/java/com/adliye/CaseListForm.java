package com.adliye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CaseListForm extends JFrame {

    private final User currentUser;
    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> comboStatus;

    public CaseListForm(User user) {
        this.currentUser = user;

        setTitle("Dava Listesi");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        add(root);

        // ÜST PANEL
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Dava Listesi - Kullanıcı: " + user.getName() + " (" + user.getRole() + ")");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topPanel.add(lblTitle);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboStatus = new JComboBox<>();
        comboStatus.addItem("Tümü");
        comboStatus.addItem("Açık");
        comboStatus.addItem("Kapalı");
        comboStatus.addItem("Beklemede");

        JButton btnFilter = new JButton("Filtrele");
        btnFilter.addActionListener(e -> {

            String status = comboStatus.getSelectedItem().toString();
            CaseDAO dao = new CaseDAO();
            List<Case> cases;

            if (currentUser.getRole().equals("avukat")) {

                if (status.equals("Tümü")) {
                    cases = dao.getCasesByLawyer(currentUser.getUserId());
                } else {
                    cases = dao.getCasesByLawyerAndStatus(
                            currentUser.getUserId(), status
                    );
                }

            } else {

                if (status.equals("Tümü")) {
                    cases = dao.getAllCases();
                } else {
                    cases = dao.getCasesByStatusFunction(status);
                }
            }

            loadTable(cases);
        });


        filterPanel.add(new JLabel("Durum:"));
        filterPanel.add(comboStatus);
        filterPanel.add(btnFilter);

        topPanel.add(filterPanel);
        root.add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID","Başlık","Durum","MahkemeID","OluşturanID","Tarih"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        // === ALT PANEL ===
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        String role = currentUser.getRole();

// SADECE PERSONEL + SAVCI
        if (role.equals("personel") || role.equals("savcı")) {

            JButton btnUpdateCase = new JButton("Dava Güncelle");

            btnUpdateCase.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Güncellemek için bir dava seçin!");
                    return;
                }

                int caseId = (int) model.getValueAt(row, 0);

                CaseDAO dao = new CaseDAO();
                Case c = dao.getCaseById(caseId);

                if (c != null) {
                    new CaseUpdateForm(c, this);
                }
            });

            bottom.add(btnUpdateCase);
        }

        // HERKES GÖRÜR
        JButton btnBack = new JButton("Ana Menüye Dön");
        btnBack.addActionListener(e -> dispose());
        bottom.add(btnBack);

        root.add(bottom, BorderLayout.SOUTH);


        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int id = (int) model.getValueAt(table.getSelectedRow(), 0);
                Case c = new CaseDAO().getCaseById(id);
                if (c != null) new CaseDetailForm(c, currentUser);
            }
        });

        loadInitial();
        setVisible(true);
    }

    public void loadInitial() {

        CaseDAO dao = new CaseDAO();
        List<Case> cases;

        if (currentUser.getRole().equals("avukat")) {
            cases = dao.getCasesByLawyer(currentUser.getUserId());
        } else {
            cases = dao.getAllCases();
        }

        loadTable(cases);
    }



    private void loadTable(List<Case> cases) {
        model.setRowCount(0);
        for (Case c : cases) {
            model.addRow(new Object[]{
                    c.getCaseId(),
                    c.getTitle(),
                    c.getStatus(),
                    c.getCourtId(),
                    c.getCreatedBy(),
                    c.getCreatedAt()
            });
        }
    }
}
