package items.equipment;

import house.Room;

import java.util.ArrayList;
import java.util.List;

public class SportEquipmentFactory {
    private static SportEquipmentFactory instance = null;
    private final List<SportEquipment> equipments = new ArrayList<>();

    public SportEquipmentFactory() {
    }

    public static SportEquipmentFactory getInstance() {
        if (instance == null){
            instance = new SportEquipmentFactory();
        }
        return instance;
    }

    public void createEquipment(Room room, SportEquipmentType type){
        SportEquipment equipment = switch (type) {
            case BICYCLE -> new Bicycle(room);
            case SKI -> new Ski(room);
            case PET_TOY -> new PetToy(room);
            default -> null;
        };
        if(equipment!=null) {
            room.addEquipment(equipment);
            equipments.add(equipment);
        }
    }

    public SportEquipment findEquipmentByType(SportEquipmentType name) {
        for (SportEquipment equipment : equipments) {
            if (equipment.getType().equals(name)) return equipment;
        }
        return null;
    }

    public List<SportEquipment> getEquipments() {
        return equipments;
    }
}
