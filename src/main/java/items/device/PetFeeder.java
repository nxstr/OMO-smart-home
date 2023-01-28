package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.ActiveState;
import items.state.IdleState;

public class PetFeeder extends Device{

    private static final int usingHours = 1;
    private static final int electricityInOnState = 5;
    private static final int electricityInOffState = 2;
    private static final int maxCapacity = 10;
    private int currentCapacity = maxCapacity;
    private boolean isPortionEated = true;


    public PetFeeder(Room currentRoom) {
        super(DeviceType.PET_FEEDER, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isEmpty(){
        return currentCapacity==0;
    }

    public void refill(){
        Observer.getInstance().logAction("PET_FEEDER is filled\n");
        currentCapacity = maxCapacity;
        setCurrentState(new IdleState(this));
    }

    public boolean isPortionEated() {
        return isPortionEated;
    }

    public void setPortionEated(boolean portionEated) {
        isPortionEated = portionEated;
    }

    public void usingDevice(){
        if(isPortionEated) {
            if (isEmpty()) {
                setCurrentState(new BrokenState(this));
                Observer.getInstance().logAction("PER_FEEDER is empty!\n");
                generateReportForObserver();
            } else {
                currentCapacity--;
                setPortionEated(false);
                setUsedTimes(getUsedTimes() + 1);
                setCurrentState(new ActiveState(this));
                Observer.getInstance().logAction("PET_FEEDER pours one portion of food at " + getHouse().getTime() + ", " + + currentCapacity + " portions are/is left\n");
                breakingEvent();
            }
        }else{
            Observer.getInstance().logAction("PET_FEEDER is full\n");
        }
    }

    public void usingDeviceByPet(){
        if(!isPortionEated){
            setCurrentState(new ActiveState(this));
            setUsedTimes(getUsedTimes() + 1);
            setPortionEated(true);
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
