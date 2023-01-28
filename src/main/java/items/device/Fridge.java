package items.device;

import house.Room;
import items.Observer;
import items.state.IdleState;
import items.state.ActiveState;

public class Fridge extends Device{

    private static final int usingHours = 1;
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
        Observer.getInstance().logAction(this.getName()+" is refilled\n");
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }


    public void usingDevice() {
        if (isEmpty()) {
            setCurrentState(new IdleState(this));
            Observer.getInstance().logAction("Food in FRIDGE is needs to refill\n");
        } else {
            setCurrentState(new ActiveState(this));
            setUsedTimes(getUsedTimes() + 1);
            currentCapacity--;
            Observer.getInstance().logAction("Fridge is using at " + getHouse().getTime() + ", " + currentCapacity + " portions of food are/is left\n");
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
