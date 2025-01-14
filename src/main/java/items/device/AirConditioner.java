package items.device;

import house.Room;

public class AirConditioner extends Device{

    private static final int usingHours = 6;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 2;

    public AirConditioner(Room currentRoom) {
        super(DeviceType.AIR_CONDITIONER, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
