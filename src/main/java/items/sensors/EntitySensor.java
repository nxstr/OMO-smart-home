package items.sensors;

import house.Room;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.List;
import java.util.Objects;

public class EntitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;

    public EntitySensor(Room currentRoom) {
        super(SensorType.ENTITY, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            List<Device> locks = getDeviceFactory().getDevices().stream().filter(i -> Objects.equals(i.getName(), DeviceType.LOCK.toString())).toList();
            for(Device c:locks) {
                if (getHouse().getOutSideArea().getEntities().isEmpty() && c.getCurrentState().getType()==StateType.IDLE) {
                    System.out.println("Activating lock at " + getHouse().getTime());
                    c.usingDevice();
                }else if (!getHouse().getOutSideArea().getEntities().isEmpty() && c.getCurrentState().getType()==StateType.ACTIVE){
                    System.out.println("Stopping lock at " + getHouse().getTime());
                    c.stopDevice();
                }
            }
            breakingEvent();
        }
    }
}
