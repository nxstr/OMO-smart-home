package items.device;

import house.Room;

public class VacuumCleaner extends Device{

    private static int usingHours = 15;
    private static final int electricityInOnState = 0; //standing on charger
    private static final int electricityInOffState = 5; //while cleaning
    public VacuumCleaner(Room currentRoom) {
        super(DeviceType.VACUUM_CLEANER, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
