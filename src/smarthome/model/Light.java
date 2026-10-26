
package smarthome.model;

public class Light extends Device {

    // Creates a light with the default name
    public Light() {
        super("Light");
    }

    // Creates a light with a custom name
    public Light(String deviceName) {
        super(deviceName);
    }
}
