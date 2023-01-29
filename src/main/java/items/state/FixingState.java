package items.state;

import items.ElectricalItem;

public class FixingState implements ObjectState{

    ElectricalItem device;
    final private StateType type;

    public FixingState(ElectricalItem device) {
        this.device = device;
        this.type = StateType.FIXING;
    }

    @Override
    public StateType getType() {
        return type;
    }

}
