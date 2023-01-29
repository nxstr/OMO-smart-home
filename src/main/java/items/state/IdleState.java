package items.state;

import items.device.Device;
import items.equipment.SportEquipment;

public class IdleState implements ObjectState{

    private final static int offTicks = 0;

    Device device;
    SportEquipment equipment;
    final private StateType type;

    public IdleState(Device device) {
        this.device = device;
        this.type = StateType.IDLE;
    }

    public IdleState(SportEquipment equipment) {
        this.equipment = equipment;
        this.type = StateType.IDLE;
    }

    @Override
    public StateType getType() {
        return type;
    }


}
