package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.ActiveState;
import items.state.IdleState;

public class CoffeeMachine extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 10;
    private int currentCapacity = maxCapacity;

    public CoffeeMachine(Room currentRoom) {
        super(DeviceType.COFFEE_MACHINE, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        Observer.getInstance().logAction(this.getName() + " is filled.\n");
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }

    public void usingDevice() {
        if (isEmpty()) {
            setCurrentState(new BrokenState(this));
            Observer.getInstance().logAction("Coffee beans in " + this.getName() + " are over!\n");
            generateReportForObserver();
        } else {
            currentCapacity--;
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            Observer.getInstance().logAction(this.getName() + " is starting at " + getHouse().getTime() + ", " + currentCapacity + " portions are/is left.\n");
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
