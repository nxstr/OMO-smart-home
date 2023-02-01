package items.device;

import items.ElectricalItem;
import items.Observer;

import java.util.logging.Logger;

public class Manual {

    private final ElectricalItem item;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Manual(ElectricalItem item) {
        this.item = item;
    }


    public void readDeviceManual() {
        logger.info("Reading the manual of " + item.getName());
    }
}
