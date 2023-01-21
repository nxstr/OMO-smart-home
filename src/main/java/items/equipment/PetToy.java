package items.equipment;

import house.Room;

public class PetToy extends SportEquipment{

    private static final int usingHours = 12;

    protected PetToy(Room currentRoom) {
        super(SportEquipmentType.PET_TOY, usingHours, currentRoom);
    }
}
