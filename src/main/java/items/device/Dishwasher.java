package items.device;

import house.Room;
import items.Observer;
import items.state.ActiveState;

import java.util.logging.Logger;

public class Dishwasher extends Device{

    private static final int usingHours = 5;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;
    private final int maxCapacity = 6;
    private int currentCapacity = maxCapacity;
    private boolean isClean = false;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Dishwasher(Room currentRoom) {
        super(DeviceType.DISHWASHER, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty(){
        return currentCapacity==0;
    }

    public void fill(){
        currentCapacity = maxCapacity;
        isClean = false;
    }

    public void emptyDevice() {
        currentCapacity = 0;
    }

    public boolean isClean() {
        return isClean;
    }



    public void usingDevice(){
        if(!isEmpty()) {
            if(!isClean) {
                breakingEvent();
                setCurrentState(new ActiveState(this));
                setUsedTimes(getUsedTimes() + 1);
                isClean = true;
                logger.info(this.getName() +" is started at " + getHouse().getTime());
            }else{
                logger.info(this.getName() +" is needs to empty at " + getHouse().getTime());
                generateReportForObserver();
            }
        }else{
            logger.info(this.getName() +" is empty at " + getHouse().getTime());
            generateReportForObserver();
        }

    }

}
