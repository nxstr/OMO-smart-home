package items.sensors;

import house.Room;
import items.Observer;
import items.device.DeviceType;
import items.state.StateType;

import java.util.logging.Logger;

public class WaterSensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 1;
    private boolean isWaterOn = true;
    private static final Logger logger = Logger.getLogger("Smarthome");

    /*
    sensor, that makes reaction when water cuts off in house
     */

    public boolean isWaterOn() {
        return isWaterOn;
    }

    public WaterSensor(Room currentRoom) {
        super(SensorType.WATER, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            if (isWaterOn) {
                isWaterOn = false;
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.IDLE)
                        .filter(d -> d.getType() == DeviceType.DISHWASHER || d.getType() == DeviceType.WASHING_MACHINE)
                        .forEach(d->d.setIsWaterOn(false));
                logger.info("There is no water in the house, all water-using devices are off at " + getHouse().getTime());
            } else {
                isWaterOn = true;
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.NON_ENERGY)
                        .filter(d -> d.getType() == DeviceType.DISHWASHER || d.getType() == DeviceType.WASHING_MACHINE)
                        .forEach(d->d.setIsWaterOn(true));
                logger.info("Water is on! All water-using devices are available at " + getHouse().getTime());
            }
            logger.info(this.getType() + " sensor got event at " + getHouse().getTime());
            generateReportForObserver();
        }
    }
}
