package items.equipment;

import house.House;
import house.Room;
import livingEntities.Person;
import items.state.ObjectState;
import items.state.IdleState;
import items.state.ActiveState;

public abstract class SportEquipment {
    private final SportEquipmentType type;

    private ObjectState currentState = new IdleState(this);

    private final int usingHours;
    private final Room currentRoom;
    private int usedTimes =0;
    House house = House.getInstance();

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

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    public void usingEquipment(){
        usedTimes++;
        setCurrentState(new ActiveState(this));
    }
}
