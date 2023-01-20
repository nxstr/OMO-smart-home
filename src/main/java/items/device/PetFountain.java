package items.device;

import house.Room;
import items.state.IdleState;
import items.state.ActiveState;

public class PetFountain extends Device{

    private static final int usingHours = 1; //using ticks = 10 min
    private static final int electricityInOnState = 2;
    private static final int electricityInOffState = 1;

    public PetFountain(Room currentRoom) {
        super(DeviceType.PET_FOUNTAIN, false, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    public boolean isDirty(){
        if(getUsedTimes()>=30){
            setCurrentState(new IdleState(this));
            setBrokenTimes(getBrokenTimes()+1);
            System.out.println("Pet Fountain needs filter change!");
            return true;
        }
        return false;
    }

    public void changeFilter(){
        setUsedTimes(0);
        if(!isDirty()){
            setCurrentState(new IdleState(this));
        }
        System.out.println("Pet Fountain has new filter!");
    }
}
