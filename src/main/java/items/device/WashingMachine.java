package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;

import java.time.LocalTime;

public class WashingMachine extends Device{

    private static final int usingHours = 8;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;
    private final int maxCapacity = 6;
    private int currentCapacity = maxCapacity;
    private boolean isClean = false;


    public WashingMachine(Room currentRoom) {
        super(DeviceType.WASHING_MACHINE, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
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

    public void usingDevice(LocalTime time){
        if(!isEmpty()) {
            if(!isClean) {
                setCurrentState(new ActiveState(this));
                setUsedTimes(getUsedTimes() + 1);
                System.out.println("Washing machine is started at " + time);
                isClean = true;
            }else{
                System.out.println("Washing machine is needs to empty");
                generateReportForObserver();
            }
        }else{
            //add task
            System.out.println("Washing machine is empty");
            generateReportForObserver();
        }

    }
}
