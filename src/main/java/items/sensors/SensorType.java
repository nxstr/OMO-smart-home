package items.sensors;


public enum SensorType {
    TEMPERATURE("temperature"), //activates air conditioner
    FIRE("fire"), //activate alarm, sets all devices and sensors to fixing (cause it s no use of electrisity state) when all people are out
    WATER("water"), //closes all waterusing devices, sets to resting state an activate alarm
    ELECTRICITY("electricity"), //sets all devices and sensors to idle, activate alarm
    ENTITY("entity"); //checks if someone is entered/leaved room

    private final String name;

    SensorType(String name) {
        this.name = name;
    }
}
