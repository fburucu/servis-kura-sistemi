package com.service.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private DefaultListModel<String> plateListModel;
    private DefaultListModel<String> districtListModel;

    public MainFrame() {
        setTitle("Manisa Seyahat - Servis Kura Sistemi");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        plateListModel = new DefaultListModel<>();
        districtListModel = new DefaultListModel<>();

        JList<String> plateList = new JList<>(plateListModel);
        JList<String> districtList = new JList<>(districtListModel);

        JScrollPane plateScroll = new JScrollPane(plateList);
        JScrollPane districtScroll = new JScrollPane(districtList);

        plateScroll.setBorder(BorderFactory.createTitledBorder("Araç Plakaları"));
        districtScroll.setBorder(BorderFactory.createTitledBorder("Öğrenci Servis Güzergahları"));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                plateScroll,
                districtScroll
        );

        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
    }
}
