package items.equipment;

import house.Room;

public class Bicycle extends SportEquipment{

    private static final int usingHours = 12;

    protected Bicycle(Room currentRoom) {
        super(SportEquipmentType.BICYCLE, usingHours, currentRoom);
    }
}
