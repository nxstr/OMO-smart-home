package items.sensors;


import java.util.Objects;

public enum SensorType {
    TEMPERATURE("temperature"), //activates air conditioner
    FIRE("fire"), //activate alarm, sets all devices and sensors to fixing (cause its no use of electrisity state) when all people are out
    WATER("water"), //closes all waterusing devices, sets to resting state an activate alarm
    ELECTRICITY("electricity"), //sets all devices and sensors to idle, activate alarm
    ENTITY("entity"); //checks if someone is entered/leaved room

    private final String name;

    SensorType(String name) {
        this.name = name;
    }

    public static SensorType getTypeByName(String name) {
        for (SensorType sensor : SensorType.values()) {
            if (Objects.equals(sensor.name, name)) return sensor;
        }
        return null;
    }
}
