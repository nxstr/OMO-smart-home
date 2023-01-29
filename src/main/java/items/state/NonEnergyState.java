package items.state;

import items.ElectricalItem;

public class NonEnergyState implements ObjectState{
    ElectricalItem device;

    final private StateType type;

    public NonEnergyState(ElectricalItem device) {
        this.device = device;
        this.type = StateType.NON_ENERGY;
    }

    @Override
    public StateType getType() {
        return type;
    }

}
