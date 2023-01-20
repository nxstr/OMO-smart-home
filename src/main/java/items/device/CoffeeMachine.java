package items.device;

import house.Room;
import items.state.BrokenState;
import items.state.ActiveState;

import java.time.LocalTime;

public class CoffeeMachine extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 10;
    private int currentCapacity = maxCapacity;

    public CoffeeMachine(Room currentRoom) {
        super(DeviceType.COFFEE_MACHINE, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        System.out.println("Coffee Machine is filled");
        currentCapacity = maxCapacity;
    }

    public void usingDevice(LocalTime time) {
        if (isEmpty()) {
            setCurrentState(new BrokenState(this));
            System.out.println("Coffee beans in coffee machine are over");
            generateReportForObserver();
        } else {
            currentCapacity--;
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            System.out.println("Coffee Machine is starting at " + time + ", " + currentCapacity + " portions are/is left");
        }
    }
}
