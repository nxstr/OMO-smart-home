package items.equipment;

import house.Room;

public class Ski extends SportEquipment{

    private static final int usingHours = 3;

    protected Ski(Room currentRoom) {
        super(SportEquipmentType.SKI, usingHours, currentRoom);
    }
}
