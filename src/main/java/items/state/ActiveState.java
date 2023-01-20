package items.state;

import items.ElectricalItem;
import items.equipment.SportEquipment;

public class ActiveState implements ObjectState{

    ElectricalItem device;
    SportEquipment equipment;
    final private StateType type;

    public ActiveState(SportEquipment equipment) {
        this.equipment = equipment;
        this.type = StateType.ACTIVE;
    }

    public ActiveState(ElectricalItem device) {
        this.device = device;
        this.type = StateType.ACTIVE;
    }

    @Override
    public StateType getType() {
        return type;
    }

    @Override
    public int getUsingHours() {
        return device.getUsingHours();
    }

    @Override
    public void getElectricity() {
        device.addUsedElectricity(device.getElectricityInOnState());
    }
}
