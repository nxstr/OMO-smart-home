package items.sensors;

import house.Room;
import items.Observer;
import items.device.DeviceType;
import items.state.StateType;

public class WaterSensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;
    private boolean isWaterOn = true;

    /*
    Means sensor, that makes reaction when water cuts off in house
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
                Observer.getInstance().logAction("There is no water in the house, all water-using devices are off\n");
            } else {
                isWaterOn = true;
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.NON_ENERGY)
                        .filter(d -> d.getType() == DeviceType.DISHWASHER || d.getType() == DeviceType.WASHING_MACHINE)
                        .forEach(d->d.setIsWaterOn(true));
                Observer.getInstance().logAction("Water is on! All water-using devices are available!\n");
            }
            Observer.getInstance().logAction(this.getType() + " sensor got event\n");
            generateReportForObserver();
        }
    }
}
