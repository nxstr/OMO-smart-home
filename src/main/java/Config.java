import house.*;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.sensors.TemperatureSensor;
import livingEntities.Adult;
import livingEntities.EntityType;
import livingEntities.LivingEntity;
import livingEntities.Pet;

public class Config {
    private final House.HouseBuilder house = House.newBuilder();
    private final RoomFactory roomFactory = RoomFactory.getInstance();
    private final DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private final SportEquipmentFactory equipmentFactory = SportEquipmentFactory.getInstance();

    public void configure(){
        Floor floor = new Floor(0);
        house.setOutSideArea(OutSideArea.getInstance());
        house.addFloor(floor);
        Room room = roomFactory.create("Bedroom", floor);
        floor.addRoom(room);
        TemperatureSensor sensor = new TemperatureSensor(room);
        room.addElectricalItem(sensor);
        Adult dad = new Adult("Bob", EntityType.ADULT, 30, room);
        house.addLivingEntity(dad);
        Pet dog1 = new Pet("Jessy", EntityType.DOG,room);
        Pet dog2 = new Pet("Archi", EntityType.DOG,room);
        house.addLivingEntity(dog1);
        house.addLivingEntity(dog2);
        House house1 = house.build();
        equipmentFactory.createEquipment(room, SportEquipmentType.PET_TOY);
        equipmentFactory.createEquipment(room, SportEquipmentType.PET_TOY);
        DeviceType[] arr = new DeviceType[]{
                DeviceType.COFFEE_MACHINE, DeviceType.DISHWASHER, DeviceType.AIR_CONDITIONER,
                DeviceType.PET_FEEDER, DeviceType.TV, DeviceType.VACUUM_CLEANER, DeviceType.WASHING_MACHINE,
                DeviceType.PET_FOUNTAIN
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
    }
}
