package com.service.ui.tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class WeeklyRestTab extends JPanel {

        private DefaultListModel<String> vehicleListModel;
        private DefaultTableModel tableModel;

        public WeeklyRestTab(){
            setLayout(new BorderLayout());
            initUI();
        }

        private void initUI(){
            vehicleListModel=new DefaultListModel<>();

            JPanel leftPanel=createVehiclePanel();
            JPanel rightpanel= createTablePanel();

            JSplitPane splitPane=  new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    leftPanel,
                    rightpanel
            );
            splitPane.setDividerLocation(300);

            add(splitPane,BorderLayout.CENTER);
        }

        // SOL PANEL (ARAC LISTESI)

        private  JPanel createVehiclePanel(){
        JPanel panel=new JPanel(new BorderLayout());

        JTextField plateField= new JTextField();
        JButton addButton = new JButton("Ekle");
        JButton removeButton = new JButton("Sil");

        JPanel inputPanel= new JPanel(new GridLayout(2,1,5,5));
        inputPanel.add(plateField);

        JPanel buttons = new JPanel(new GridLayout(1,2,5,5));
        buttons.add(addButton);
        buttons.add(removeButton);

        inputPanel.add(buttons);

        JList<String> vehicleList=new JList<>(vehicleListModel);
        JScrollPane scrollPane= new JScrollPane(vehicleList);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Aktif Çalışan Araçlar")
        );

        panel.add(inputPanel,BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String plate=plateField.getText().trim();
            if(!plate.isEmpty()){
                vehicleListModel.addElement(plate);
                plateField.setText("");
            } else {
                showWarning("Plaka boş olamaz!");
            }
        });

        removeButton.addActionListener(e -> {
            int index=vehicleList.getSelectedIndex();
            if(index != -1){
                vehicleListModel.remove(index);
            } else {
                showWarning("Silmek için araç seçin!");
            }
        });

        return panel;
        }

        // SAG PANEL (HAFTALIK TABLO)
        private JPanel createTablePanel(){
            JPanel panel = new JPanel(new BorderLayout());

            String[] columns = {
                    "Pazartesi",
                    "Salı",
                    "Çarşamba",
                    "Perşembe",
                    "Cuma",
                    "Cumartesi",
                    "Pazar"
            };

            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(tableModel);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(
                    BorderFactory.createTitledBorder("Haftalık Dinlenme Planı")
            );

            panel.add(scrollPane, BorderLayout.CENTER);

            JButton generateButton = new JButton("Plan Oluştur");
            generateButton.setEnabled(false);
            panel.add(generateButton, BorderLayout.SOUTH);

            return panel;
        }

        private void showWarning(String message){
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
            );
        }

}


