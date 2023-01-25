package items.device;

import house.Room;

public class Television extends Device{

    private static final int usingHours = 6;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;

    public Television(Room currentRoom) {
        super(DeviceType.TV, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
