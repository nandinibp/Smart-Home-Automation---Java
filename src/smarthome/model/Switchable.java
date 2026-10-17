package smarthome.model;

public interface Switchable {
    void turnOn();
    void turnOff();
    boolean isOn();
}