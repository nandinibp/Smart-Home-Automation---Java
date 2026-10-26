
package smarthome.database;

import smarthome.model.Device;
import smarthome.model.Light;
import smarthome.model.Fan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    // Add a device to MySQL
    public boolean addDevice(Device device) {

        if (device == null
                || device.getDeviceName() == null
                || device.getDeviceName().trim().isEmpty()) {
            System.out.println("Error: Invalid device details.");
            return false;
        }

        String sql = "INSERT IGNORE INTO Devices "
                + "(device_name, device_type, status) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, device.getDeviceName().trim());
            ps.setString(2, device.getClass().getSimpleName());
            ps.setString(3, device.isOn() ? "ON" : "OFF");

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding device: "
                    + e.getMessage());
            return false;
        }
    }

    // Retrieve all devices as displayable text
    public List<String> getAllDevices() {

        List<String> devices = new ArrayList<>();

        String sql = "SELECT device_id, device_name, "
                + "device_type, status FROM Devices "
                + "ORDER BY device_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String details = rs.getInt("device_id") + " | "
                        + rs.getString("device_name") + " | "
                        + rs.getString("device_type") + " | "
                        + rs.getString("status");

                devices.add(details);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving devices: "
                    + e.getMessage());
        }

        return devices;
    }

    // Retrieve all saved devices as Java objects
    public List<Device> getAllDeviceObjects() {

        List<Device> devices = new ArrayList<>();

        String sql = "SELECT device_name, device_type, status "
                + "FROM Devices ORDER BY device_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String name = rs.getString("device_name");
                String type = rs.getString("device_type");
                String status = rs.getString("status");

                Device device = null;

                if ("Light".equalsIgnoreCase(type)) {
                    device = new Light(name);
                } else if ("Fan".equalsIgnoreCase(type)) {
                    device = new Fan(name);
                } else {
                    System.out.println(
                            "Unsupported device type in MySQL: " + type);
                    continue;
                }

                if ("ON".equalsIgnoreCase(status)) {
                    device.turnOn();
                } else {
                    device.turnOff();
                }

                devices.add(device);
            }

        } catch (SQLException e) {
            System.out.println("Error loading devices from MySQL: "
                    + e.getMessage());
        }

        return devices;
    }

    // Retrieve one device's saved status
    public String getDeviceStatus(String deviceName) {

        if (deviceName == null || deviceName.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT status FROM Devices "
                + "WHERE device_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, deviceName.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");

                    if ("ON".equalsIgnoreCase(status)
                            || "OFF".equalsIgnoreCase(status)) {
                        return status.toUpperCase();
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving device status: "
                    + e.getMessage());
        }

        return null;
    }

    // Update a device's ON/OFF status
    public boolean updateStatus(String deviceName, String status) {

        if (deviceName == null || deviceName.trim().isEmpty()) {
            System.out.println("Error: Device name cannot be empty.");
            return false;
        }

        if (status == null
                || (!status.equalsIgnoreCase("ON")
                && !status.equalsIgnoreCase("OFF"))) {
            System.out.println("Error: Status must be ON or OFF.");
            return false;
        }

        String sql = "UPDATE Devices SET status = ? "
                + "WHERE device_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.toUpperCase());
            ps.setString(2, deviceName.trim());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating status: "
                    + e.getMessage());
            return false;
        }
    }

    // Delete a device from MySQL
    public boolean deleteDevice(String deviceName) {

        if (deviceName == null || deviceName.trim().isEmpty()) {
            System.out.println("Error: Device name cannot be empty.");
            return false;
        }

        String sql = "DELETE FROM Devices WHERE device_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, deviceName.trim());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting device: "
                    + e.getMessage());
            return false;
        }
    }
}
