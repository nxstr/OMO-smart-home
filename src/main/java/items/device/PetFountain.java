package items.device;

import house.Room;
import items.Observer;
import items.state.BrokenState;
import items.state.IdleState;
import items.state.ActiveState;

import java.util.logging.Logger;

public class PetFountain extends Device{

    private static final int usingHours = 1; //using ticks = 10 min
    private static final int electricityInOnState = 2;
    private static final int electricityInOffState = 1;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public PetFountain(Room currentRoom) {
        super(DeviceType.PET_FOUNTAIN, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isDirty(){
        if(getUsedTimes()>=30){
            setCurrentState(new IdleState(this));
            setBrokenTimes(getBrokenTimes()+1);
            logger.info("PET_FOUNTAIN needs filter change at " + getHouse().getTime());
            return true;
        }
        return false;
    }

    public void changeFilter(){
        setUsedTimes(0);
        if(!isDirty()){
            setCurrentState(new IdleState(this));
        }
        logger.info("PET_FOUNTAIN has new filter at " + getHouse().getTime());
    }

    public void usingDevice(){
        if(!isDirty()) {
            setUsedTimes(getUsedTimes() + 1);
            setCurrentState(new ActiveState(this));
            logger.info(this.getName() + " is starting at " + getHouse().getTime());
            breakingEvent();
        }else{
            setCurrentState(new BrokenState(this));
            logger.info("PET_FOUNTAIN is dirty at " + getHouse().getTime());
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
