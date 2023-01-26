package items.sensors;

import house.Room;
import items.device.*;

import java.util.ArrayList;
import java.util.List;

public class SensorFactory {
        private static SensorFactory instance = null;
        private final List<Sensor> sensors = new ArrayList<>();

        public SensorFactory() {
        }

        public static SensorFactory getInstance() {
            if (instance == null){
                instance = new SensorFactory();
            }
            return instance;
        }

        public Sensor createSensor(Room room, SensorType type){
            Sensor sensor = switch (type) {
                case TEMPERATURE -> new TemperatureSensor(room);
                case ENTITY -> new EntitySensor(room);
                case FIRE -> new FireSensor(room);
                case WATER -> new WaterSensor(room);
                case ELECTRICITY -> new ElectricitySensor(room);
                default -> null;
            };

            room.addElectricalItem(sensor);
            sensors.add(sensor);
            return sensor;
        }

        public Sensor findSensorByName(String name) {
            for (Sensor sensor : sensors) {
                if (sensor.getName().equals(name)) return sensor;
            }
            return null;
        }

        public List<Sensor> getSensors() {
            return sensors;
        }
}