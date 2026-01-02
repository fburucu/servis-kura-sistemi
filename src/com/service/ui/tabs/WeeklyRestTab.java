package com.service.ui.tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class WeeklyRestTab extends JPanel {

    /* ================= MODEL ================= */
    static class Vehicle {
        String plate;
        boolean fromStudentService;
        boolean usedWeekendRest;
        LocalDate lastRestDate;

        Vehicle(String plate, boolean fromStudentService) {
            this.plate = plate;
            this.fromStudentService = fromStudentService;
        }

        @Override
        public String toString() {
            return plate;
        }
    }

    /* ================= FIELDS ================= */
    private DefaultListModel<Vehicle> vehicleListModel = new DefaultListModel<>();
    private DefaultTableModel tableModel;

    private JComboBox<Vehicle> yatarVehicleCombo;
    private JSpinner weekStartSpinner;
    private JSpinner yatarDateSpinner;
    private JLabel titleLabel;

    /* ================= CONSTRUCTOR ================= */
    public WeeklyRestTab() {
        setLayout(new BorderLayout());
        initUI();
    }

    /* ================= UI ================= */
    private void initUI() {

        JPanel leftPanel = createVehiclePanel();
        JPanel rightPanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                rightPanel
        );
        splitPane.setDividerLocation(340);

        add(splitPane, BorderLayout.CENTER);
    }

    /* ================= LEFT PANEL ================= */
    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField plateField = new JTextField();
        JCheckBox studentCheck = new JCheckBox("Öğrenci servisinden geldi");

        JButton addButton = new JButton("Ekle");
        JButton removeButton = new JButton("Sil");

        yatarVehicleCombo = new JComboBox<>();
        yatarVehicleCombo.setBorder(
                BorderFactory.createTitledBorder("Yatara Gidecek Araç")
        );

        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        inputPanel.add(new JLabel("Plaka:"));
        inputPanel.add(plateField);
        inputPanel.add(studentCheck);
        inputPanel.add(yatarVehicleCombo);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.add(addButton);
        buttons.add(removeButton);
        inputPanel.add(buttons);

        JList<Vehicle> vehicleList = new JList<>(vehicleListModel);
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Aktif Çalışan Araçlar")
        );

        addButton.addActionListener(e -> {
            String plate = plateField.getText().trim();
            if (plate.isEmpty()) {
                showWarning("Plaka boş olamaz!");
                return;
            }

            Vehicle v = new Vehicle(plate, studentCheck.isSelected());
            vehicleListModel.addElement(v);
            yatarVehicleCombo.addItem(v);

            plateField.setText("");
            studentCheck.setSelected(false);
        });

        removeButton.addActionListener(e -> {
            Vehicle selected = vehicleList.getSelectedValue();
            if (selected == null) {
                showWarning("Silmek için araç seçin!");
                return;
            }
            vehicleListModel.removeElement(selected);
            yatarVehicleCombo.removeItem(selected);
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /* ================= RIGHT PANEL ================= */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        titleLabel = new JLabel("Haftalık Dinlenme Programı", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Pazartesi", "Salı", "Çarşamba",
                "Perşembe", "Cuma", "Cumartesi", "Pazar"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(3, 2, 5, 5));

        // Sadece tarih
        weekStartSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor weekEditor = new JSpinner.DateEditor(weekStartSpinner, "dd/MM/yyyy");
        weekStartSpinner.setEditor(weekEditor);

        yatarDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor yatarEditor = new JSpinner.DateEditor(yatarDateSpinner, "dd/MM/yyyy");
        yatarDateSpinner.setEditor(yatarEditor);

        bottom.add(new JLabel("Hafta Başlangıç (Pzt):"));
        bottom.add(weekStartSpinner);

        bottom.add(new JLabel("Yatar Tarihi:"));
        bottom.add(yatarDateSpinner);

        JButton generateButton = new JButton("Plan Oluştur");
        bottom.add(new JLabel());
        bottom.add(generateButton);

        panel.add(bottom, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> generatePlan());

        return panel;
    }

    /* ================= PLAN LOGIC ================= */
    private void generatePlan() {

        Vehicle yatarVehicle = (Vehicle) yatarVehicleCombo.getSelectedItem();
        if (yatarVehicle == null) {
            showWarning("Yatara gidecek araç seçilmedi!");
            return;
        }

        LocalDate weekStart = toLocalDate((Date) weekStartSpinner.getValue());
        LocalDate weekEnd = weekStart.plusDays(6);
        LocalDate yatarDate = toLocalDate((Date) yatarDateSpinner.getValue());

        updateTitle(weekStart, weekEnd);

        Map<DayOfWeek, List<Vehicle>> plan = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek d : DayOfWeek.values()) {
            plan.put(d, new ArrayList<>());
        }

        Set<LocalDate> forbidden = Set.of(
                yatarDate,
                yatarDate.plusDays(1)
        );

        List<Vehicle> vehicles = Collections.list(vehicleListModel.elements());
        vehicles.remove(yatarVehicle);

        // Öğrenci servisinden gelenler: 1 kere hafta sonu
        for (Vehicle v : vehicles) {
            if (v.fromStudentService && !v.usedWeekendRest) {
                DayOfWeek d = Math.random() < 0.5 ? DayOfWeek.SATURDAY : DayOfWeek.SUNDAY;
                plan.get(d).add(v);
                v.usedWeekendRest = true;
            }
        }

        // Haftaiçi dağıtım
        for (Vehicle v : vehicles) {
            if (v.fromStudentService && v.usedWeekendRest) continue;

            for (int i = 0; i < 5; i++) {
                LocalDate date = weekStart.plusDays(i);
                if (forbidden.contains(date)) continue;

                DayOfWeek day = date.getDayOfWeek();
                int limit = (day == DayOfWeek.FRIDAY) ? 4 : 6;

                if (plan.get(day).size() < limit) {
                    plan.get(day).add(v);
                    v.lastRestDate = date;
                    break;
                }
            }
        }

        fillTable(plan);
    }

    /* ================= TABLE ================= */
    private void fillTable(Map<DayOfWeek, List<Vehicle>> plan) {
        tableModel.setRowCount(0);

        int max = plan.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        for (int i = 0; i < max; i++) {
            Object[] row = new Object[7];
            for (int d = 0; d < 7; d++) {
                DayOfWeek day = DayOfWeek.of(d + 1);
                List<Vehicle> list = plan.get(day);
                row[d] = (i < list.size()) ? list.get(i).plate : "";
            }
            tableModel.addRow(row);
        }
    }

    /* ================= HELPERS ================= */
    private LocalDate toLocalDate(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void updateTitle(LocalDate start, LocalDate end) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        titleLabel.setText(
                fmt.format(start) + " - " + fmt.format(end) +
                        " Haftası Dinlenme Programı"
        );
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Uyarı", JOptionPane.WARNING_MESSAGE);
    }
}
