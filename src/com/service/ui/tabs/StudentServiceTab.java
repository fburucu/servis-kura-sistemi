package com.service.ui.tabs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentServiceTab extends JPanel {

    private DefaultListModel<String> plateListModel;
    private DefaultListModel<String> districtListModel;

    public StudentServiceTab() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        plateListModel = new DefaultListModel<>();
        districtListModel = new DefaultListModel<>();

        JPanel platePanel = createPlatePanel();
        JPanel districtPanel = createDistrictPanel();

        JButton drawButton = new JButton("🎲 Kura Çek");
        drawButton.setFont(new Font("Arial", Font.BOLD, 16));
        drawButton.addActionListener(e -> drawLots());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(drawButton);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                platePanel,
                districtPanel
        );
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ===== KURA ÇEKİMİ =====
    private void drawLots() {
        if (plateListModel.isEmpty() || districtListModel.isEmpty()) {
            showWarning("Plaka ve ilçe listeleri boş olamaz!");
            return;
        }

        if (plateListModel.size() != districtListModel.size()) {
            showWarning("Plaka sayısı ile ilçe sayısı eşit olmalıdır!");
            return;
        }

        List<String> districts = new ArrayList<>();
        for (int i = 0; i < districtListModel.size(); i++) {
            districts.add(districtListModel.get(i));
        }

        Collections.shuffle(districts);

        StringBuilder result = new StringBuilder("🎯 KURA SONUCU:\n\n");
        for (int i = 0; i < plateListModel.size(); i++) {
            result.append(plateListModel.get(i))
                    .append(" → ")
                    .append(districts.get(i))
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(result.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Kura Sonucu",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===== SOL PANEL =====
    private JPanel createPlatePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField field = new JTextField();
        JButton add = new JButton("Ekle");
        JButton remove = new JButton("Sil");

        JPanel input = new JPanel(new GridLayout(2, 1, 5, 5));
        input.add(field);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.add(add);
        buttons.add(remove);
        input.add(buttons);

        JList<String> list = new JList<>(plateListModel);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createTitledBorder("Araç Plakaları"));

        panel.add(input, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        add.addActionListener(e -> {
            String text = field.getText().trim();
            if (!text.isEmpty()) {
                plateListModel.addElement(text);
                field.setText("");
            } else {
                showWarning("Plaka boş olamaz!");
            }
        });

        remove.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index != -1) {
                plateListModel.remove(index);
            } else {
                showWarning("Silmek için plaka seçin!");
            }
        });

        return panel;
    }

    // ===== SAĞ PANEL =====
    private JPanel createDistrictPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField field = new JTextField();
        JButton add = new JButton("Ekle");
        JButton remove = new JButton("Sil");

        JPanel input = new JPanel(new GridLayout(2, 1, 5, 5));
        input.add(field);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.add(add);
        buttons.add(remove);
        input.add(buttons);

        JList<String> list = new JList<>(districtListModel);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(
                BorderFactory.createTitledBorder("Öğrenci Servis Güzergahları")
        );

        panel.add(input, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        add.addActionListener(e -> {
            String text = field.getText().trim();
            if (!text.isEmpty()) {
                districtListModel.addElement(text);
                field.setText("");
            } else {
                showWarning("İlçe adı boş olamaz!");
            }
        });

        remove.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index != -1) {
                districtListModel.remove(index);
            } else {
                showWarning("Silmek için ilçe seçin!");
            }
        });

        return panel;
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
