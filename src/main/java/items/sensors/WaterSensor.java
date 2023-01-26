package items.sensors;

import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.state.FixingState;
import items.state.IdleState;
import items.state.StateType;

public class WaterSensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;
    private boolean isWaterOn = true;
    private DeviceFactory deviceFactory = DeviceFactory.getInstance();

    /*
    Means sensor, that makes reaction when water cuts off in house
     */

    public WaterSensor(Room currentRoom) {
        super(SensorType.WATER, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            if (isWaterOn) {
                isWaterOn = false;
                deviceFactory.getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.IDLE)
                        .filter(d -> d.getType() == DeviceType.DISHWASHER || d.getType() == DeviceType.WASHING_MACHINE || d.getType() == DeviceType.FIRE_SUPPRESSION)
                        .forEach(d -> d.setCurrentState(new FixingState(d)));
                System.out.println("There is no water in the house, all water-using devices are off");
            } else {
                isWaterOn = true;
                deviceFactory.getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.FIXING)
                        .filter(d -> d.getType() == DeviceType.DISHWASHER || d.getType() == DeviceType.WASHING_MACHINE || d.getType() == DeviceType.FIRE_SUPPRESSION)
                        .forEach(d -> d.setCurrentState(new IdleState(d)));
                System.out.println("Water is on! All water-using devices are available!");
            }
            System.out.println(this.getType() + " sensor got event");
            generateReportForObserver();
        }
    }
}
