package items.device;

import house.Room;
import items.state.BrokenState;
import items.state.ActiveState;

import java.time.LocalTime;

public class PetFeeder extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 2;
    private static final int maxCapacity = 10;
    private int currentCapacity = maxCapacity;


    public PetFeeder(Room currentRoom) {
        super(DeviceType.PET_FEEDER, false, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty(){
        return currentCapacity==0;
    }

    public void refill(){
        currentCapacity = maxCapacity;
    }

    public void usingDevice(LocalTime time){
        if(isEmpty()){
            setCurrentState(new BrokenState(this));
            System.out.println("Pet Feeder is empty!");
            generateReportForObserver();
        }else{
            currentCapacity--;
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            System.out.println("Pet Feeder pours one portion of food at " + time + ", " + currentCapacity + " portions are/is left");
        }
    }
}
