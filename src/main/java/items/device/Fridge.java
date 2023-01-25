package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;

import java.time.LocalTime;

public class Fridge extends Device{

    private static int usingHours = 1;
    private static final int electricityInOnState = 7;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 20;
    private int currentCapacity = maxCapacity;

    public Fridge(Room currentRoom) {
        super(DeviceType.FRIDGE, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        System.out.println("Fridge is refilled");
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }


    public void usingDevice() {
        if (isEmpty()) {
            setCurrentState(new IdleState(this));
            System.out.println("Food in Fridge is needs to refill");
        } else {
            setCurrentState(new ActiveState(this));
            setUsedTimes(getUsedTimes() + 1);
            currentCapacity--;
            System.out.println("Fridge is using at " + getHouse().getTime() + ", " + currentCapacity + " portions of food are/is left");
            breakingEvent();
        }
    }

    public void fixingItem() {
        if(currentCapacity==0){
            refill();
        }else{
            super.fixingItem();
        }
    }
}
