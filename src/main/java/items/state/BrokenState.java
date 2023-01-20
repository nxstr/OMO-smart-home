package items.state;

import items.ElectricalItem;
import items.device.Device;

public class BrokenState implements ObjectState{

    ElectricalItem device;
    final private StateType type;


    public BrokenState(ElectricalItem device) {
        this.device = device;
        this.type = StateType.BROKEN;
    }

    @Override
    public StateType getType() {
        return type;
    }

    @Override
    public int getUsingHours() {
        return 0;
    }

    @Override
    public void getElectricity() {
        device.addUsedElectricity(device.getElectricityInBrokeState());
    }
}
