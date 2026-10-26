
package smarthome.service;

import smarthome.model.Device;
import smarthome.model.Light;
import smarthome.model.Fan;
import smarthome.exception.DeviceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class DeviceManager {

    private final List<Device> devices = new ArrayList<>();

    public DeviceManager() {
        devices.add(new Light());
        devices.add(new Fan());
    }

    public void addDevice(Device device) {
        if (device != null && findDevice(device.getDeviceName()) == null) {
            devices.add(device);
        }
    }

    public List<Device> getAllDevices() {
        return new ArrayList<>(devices);
    }

    public Device findDevice(String deviceName) {
        for (Device device : devices) {
            if (device.getDeviceName().equalsIgnoreCase(deviceName)) {
                return device;
            }
        }
        return null;
    }

    public Device getDevice(String deviceName)
            throws DeviceNotFoundException {

        Device device = findDevice(deviceName);

        if (device == null) {
            throw new DeviceNotFoundException(
                "Device not found: " + deviceName
            );
        }

        return device;
    }

    public void turnOnDevice(String deviceName)
            throws DeviceNotFoundException {
        getDevice(deviceName).turnOn();
    }

    public void turnOffDevice(String deviceName)
            throws DeviceNotFoundException {
        getDevice(deviceName).turnOff();
    }

    public boolean removeDevice(String deviceName)
            throws DeviceNotFoundException {

        Device device = getDevice(deviceName);
        devices.remove(device);
        return true;
    }

    public Light createLight(String deviceName) {
        Light light = new Light(deviceName);
        addDevice(light);
        return light;
    }

    public Fan createFan(String deviceName) {
        Fan fan = new Fan(deviceName);
        addDevice(fan);
        return fan;
    }
}
