package items.device;

import house.Room;

public class Lock extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 1;
    private static final int electricityInOffState = 1;

    public Lock(Room currentRoom) {
        super(DeviceType.LOCK, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
