
package smarthome.monitor;

import smarthome.model.Device;
import smarthome.service.DeviceManager;

import java.util.List;

public class DeviceMonitor extends Thread {

    private final DeviceManager manager;
    private volatile boolean running = true;

    public DeviceMonitor(DeviceManager manager) {
        this.manager = manager;
        setName("SmartHome-DeviceMonitor");
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running && !isInterrupted()) {
            try {
                synchronized (manager) {
                    List<Device> devices = manager.getAllDevices();

                    System.out.println(
                            "\n--- Smart Home Monitor ---"
                    );

                    for (Device device : devices) {
                        System.out.println(
                                device.getDeviceName()
                                + " status: "
                                + (device.isOn() ? "ON" : "OFF")
                        );
                    }

                    System.out.println("--------------------------");
                }

                Thread.sleep(5000);

            } catch (InterruptedException ex) {
                interrupt();
                break;
            }
        }
    }

    public void stopMonitoring() {
        running = false;
        interrupt();
    }
}
