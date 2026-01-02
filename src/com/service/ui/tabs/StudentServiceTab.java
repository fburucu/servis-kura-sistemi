package com.service.ui.tabs;

import javax.swing.*;
import java.awt.*;

public class StudentServiceTab extends JPanel {

    public StudentServiceTab() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel(
                "Öğrenci Servisi Kura Sistemi",
                SwingConstants.CENTER
        );
        label.setFont(new Font("Arial", Font.BOLD, 18));

        add(label, BorderLayout.CENTER);
    }
}
