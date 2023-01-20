package items.device;

import items.ElectricalItem;

public class Manual {

    private final ElectricalItem item;

    public Manual(ElectricalItem item) {
        this.item = item;
    }


    public void readDeviceManual() {
        System.out.println("Reading the manual of" + item.getName());
    }
}
