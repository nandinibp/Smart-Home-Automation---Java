
package smarthome;

import smarthome.model.Device;
import smarthome.model.Light;
import smarthome.model.Fan;
import smarthome.service.DeviceManager;
import smarthome.exception.DeviceNotFoundException;
import smarthome.monitor.DeviceMonitor;
import smarthome.database.DeviceDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main extends JFrame {

    private final DeviceManager manager = new DeviceManager();
    private final DeviceDAO dao = new DeviceDAO();

    private DeviceMonitor monitor;
    private JLabel lightStatus;
    private JLabel fanStatus;
    private JTextArea activityLog;

    public Main() {
        setTitle("Smart Home Automation");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        saveDefaultDevice("Light", new Light());
        saveDefaultDevice("Fan", new Fan());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (monitor != null) {
                    monitor.stopMonitoring();
                }
                log("Smart Home Automation closed.");
                dispose();
            }
        });

        JLabel heading = new JLabel(
                "Smart Home Dashboard",
                SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        lightStatus = new JLabel("Light: OFF", SwingConstants.CENTER);
        fanStatus = new JLabel("Fan: OFF", SwingConstants.CENTER);

        centerPanel.add(lightStatus);
        centerPanel.add(fanStatus);

        JButton lightButton = new JButton("Toggle Light");
        JButton fanButton = new JButton("Toggle Fan");
        JButton viewButton = new JButton("View Devices");
        JButton addButton = new JButton("Add Device");
        JButton searchButton = new JButton("Search Device");
        JButton removeButton = new JButton("Remove Device");

        centerPanel.add(lightButton);
        centerPanel.add(fanButton);
        centerPanel.add(viewButton);
        centerPanel.add(addButton);
        centerPanel.add(searchButton);
        centerPanel.add(removeButton);

        add(centerPanel, BorderLayout.CENTER);

        activityLog = new JTextArea(7, 40);
        activityLog.setEditable(false);
        activityLog.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(activityLog);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Activity Log")
        );
        add(scrollPane, BorderLayout.SOUTH);

        lightButton.addActionListener(e -> toggleDevice("Light"));
        fanButton.addActionListener(e -> toggleDevice("Fan"));
        viewButton.addActionListener(e -> viewDevices());
        addButton.addActionListener(e -> addDevice());
        searchButton.addActionListener(e -> searchDevice());
        removeButton.addActionListener(e -> removeDevice());

        updateStatus();
        log("Smart Home Automation started.");

        monitor = new DeviceMonitor(manager);
        monitor.start();

        log("Background device monitor started.");
    }

    private void saveDefaultDevice(String name, Device device) {
        try {
            boolean saved = dao.addDevice(device);

            if (saved) {
                log(name + " saved to MySQL.");
            } else {
                log(name + " already exists in MySQL.");
            }
        } catch (Exception ex) {
            log("Database error saving " + name + ": "
                    + ex.getMessage());
        }
    }

    private void toggleDevice(String name) {
        Device device;

        synchronized (manager) {
            device = manager.findDevice(name);

            if (device == null) {
                showError("Device not found: " + name);
                return;
            }

            if (device.isOn()) {
                device.turnOff();
            } else {
                device.turnOn();
            }
        }

        String status = device.isOn() ? "ON" : "OFF";

        if (dao.updateStatus(name, status)) {
            log(name + " turned " + status
                    + " and database updated.");
        } else {
            log(name + " changed to " + status
                    + " in memory, but no database row was updated.");
        }

        updateStatus();
    }

    private void updateStatus() {
        updateOneStatus("Light", lightStatus);
        updateOneStatus("Fan", fanStatus);
    }

    private void updateOneStatus(String name, JLabel label) {
        synchronized (manager) {
            Device device = manager.findDevice(name);

            if (device == null) {
                label.setText(name + ": Not Added");
            } else {
                label.setText(
                        name + ": " + (device.isOn() ? "ON" : "OFF")
                );
            }
        }
    }

    private void viewDevices() {
        List<String> devices = dao.getAllDevices();

        if (devices.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No devices found in the database."
            );
            return;
        }

        StringBuilder result =
                new StringBuilder("Devices stored in MySQL:\n\n");

        for (String device : devices) {
            result.append(device).append("\n");
        }

        JOptionPane.showMessageDialog(this, result.toString());
        log("Viewed devices from MySQL.");
    }

    private void addDevice() {
        String[] options = {"Light", "Fan"};

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select a device type:",
                "Add Device",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) {
            return;
        }

        String name = JOptionPane.showInputDialog(
                this,
                "Enter a name for the " + choice + ":",
                "Device Name",
                JOptionPane.QUESTION_MESSAGE
        );

        if (name == null) {
            return;
        }

        name = name.trim();

        if (name.isEmpty()) {
            showError("Device name cannot be empty.");
            return;
        }

        synchronized (manager) {
            if (manager.findDevice(name) != null) {
                showError("A device with this name already exists.");
                return;
            }
        }

        Device device;

        if (choice.equals("Light")) {
            device = new Light(name);
        } else {
            device = new Fan(name);
        }

        boolean saved = dao.addDevice(device);

        if (!saved) {
            showError(
                    "The device could not be saved. "
                    + "It may already exist in MySQL."
            );
            log("Failed to add " + name + " to MySQL.");
            return;
        }

        synchronized (manager) {
            manager.addDevice(device);
        }

        log(choice + " '" + name
                + "' added to the dashboard and MySQL.");

        JOptionPane.showMessageDialog(
                this,
                choice + " '" + name + "' added successfully!"
        );

        updateStatus();
    }

    private void searchDevice() {
        String name = JOptionPane.showInputDialog(
                this,
                "Enter the device name to search:"
        );

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        Device device;

        synchronized (manager) {
            device = manager.findDevice(name.trim());
        }

        if (device == null) {
            showError("Device not found: " + name.trim());
            log("Search failed: " + name.trim());
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Device: " + device.getDeviceName()
                + "\nType: " + device.getClass().getSimpleName()
                + "\nStatus: " + (device.isOn() ? "ON" : "OFF")
        );

        log("Searched for " + device.getDeviceName() + ".");
    }

    private void removeDevice() {
        String name = JOptionPane.showInputDialog(
                this,
                "Enter the device name to remove:"
        );

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        name = name.trim();

        if (name.equalsIgnoreCase("Light")
                || name.equalsIgnoreCase("Fan")) {
            showError(
                    "The default Light and Fan cannot be removed."
            );
            return;
        }

        Device device;

        synchronized (manager) {
            device = manager.findDevice(name);
        }

        if (device == null) {
            showError("Device not found: " + name);
            log("Removal failed: " + name);
            return;
        }

        boolean deleted = dao.deleteDevice(name);

        if (!deleted) {
            showError(
                    "The device could not be deleted from MySQL."
            );
            log("Could not remove " + name + " from MySQL.");
            return;
        }

        try {
            synchronized (manager) {
                manager.removeDevice(name);
            }
        } catch (DeviceNotFoundException ex) {
            showError(ex.getMessage());
            log("Removal failed: " + ex.getMessage());
            return;
        }

        log(name + " removed from the dashboard and MySQL.");

        JOptionPane.showMessageDialog(
                this,
                name + " removed successfully!"
        );

        updateStatus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Smart Home Automation",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void log(String message) {
        if (activityLog == null) {
            return;
        }

        String time = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );

        activityLog.append("[" + time + "] " + message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main dashboard = new Main();
            dashboard.setVisible(true);
        });
    }
}
