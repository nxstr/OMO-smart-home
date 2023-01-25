package items.equipment;

import house.Room;

public class Ski extends SportEquipment{

    private static final int usingHours = 15;

    protected Ski(Room currentRoom) {
        super(SportEquipmentType.SKI, usingHours, currentRoom);
    }
}
