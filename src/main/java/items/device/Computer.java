package items.device;

import house.Room;

public class Computer extends Device {

    private static final int usingHours = 3;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;

    public Computer(Room currentRoom) {
        super(DeviceType.COMPUTER, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
