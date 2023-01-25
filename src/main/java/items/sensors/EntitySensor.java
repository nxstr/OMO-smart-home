package items.sensors;

import house.House;
import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.IdleState;
import items.state.StateType;
import strategy.Strategy;

import java.util.List;

public class EntitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;

    public EntitySensor(Room currentRoom) {
        super(SensorType.ENTITY, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            List<ElectricalItem> items = getCurrentRoom().getElectricalItems().stream()
                    .filter(d -> d.getMainType() == "device").toList();
            List<ElectricalItem> locks = items.stream().filter(i -> i.getName() == DeviceType.LOCK.toString()).toList();
            for(ElectricalItem c:locks) {
                if (getHouse().getOutSideArea().getEntities().isEmpty()) {
                    System.out.println("Activating lock at " + getHouse().getTime());
                    c.usingDevice();
                }else{
                    System.out.println("Stopping lock at " + getHouse().getTime());
                    c.setCurrentState(new IdleState((Device) c));
                }
            }
            breakingEvent();
        }
    }
}
