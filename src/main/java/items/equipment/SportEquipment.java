package items.equipment;

import house.House;
import house.Room;
import items.Observer;
import items.state.ObjectState;
import items.state.IdleState;
import items.state.ActiveState;
import items.state.StateType;

import java.util.logging.Logger;

public abstract class SportEquipment {
    private final SportEquipmentType type;

    private ObjectState currentState = new IdleState(this);

    private final int usingHours;
    private final Room currentRoom;
    private int usedTimes =0;

    private static final Logger logger = Logger.getLogger("Smarthome");

    protected SportEquipment(SportEquipmentType type, int usingHours, Room currentRoom) {
        this.type = type;
        this.usingHours = usingHours;
        this.currentRoom = currentRoom;
    }

    public ObjectState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ObjectState currentState) {
        this.currentState = currentState;
    }

    public SportEquipmentType getType() {
        return type;
    }

    public int getUsingHours() {
        return usingHours;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void usingEquipment(){
        usedTimes++;
        setCurrentState(new ActiveState(this));
    }

    public void stopEquipment(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            logger.info(this.getType() + " set to idle at " + House.getInstance().getTime());
            this.setCurrentState(new IdleState(this));
        }
    }

    public void generateReportForDay(){
        Observer.getInstance().handleSportDayReport(this);
    }
}
