package com.service.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private DefaultListModel<String> plateListModel;
    private DefaultListModel<String> districtListModel;

    public MainFrame() {
        setTitle("Manisa Seyahat - Servis Kura Sistemi");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        plateListModel = new DefaultListModel<>();
        districtListModel = new DefaultListModel<>();

        // SOL PANEL (Plakalar)
        JPanel platePanel= new JPanel(new BorderLayout());

        JTextField plateField = new JTextField();
        JButton addPlateButton = new JButton("Plaka Ekle");
        JButton removePlateButton = new JButton("Plaka Sil");

        JPanel plateInputPanel= new JPanel(new GridLayout(2,1,5,5));
        plateInputPanel.add(plateField);

        JPanel plateButtonPanel = new JPanel(new GridLayout(1,2,5,5));
        plateButtonPanel.add(addPlateButton);
        plateButtonPanel.add(removePlateButton);

        plateInputPanel.add(plateButtonPanel);

        JList<String> plateList=new JList<>(plateListModel);
        JScrollPane plateScroll = new JScrollPane(plateList);
        plateScroll.setBorder(BorderFactory.createTitledBorder("Araç Plakaları"));

        platePanel.add(plateInputPanel,BorderLayout.NORTH);
        platePanel.add(plateScroll,BorderLayout.CENTER);

        //Sag Panel
        JList<String> districtList= new JList<>(districtListModel);
        JScrollPane districtScroll= new JScrollPane(districtList);
        districtScroll.setBorder(
                BorderFactory.createTitledBorder("Öğrenci Servis Güzergahları")
        );

        JSplitPane splitPane= new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                platePanel,
                districtScroll
        );
        splitPane.setDividerLocation(450);

        add(splitPane,BorderLayout.CENTER);

        // Button Actions
        addPlateButton.addActionListener(e -> {
            String plate= plateField.getText().trim();
            if(!plate.isEmpty()){
                plateListModel.addElement(plate);
                plateField.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Plaka Boş olamaz!",
                        "Uyarı",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        removePlateButton.addActionListener(e -> {
            int selectedIndex=plateList.getSelectedIndex();
            if(selectedIndex!=-1){
                plateListModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Silmek için bir plaka seçin!",
                        "Uyarı",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }
}
