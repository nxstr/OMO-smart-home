package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.IdleState;
import items.state.ActiveState;

public class PetFountain extends Device{

    private static final int usingHours = 1; //using ticks = 10 min
    private static final int electricityInOnState = 2;
    private static final int electricityInOffState = 1;

    public PetFountain(Room currentRoom) {
        super(DeviceType.PET_FOUNTAIN, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isDirty(){
        if(getUsedTimes()>=30){
            setCurrentState(new IdleState(this));
            setBrokenTimes(getBrokenTimes()+1);
            Observer.getInstance().logAction("PET_FOUNTAIN needs filter change!\n");
            return true;
        }
        return false;
    }

    public void changeFilter(){
        setUsedTimes(0);
        if(!isDirty()){
            setCurrentState(new IdleState(this));
        }
        Observer.getInstance().logAction("PET_FOUNTAIN has new filter!\n");
    }

    public void usingDevice(){
        if(!isDirty()) {
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            Observer.getInstance().logAction(this.getName() + " is starting at " + getHouse().getTime()+"\n");
            breakingEvent();
        }else{
            setCurrentState(new BrokenState(this));
            Observer.getInstance().logAction("PET_FOUNTAIN is dirty!\n");
            generateReportForObserver();
        }
    }

    public void fixingItem() {
        if(isDirty()){
            changeFilter();
        }else{
            super.fixingItem();
        }
    }
}
