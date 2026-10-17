package smarthome.model;

public abstract class Device implements Switchable {

    private final String deviceName;
    protected boolean on;

    public Device(String deviceName) {
        this.deviceName = deviceName;
        this.on = false;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public void turnOn() {
        on = true;
    }

    @Override
    public void turnOff() {
        on = false;
    }

    @Override
    public boolean isOn() {
        return on;
    }
}