
package smarthome.database;

import smarthome.model.Device;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    // Add a device to the database
    public boolean addDevice(Device device) {

        String sql = "INSERT IGNORE INTO Devices " +
        "(device_name, device_type, status) " +
        "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, device.getDeviceName());
            ps.setString(2, device.getClass().getSimpleName());
            ps.setString(3, device.isOn() ? "ON" : "OFF");

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding device: "
                    + e.getMessage());
            return false;
        }
    }

    // Retrieve all devices from the database
    public List<String> getAllDevices() {

        List<String> devices = new ArrayList<>();

        String sql = "SELECT device_id, device_name, " +
                "device_type, status FROM Devices";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String details =
                        rs.getInt("device_id") + " | " +
                        rs.getString("device_name") + " | " +
                        rs.getString("device_type") + " | " +
                        rs.getString("status");

                devices.add(details);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving devices: "
                    + e.getMessage());
        }

        return devices;
    }

    // Update a device's ON/OFF status
    public boolean updateStatus(String deviceName, String status) {

        String sql = "UPDATE Devices SET status = ? " +
                "WHERE device_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, deviceName);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating status: "
                    + e.getMessage());
            return false;
        }
    }

    // Delete a device from the database
    public boolean deleteDevice(String deviceName) {

        String sql = "DELETE FROM Devices WHERE device_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, deviceName);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting device: "
                    + e.getMessage());
            return false;
        }
    }
}
