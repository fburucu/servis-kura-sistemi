package com.service.ui;

import com.service.ui.tabs.StudentServiceTab;
import com.service.ui.tabs.WeeklyRestTab;


import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Manisa Seyahat - Operasyon Yönetim Sistemi");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Öğrenci Servisi Kurası", new StudentServiceTab());
        tabbedPane.addTab("Haftalık Dinlenme Planı", new WeeklyRestTab());

        add(tabbedPane);
    }
}
