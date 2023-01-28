package items.state;

import items.ElectricalItem;

public class OffState implements ObjectState{
    ElectricalItem device;

    final private StateType type;

    public OffState(ElectricalItem device) {
        this.device = device;
        this.type = StateType.OFF;
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
        device.addUsedElectricity(0);
    }
}
