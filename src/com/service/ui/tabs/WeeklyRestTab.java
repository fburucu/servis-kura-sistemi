package com.service.ui.tabs;

import javax.swing.*;
import java.awt.*;

public class WeeklyRestTab extends JPanel {

    public WeeklyRestTab() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel(
                "Haftalık Dinlenme Planı",
                SwingConstants.CENTER
        );
        label.setFont(new Font("Arial", Font.BOLD, 18));

        add(label, BorderLayout.CENTER);
    }
}
