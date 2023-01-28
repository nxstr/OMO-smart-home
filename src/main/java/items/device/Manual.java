package items.device;

import items.ElectricalItem;
import items.Observer;

public class Manual {

    private final ElectricalItem item;

    public Manual(ElectricalItem item) {
        this.item = item;
    }


    public void readDeviceManual() {
        Observer.getInstance().logAction("Reading the manual of " + item.getName()+"\n");
    }
}
