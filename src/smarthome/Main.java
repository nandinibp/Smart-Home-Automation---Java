
package smarthome;

import smarthome.exception.DeviceNotFoundException;
import smarthome.model.Device;
import smarthome.model.Light;
import smarthome.model.Fan;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private final List<Device> devices = new ArrayList<>();

    private JLabel lightStatus;
    private JLabel fanStatus;
    private JTextArea activityLog;

    public Main() {

        devices.add(new Light());
        devices.add(new Fan());

        setTitle("Smart Home Automation System");
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel heading = new JLabel("Smart Home Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel devicePanel = new JPanel(new GridLayout(2, 2, 10, 10));

        lightStatus = new JLabel("Light: OFF");
        fanStatus = new JLabel("Fan: OFF");

        JButton lightButton = new JButton("Toggle Light");
        JButton fanButton = new JButton("Toggle Fan");

        devicePanel.add(lightStatus);
        devicePanel.add(lightButton);
        devicePanel.add(fanStatus);
        devicePanel.add(fanButton);

        lightButton.addActionListener(e -> toggleDevice("Light"));
        fanButton.addActionListener(e -> toggleDevice("Fan"));

        JButton viewButton = new JButton("View Devices");
        JButton addButton = new JButton("Add Device");
        JButton searchButton = new JButton("Search Device");
        JButton removeButton = new JButton("Remove Device");

        JPanel managementPanel = new JPanel(
            new GridLayout(1, 4, 8, 8)
        );

        managementPanel.add(viewButton);
        managementPanel.add(addButton);
        managementPanel.add(searchButton);
        managementPanel.add(removeButton);

        viewButton.addActionListener(e -> viewDevices());
        addButton.addActionListener(e -> addDevice());
        searchButton.addActionListener(e -> searchDevice());
        removeButton.addActionListener(e -> removeDevice());

        activityLog = new JTextArea(8, 30);
        activityLog.setEditable(false);
        activityLog.setLineWrap(true);
        activityLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(activityLog);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(devicePanel, BorderLayout.NORTH);
        centerPanel.add(managementPanel, BorderLayout.CENTER);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        log("Smart Home System started.");
        log("Light and Fan are ready.");
    }

    private Device findDevice(String name)
            throws DeviceNotFoundException {

        for (Device device : devices) {
            if (device.getDeviceName().equalsIgnoreCase(name)) {
                return device;
            }
        }

        throw new DeviceNotFoundException(
            "Device not found: " + name
        );
    }

    private void toggleDevice(String name) {

        try {
            Device device = findDevice(name);

            if (device.isOn()) {
                device.turnOff();
                log(name + " turned OFF.");
            } else {
                device.turnOn();
                log(name + " turned ON.");
            }

            updateStatus();

        } catch (DeviceNotFoundException e) {
            showError(e.getMessage());
        }
    }

    private void updateStatus() {

        try {
            Device light = findDevice("Light");
            Device fan = findDevice("Fan");

            lightStatus.setText(
                "Light: " + (light.isOn() ? "ON" : "OFF")
            );

            fanStatus.setText(
                "Fan: " + (fan.isOn() ? "ON" : "OFF")
            );

        } catch (DeviceNotFoundException e) {
            lightStatus.setText(
                "Light: " + (deviceExists("Light") ? "ON/OFF" : "Removed")
            );

            fanStatus.setText(
                "Fan: " + (deviceExists("Fan") ? "ON/OFF" : "Removed")
            );
        }
    }

    private boolean deviceExists(String name) {
        for (Device device : devices) {
            if (device.getDeviceName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private void viewDevices() {

        if (devices.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No devices registered."
            );
            return;
        }

        StringBuilder result = new StringBuilder();

        for (Device device : devices) {
            result.append(device.getDeviceName())
                  .append(" - ")
                  .append(device.isOn() ? "ON" : "OFF")
                  .append("\n");
        }

        JOptionPane.showMessageDialog(
            this,
            result.toString(),
            "Registered Devices",
            JOptionPane.INFORMATION_MESSAGE
        );

        log("Viewed registered devices.");
    }

    private void addDevice() {

        String[] options = {"Light", "Fan"};

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Choose a device to add:",
            "Add Device",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == null) {
            return;
        }

        if (deviceExists(choice)) {
            showError(choice + " is already registered.");
            return;
        }

        if (choice.equals("Light")) {
            devices.add(new Light());
        } else {
            devices.add(new Fan());
        }

        log(choice + " added successfully.");
        updateStatus();

        JOptionPane.showMessageDialog(
            this, choice + " added successfully."
        );
    }

    private void searchDevice() {

        String name = JOptionPane.showInputDialog(
            this, "Enter device name (Light or Fan):"
        );

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        try {
            Device device = findDevice(name.trim());

            JOptionPane.showMessageDialog(
                this,
                device.getDeviceName() + " status: "
                    + (device.isOn() ? "ON" : "OFF"),
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
            );

            log("Searched for " + device.getDeviceName() + ".");

        } catch (DeviceNotFoundException e) {
            showError(e.getMessage());
        }
    }

    private void removeDevice() {

        String name = JOptionPane.showInputDialog(
            this, "Enter device name to remove:"
        );

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        try {
            Device device = findDevice(name.trim());
            devices.remove(device);

            log(device.getDeviceName() + " removed.");
            updateStatus();

            JOptionPane.showMessageDialog(
                this,
                device.getDeviceName() + " removed successfully."
            );

        } catch (DeviceNotFoundException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        log("Error: " + message);
    }

    private void log(String message) {
        activityLog.append(message + "\n");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
