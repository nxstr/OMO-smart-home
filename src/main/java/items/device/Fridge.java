package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.IdleState;
import items.state.ActiveState;

import java.util.logging.Logger;

public class Fridge extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 7;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 20;
    private int currentCapacity = maxCapacity;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Fridge(Room currentRoom) {
        super(DeviceType.FRIDGE, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        logger.info(this.getName()+" is refilled at " +getHouse().getTime());
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }


    public void usingDevice() {
        if (isEmpty()) {
            setCurrentState(new BrokenState(this));
            logger.info("Food in FRIDGE is needs to refill at " +getHouse().getTime());
            generateReportForObserver();
        } else {
            setCurrentState(new ActiveState(this));
            setUsedTimes(getUsedTimes() + 1);
            currentCapacity--;
            logger.info("Fridge is using at " + getHouse().getTime() + ", " + currentCapacity + " portions of food are/is left at " +getHouse().getTime());
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
