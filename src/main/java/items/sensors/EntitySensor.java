package items.sensors;

import house.Room;
import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class EntitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 1;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public EntitySensor(Room currentRoom) {
        super(SensorType.ENTITY, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            List<Device> locks = getDeviceFactory().getDevices().stream().filter(i -> Objects.equals(i.getName(), DeviceType.LOCK.toString())).toList();
            for(Device c:locks) {
                if (getHouse().getOutSideArea().getEntities().isEmpty() && c.getCurrentState().getType()==StateType.IDLE) {
                    logger.info("Activating lock at " + getHouse().getTime());
                    c.usingDevice();
                }else if (!getHouse().getOutSideArea().getEntities().isEmpty() && c.getCurrentState().getType()==StateType.ACTIVE){
                    logger.info("Stopping lock at " + getHouse().getTime());
                    c.stopDevice();
                }
            }
            generateReportForObserver();
            breakingEvent();
        }
    }
}
