
package smarthome.model;

public class Fan extends Device {

    // Creates a fan with the default name
    public Fan() {
        super("Fan");
    }

    // Creates a fan with a custom name
    public Fan(String deviceName) {
        super(deviceName);
    }
}
