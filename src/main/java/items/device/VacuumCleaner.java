package items.device;

import house.Room;

public class VacuumCleaner extends Device{

    private static final int usingHours = 2;
    private static final int electricityInOnState = 5; //standing on charger
    private static final int electricityInOffState = 0; //while cleaning
    public VacuumCleaner(Room currentRoom) {
        super(DeviceType.VACUUM_CLEANER, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
