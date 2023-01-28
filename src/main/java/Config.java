import house.*;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.sensors.*;
import livingEntities.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Config {
    private final House.HouseBuilder house = House.newBuilder();
    private final RoomFactory roomFactory = RoomFactory.getInstance();
    private final DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private final SensorFactory sensorFactory = SensorFactory.getInstance();
    private final SportEquipmentFactory equipmentFactory = SportEquipmentFactory.getInstance();

    public void configure(String file) throws IOException, ParseException {
        SensorType[] allRoomsSensorTypes = new SensorType[]{
                SensorType.TEMPERATURE, SensorType.FIRE
        };

        Object o = new JSONParser().parse(new FileReader(file));

        JSONObject j = (JSONObject) o;
        JSONArray array = (JSONArray) j.get("house");

        house.setId(Integer.parseInt(((JSONObject) array.get(0)).get("houseId").toString()));
        house.setOutSideArea(OutsideArea.getInstance());
        House house1 = house.build();
        for (Object floor : (JSONArray) ((JSONObject) array.get(0)).get("floors")) {
            Floor f = new Floor(((Long) (((JSONObject) floor).get("level"))).intValue());
            house.addFloor(f);
            for(Object room: (JSONArray) ((JSONObject) floor).get("rooms")){
                Room r = roomFactory.create((String) ((JSONObject) room).get("name"), f);
                f.addRoom(r);
                JSONArray items = (JSONArray) ((JSONObject) room).get("items");
                if(!items.isEmpty()) {
                    JSONArray equipments = (JSONArray) (((JSONObject) items.get(0)).get("equipments"));
                    if (!equipments.isEmpty()) {
                        for (Object sport : equipments) {
                            equipmentFactory.createEquipment(r, Objects.requireNonNull(SportEquipmentType.getTypeByName((String) ((JSONObject) sport).get("type"))));
                        }
                    }
                    JSONArray devices = (JSONArray) (((JSONObject) items.get(0)).get("devices"));
                    if (!devices.isEmpty()) {
                        for (Object device : devices) {
                            deviceFactory.createDevice(r, Objects.requireNonNull(DeviceType.getTypeByName((String) ((JSONObject) device).get("type"))));
                        }
                    }
                    deviceFactory.createDevice(r, DeviceType.FIRE_SUPPRESSION);
                    for (SensorType type : allRoomsSensorTypes) {
                        sensorFactory.createSensor(r, type);
                    }
                }
            }
        }

        Room room = null;
        if(deviceFactory.getDevices().stream().noneMatch(d->d.getType().equals(DeviceType.LOCK))){
            if(roomFactory.findRoomByName("Hallway")!=null){
                room = roomFactory.findRoomByName("Garage");
            }
            else if(roomFactory.findRoomByName("Garage")!=null){
                room = roomFactory.findRoomByName("Garage");
            }else{
                room = house1.getFloors().get(0).getRooms().get(0);
            }
            deviceFactory.createDevice(room, DeviceType.LOCK);
            sensorFactory.createSensor(room, SensorType.ENTITY);
        }else{
            Device device = deviceFactory.getDevices().stream().filter(d->d.getType().equals(DeviceType.LOCK)).findAny().get();
            room = device.getCurrentRoom();
            sensorFactory.createSensor(room, SensorType.ENTITY);
        }
        sensorFactory.createSensor(room, SensorType.WATER);
        sensorFactory.createSensor(room, SensorType.ELECTRICITY);

        JSONArray entities = (JSONArray) ((JSONObject) array.get(0)).get("entities");
            JSONArray people = (JSONArray) ((JSONObject) entities.get(0)).get("people");
            if (!people.isEmpty()) {
                JSONArray adults = (JSONArray) (((JSONObject) (people.get(0))).get("adults"));
                if (!adults.isEmpty()) {
                    for (Object adult : adults) {
                        house.addLivingEntity(new Adult((String) (((JSONObject) adult).get("name")), EntityType.getTypeByName((String) (((JSONObject) adult).get("type"))),
                                roomFactory.findRoomByName((String) (((JSONObject) adult).get("room"))),
                                ((Long) (((JSONObject) adult).get("age"))).intValue()));
                    }
                }
                JSONArray children = (JSONArray) (((JSONObject) (people.get(1))).get("children"));
                if (!children.isEmpty()) {
                    for (Object child : children) {
                        house.addLivingEntity(new Child((String) (((JSONObject) child).get("name")), EntityType.CHILD,
                                roomFactory.findRoomByName((String) (((JSONObject) child).get("room"))),
                                ((Long) (((JSONObject) child).get("age"))).intValue()));
                    }
                }
            }
            JSONArray pets = (JSONArray) ((JSONObject) entities.get(1)).get("pets");
            if (!pets.isEmpty()) {
                for (Object pet : pets) {
                    house.addLivingEntity(new Pet((String) (((JSONObject) pet).get("name")), EntityType.getTypeByName((String) (((JSONObject) pet).get("type"))),
                            roomFactory.findRoomByName((String) (((JSONObject) pet).get("room")))));
                }
            }

        List<Device> feeders = deviceFactory.getPetDevices().stream().filter((d->d.getType() == DeviceType.PET_FEEDER)).toList();
        while(pets.size() > feeders.size()){
            deviceFactory.createDevice(house1.getFloors().get(0).getRooms().get(0), DeviceType.PET_FEEDER);
            feeders = deviceFactory.getPetDevices().stream().filter((d->d.getType() == DeviceType.PET_FEEDER)).toList();
        }

    }

    public List<Integer> configureSimulation(String file) throws IOException, ParseException {
        Object o = new JSONParser().parse(new FileReader(file));

        JSONObject j = (JSONObject) o;
        JSONArray array = (JSONArray) j.get("simulation");

        List<Integer> result = new ArrayList<>();
        result.add(Integer.parseInt(((JSONObject) array.get(0)).get("iterations").toString()));
        result.add(Integer.parseInt(((JSONObject) array.get(0)).get("starting_hour").toString()));
        return result;
    }
}
