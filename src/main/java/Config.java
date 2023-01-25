import house.*;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.sensors.*;
import items.state.BrokenState;
import livingEntities.*;

public class Config {
    private final House.HouseBuilder house = House.newBuilder();
    private final RoomFactory roomFactory = RoomFactory.getInstance();
    private final DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private final SensorFactory sensorFactory = SensorFactory.getInstance();
    private final SportEquipmentFactory equipmentFactory = SportEquipmentFactory.getInstance();

    public void configure(){
        Floor floor = new Floor(0);
        house.setOutSideArea(OutSideArea.getInstance());
        house.addFloor(floor);
        Room room = roomFactory.create("Bedroom", floor);
        floor.addRoom(room);
        Adult dad = new Adult("Bob", EntityType.ADULT, room, 30);
        Child kid = new Child("Artur", EntityType.CHILD, room, 8);
        house.addLivingEntity(dad);
        house.addLivingEntity(kid);
        Pet dog1 = new Pet("Jessy", EntityType.DOG,room);
        Pet dog2 = new Pet("Archi", EntityType.DOG,room);
        house.addLivingEntity(dog1);
        house.addLivingEntity(dog2);
        House house1 = house.build();
        equipmentFactory.createEquipment(room, SportEquipmentType.PET_TOY);
        equipmentFactory.createEquipment(room, SportEquipmentType.PET_TOY);
        equipmentFactory.createEquipment(room, SportEquipmentType.BICYCLE);
        DeviceType[] arr = new DeviceType[]{
                DeviceType.COFFEE_MACHINE, DeviceType.DISHWASHER, DeviceType.AIR_CONDITIONER,
                DeviceType.PET_FEEDER, DeviceType.TV, DeviceType.VACUUM_CLEANER, DeviceType.WASHING_MACHINE,
                DeviceType.PET_FOUNTAIN, DeviceType.LOCK, DeviceType.FRIDGE
        };
        SensorType[] arr1 = new SensorType[]{
                SensorType.ENTITY, SensorType.TEMPERATURE
        };
        for(DeviceType type:arr){
            if(type == DeviceType.PET_FEEDER){
                for(LivingEntity e: house1.getLivingEntities()){
                    if(e.getType()==EntityType.DOG){
                        deviceFactory.createDevice(room, DeviceType.PET_FEEDER);
                    }
                }
            }else{
                deviceFactory.createDevice(room, type);
            }
        }
        for(SensorType type:arr1){
            sensorFactory.createSensor(room, type);
        }
    }
}
