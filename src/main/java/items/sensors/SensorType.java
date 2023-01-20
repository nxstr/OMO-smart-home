package items.sensors;


import java.util.Objects;

public enum SensorType {
    TEMPERATURE("temperature"),
    FIRE("fire"),
    WATER_LEAK("water_leak"),
    ELECTRICITY("electricity");

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
