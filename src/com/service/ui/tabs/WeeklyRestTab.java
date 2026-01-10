package com.service.ui.tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WeeklyRestTab extends JPanel {

    // Referans liste (İlk açılış için)
    private static final Map<String, DayOfWeek> BASE_REST_MAP = new HashMap<>() {{
        put("557", DayOfWeek.MONDAY); put("244", DayOfWeek.MONDAY); put("339", DayOfWeek.MONDAY);
        put("025", DayOfWeek.MONDAY); put("207", DayOfWeek.MONDAY); put("050", DayOfWeek.MONDAY);
        put("777", DayOfWeek.TUESDAY); put("390", DayOfWeek.TUESDAY); put("517", DayOfWeek.TUESDAY);
        put("710", DayOfWeek.TUESDAY); put("575", DayOfWeek.TUESDAY); put("479", DayOfWeek.TUESDAY);
        put("033", DayOfWeek.TUESDAY);
        put("125", DayOfWeek.WEDNESDAY); put("EV707", DayOfWeek.WEDNESDAY); put("765", DayOfWeek.WEDNESDAY);
        put("654", DayOfWeek.WEDNESDAY);  put("945", DayOfWeek.WEDNESDAY);  put("532", DayOfWeek.WEDNESDAY);
        put("116", DayOfWeek.WEDNESDAY);
        put("344", DayOfWeek.THURSDAY); put("1234", DayOfWeek.THURSDAY); put("101", DayOfWeek.THURSDAY);
        put("857", DayOfWeek.THURSDAY); put("228", DayOfWeek.THURSDAY); put("UV707", DayOfWeek.THURSDAY);
        put("234", DayOfWeek.THURSDAY);
        put("051", DayOfWeek.FRIDAY); put("021", DayOfWeek.FRIDAY); put("DM510", DayOfWeek.FRIDAY);
        put("203", DayOfWeek.SATURDAY);
    }};

    static class Vehicle {
        String plate;
        boolean isNewcomer;
        DayOfWeek lastRestDay;

        Vehicle(String plate, boolean isNewcomer) {
            this.plate = plate;
            this.isNewcomer = isNewcomer;
            this.lastRestDay = BASE_REST_MAP.getOrDefault(plate, DayOfWeek.FRIDAY);
        }
        @Override
        public String toString() { return plate; }
    }

    private DefaultListModel<Vehicle> vehicleListModel = new DefaultListModel<>();
    private DefaultTableModel tableModel;
    private JComboBox<Vehicle> yatarVehicleCombo;
    private JComboBox<DayOfWeek> yatarDayCombo;

    public WeeklyRestTab() {
        setLayout(new BorderLayout());
        initUI();
        loadInitialData();
    }

    private void initUI() {
        // ... (UI Tasarımı öncekiyle aynı kalıyor, sadece generatePlan mantığı değişti)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Araç Yönetimi"));

        JTextField plateField = new JTextField();
        JCheckBox newcomerCheck = new JCheckBox("Yeni Gelen (Okul/Fabrika)");
        JButton addButton = new JButton("Araç Ekle");
        JButton removeButton = new JButton("Seçili Aracı Sil");

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inputPanel.add(new JLabel("Plaka:"));
        inputPanel.add(plateField);
        inputPanel.add(newcomerCheck);
        inputPanel.add(addButton);

        JList<Vehicle> vehicleList = new JList<>(vehicleListModel);
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(vehicleList), BorderLayout.CENTER);
        leftPanel.add(removeButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] columns = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);

        JPanel controlPanel = new JPanel(new FlowLayout());
        yatarVehicleCombo = new JComboBox<>();
        yatarDayCombo = new JComboBox<>(new DayOfWeek[]{
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY ,DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
        });
        JButton generateBtn = new JButton("Programı Hazırla");

        controlPanel.add(new JLabel("Yatar Aracı:"));
        controlPanel.add(yatarVehicleCombo);
        controlPanel.add(new JLabel("Yatar Günü:"));
        controlPanel.add(yatarDayCombo);
        controlPanel.add(generateBtn);

        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String p = plateField.getText().toUpperCase().trim();
            if(!p.isEmpty()){
                Vehicle v = new Vehicle(p, newcomerCheck.isSelected());
                vehicleListModel.addElement(v);
                yatarVehicleCombo.addItem(v);
                plateField.setText("");
            }
        });

        generateBtn.addActionListener(e -> generatePlan());
    }

    private void generatePlan() {
        if (vehicleListModel.isEmpty()) return;

        Vehicle yatarV = (Vehicle) yatarVehicleCombo.getSelectedItem();
        DayOfWeek yatarDay = (DayOfWeek) yatarDayCombo.getSelectedItem();
        DayOfWeek yatarNextDay = yatarDay.plus(1);

        Map<DayOfWeek, List<String>> schedule = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek d : DayOfWeek.values()) schedule.put(d, new ArrayList<>());

        // Günlük kapasite limitlerini tanımla
        Map<DayOfWeek, Integer> dailyLimits = new HashMap<>();
        dailyLimits.put(DayOfWeek.MONDAY, 6);
        dailyLimits.put(DayOfWeek.TUESDAY, 7);
        dailyLimits.put(DayOfWeek.WEDNESDAY, 7);
        dailyLimits.put(DayOfWeek.THURSDAY, 7);
        dailyLimits.put(DayOfWeek.FRIDAY, 7); // Cuma için 7 ama algoritma "mecbur kalmadıkça" buraya atmayacak

        List<Vehicle> allVehicles = new ArrayList<>();
        for(int i=0; i<vehicleListModel.size(); i++) allVehicles.add(vehicleListModel.get(i));

        // 1. ADIM: Yeni gelenleri Cumartesiye ayır
        List<Vehicle> newcomers = allVehicles.stream().filter(v -> v.isNewcomer).collect(Collectors.toList());
        for(Vehicle v : newcomers) {
            schedule.get(DayOfWeek.SATURDAY).add(v.plate);
            v.isNewcomer = false; // Gelecek hafta normale dönecek
            v.lastRestDay = DayOfWeek.FRIDAY;
        }

        // 2. ADIM: Normal araçları (ve yatar aracını) yerleştir
        List<Vehicle> regulars = allVehicles.stream().filter(v -> !newcomers.contains(v)).collect(Collectors.toList());

        // Araçları geçen haftaki günlerine göre sırala (Pazartesi olanlar en önce)
        regulars.sort(Comparator.comparingInt(v -> v.lastRestDay.getValue()));

        for (Vehicle v : regulars) {
            DayOfWeek assignedDay = null;
            DayOfWeek deadline = v.lastRestDay;
            if (deadline.getValue() > 5) deadline = DayOfWeek.FRIDAY;

            // Aracı Pazartesi'den başlayarak Deadline gününe kadar boş yer aramaya sok
            for (int d = 1; d <= deadline.getValue(); d++) {
                DayOfWeek currentDay = DayOfWeek.of(d);

                // Yatar kısıtlaması kontrolü
                if (v.equals(yatarV) && (currentDay == yatarDay || currentDay == yatarNextDay)) continue;

                // Cuma stratejisi: Eğer deadline Cuma değilse ve başka günlerde yer varsa Cuma'yı pas geç
                if (currentDay == DayOfWeek.FRIDAY && deadline != DayOfWeek.FRIDAY) continue;

                // Kapasite kontrolü
                if (schedule.get(currentDay).size() < dailyLimits.get(currentDay)) {
                    assignedDay = currentDay;
                    break;
                }
            }

            // Eğer hala yer bulamadıysa (tüm günler dolduysa), zorunlu olarak boş bir yere at (Cuma dahil)
            if (assignedDay == null) {
                for (int d = 1; d <= 5; d++) {
                    DayOfWeek backupDay = DayOfWeek.of(d);
                    if (v.equals(yatarV) && (backupDay == yatarDay || backupDay == yatarNextDay)) continue;
                    if (schedule.get(backupDay).size() < 10) { // Genişletilmiş limit
                        assignedDay = backupDay;
                        break;
                    }
                }
            }

            if (assignedDay != null) {
                schedule.get(assignedDay).add(v.plate);
                v.lastRestDay = assignedDay;
            }
        }

        updateTable(schedule);
    }

    private void updateTable(Map<DayOfWeek, List<String>> schedule) {
        tableModel.setRowCount(0);
        int maxRows = 0;
        for (List<String> list : schedule.values()) maxRows = Math.max(maxRows, list.size());

        for (int i = 0; i < maxRows; i++) {
            Object[] row = new Object[7];
            for (int d = 1; d <= 7; d++) {
                List<String> cars = schedule.get(DayOfWeek.of(d));
                row[d - 1] = (i < cars.size()) ? cars.get(i) : "";
            }
            tableModel.addRow(row);
        }
    }

    private void loadInitialData() {
        BASE_REST_MAP.forEach((plate, day) -> {
            Vehicle v = new Vehicle(plate, false);
            v.lastRestDay = day;
            vehicleListModel.addElement(v);
            yatarVehicleCombo.addItem(v);
        });
    }
}