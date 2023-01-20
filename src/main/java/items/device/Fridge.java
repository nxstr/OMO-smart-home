package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;

public class Fridge extends Device{

    private static final int usingHours = 24;
    private static final int electricityInOnState = 7;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 20;
    private int currentCapacity = maxCapacity;

    public Fridge(Room currentRoom) {
        super(DeviceType.FRIDGE, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        System.out.println("Fridge is filled");
        currentCapacity = maxCapacity;
    }


    public void usingDevice() {
        setUsedTimes(getUsedTimes() + 1);
        if (isEmpty()) {
            setCurrentState(new IdleState(this));
            System.out.println("Food in Fridge is over");
        } else {
            setCurrentState(new ActiveState(this));
            currentCapacity--;
        }
    }
}
