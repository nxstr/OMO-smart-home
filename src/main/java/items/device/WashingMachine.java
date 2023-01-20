package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;

public class WashingMachine extends Device{

    private static final int usingHours = 2;
    private static final int electricityInOnState = 10;
    private static final int electricityInOffState = 2;
    private final int maxCapacity = 6;
    private int currentCapacity = 0;


    public WashingMachine(Room currentRoom) {
        super(DeviceType.WASHING_MACHINE, true, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty(){
        return currentCapacity==0;
    }

    public void fill(){
        currentCapacity = maxCapacity;
    }

    public void usingDevice(){
        if(!isEmpty()) {
            setCurrentState(new ActiveState(this));
            setUsedTimes(getUsedTimes() + 1);
            System.out.println("Washing machine is started");
        }else{
            setCurrentState(new IdleState(this));
            //add task
            System.out.println("Washing machine is empty");
        }
    }
}
