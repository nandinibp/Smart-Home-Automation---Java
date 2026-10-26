
package smarthome.database;

import smarthome.model.Light;
import java.util.List;

public class TestDeviceDAO {

    public static void main(String[] args) {

        DeviceDAO dao = new DeviceDAO();

        // Create a light with a unique name
        Light light = new Light("Bedroom Light");

        // Add the light to the database
        boolean added = dao.addDevice(light);

        if (added) {
            System.out.println("Device added successfully!");
        } else {
            System.out.println(
                "Device was not added. It may already exist."
            );
        }

        // Retrieve all devices from MySQL
        List<String> devices = dao.getAllDevices();

        System.out.println("\nDevices in MySQL:");

        for (String device : devices) {
            System.out.println(device);
        }
    }
}
