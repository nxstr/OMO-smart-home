package items.equipment;

import house.Room;
import items.device.*;

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

    public SportEquipment createEquipment(Room room, SportEquipmentType type){
        SportEquipment equipment = switch (type) {
            case BICYCLE -> new Bicycle(room);
            case SKI -> new Ski(room);
            case PET_TOY -> new PetToy(room);
            default -> null;
        };

        room.addEquipment(equipment);
        equipments.add(equipment);
        return equipment;
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
