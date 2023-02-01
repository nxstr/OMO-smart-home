package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.ActiveState;
import items.state.IdleState;

import java.util.logging.Logger;

public class CoffeeMachine extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 1;
    private static final int maxCapacity = 10;
    private int currentCapacity = maxCapacity;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public CoffeeMachine(Room currentRoom) {
        super(DeviceType.COFFEE_MACHINE, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public void refill() {
        logger.info(this.getName() + " is filled at " + getHouse().getTime());
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }

    public void usingDevice() {
        if (isEmpty()) {
            setCurrentState(new BrokenState(this));
            logger.info("Coffee beans in " + this.getName() + " are over at " + getHouse().getTime());
            generateReportForObserver();
        } else {
            currentCapacity--;
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            logger.info("this.getName() + \" is starting at \" + getHouse().getTime() + \", \" + currentCapacity + \" portions are/is left at " + getHouse().getTime());
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
