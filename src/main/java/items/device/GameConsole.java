package items.device;

import house.Room;

public class GameConsole extends Device{

    private static final int usingHours = 2;
    private static final int electricityInOnState = 8;
    private static final int electricityInOffState = 1;

    public GameConsole(Room currentRoom) {
        super(DeviceType.GAME_CONSOLE, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }
}
