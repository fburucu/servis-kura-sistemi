package com.service.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private DefaultListModel<String> plateListModel;
    private DefaultListModel<String> districtListModel;

    public MainFrame() {
        setTitle("Manisa Seyahat - Servis Kura Sistemi");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        plateListModel = new DefaultListModel<>();
        districtListModel = new DefaultListModel<>();

        // Sol Panel (Plakalar)
        JPanel platePanel = new JPanel(new BorderLayout());

        JTextField plateField = new JTextField();
        JButton addPlateButton = new JButton("Ekle");
        JButton removePlateButton = new JButton("Sil");

        JPanel plateInputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        plateInputPanel.add(plateField);

        JPanel plateButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        plateButtonPanel.add(addPlateButton);
        plateButtonPanel.add(removePlateButton);

        plateInputPanel.add(plateButtonPanel);

        JList<String> plateList = new JList<>(plateListModel);
        JScrollPane plateScroll = new JScrollPane(plateList);
        plateScroll.setBorder(BorderFactory.createTitledBorder("Araç Plakaları"));

        platePanel.add(plateInputPanel, BorderLayout.NORTH);
        platePanel.add(plateScroll, BorderLayout.CENTER);

        // Sag Panel (Ilceler)
        JPanel districtPanel = new JPanel(new BorderLayout());

        JTextField districtField = new JTextField();
        JButton addDistrictButton = new JButton("Ekle");
        JButton removeDistrictButton = new JButton("Sil");

        JPanel districtInputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        districtInputPanel.add(districtField);

        JPanel districtButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        districtButtonPanel.add(addDistrictButton);
        districtButtonPanel.add(removeDistrictButton);

        districtInputPanel.add(districtButtonPanel);

        JList<String> districtList = new JList<>(districtListModel);
        JScrollPane districtScroll = new JScrollPane(districtList);
        districtScroll.setBorder(
                BorderFactory.createTitledBorder("Öğrenci Servis Güzergahları")
        );

        districtPanel.add(districtInputPanel, BorderLayout.NORTH);
        districtPanel.add(districtScroll, BorderLayout.CENTER);

        // Orta Ayirici
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                platePanel,
                districtPanel
        );
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);

        // Plaka Buton
        addPlateButton.addActionListener(e -> {
            String plate = plateField.getText().trim();
            if (!plate.isEmpty()) {
                plateListModel.addElement(plate);
                plateField.setText("");
            } else {
                showWarning("Plaka boş olamaz!");
            }
        });

        removePlateButton.addActionListener(e -> {
            int index = plateList.getSelectedIndex();
            if (index != -1) {
                plateListModel.remove(index);
            } else {
                showWarning("Silmek için plaka seçin!");
            }
        });

        // Ilce Buton
        addDistrictButton.addActionListener(e -> {
            String district = districtField.getText().trim();
            if (!district.isEmpty()) {
                districtListModel.addElement(district);
                districtField.setText("");
            } else {
                showWarning("İlçe adı boş olamaz!");
            }
        });

        removeDistrictButton.addActionListener(e -> {
            int index = districtList.getSelectedIndex();
            if (index != -1) {
                districtListModel.remove(index);
            } else {
                showWarning("Silmek için ilçe seçin!");
            }
        });
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Uyarı",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
