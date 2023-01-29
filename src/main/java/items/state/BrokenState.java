package items.state;

import items.ElectricalItem;

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

}
