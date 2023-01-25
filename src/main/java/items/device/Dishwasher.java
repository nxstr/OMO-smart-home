package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;
import items.state.ObjectState;
import items.state.StateType;

import java.time.LocalTime;

public class Dishwasher extends Device{

    private static int usingHours = 5;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;
    private final int maxCapacity = 6;
    private int currentCapacity = maxCapacity;
    private boolean isClean = false;

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
                System.out.println("Dishwasher is started at " + getHouse().getTime());
            }else{
                System.out.println("Dishwasher is needs to empty");
                generateReportForObserver();
            }
        }else{
            System.out.println("Dishwasher is empty");
            generateReportForObserver();
        }

    }

}
