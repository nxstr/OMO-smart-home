package items.sensors;

import house.Room;

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

        public void createSensor(Room room, SensorType type){
            Sensor sensor = switch (type) {
                case TEMPERATURE -> new TemperatureSensor(room);
                case ENTITY -> new EntitySensor(room);
                case FIRE -> new FireSensor(room);
                case WATER -> new WaterSensor(room);
                case ELECTRICITY -> new ElectricitySensor(room);
                default -> null;
            };
            if(sensor!=null) {
                room.addElectricalItem(sensor);
                sensors.add(sensor);
            }
        }

        public List<Sensor> getSensors() {
            return sensors;
        }
}