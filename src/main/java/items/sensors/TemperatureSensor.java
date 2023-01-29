package items.sensors;

import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;
import strategy.Strategy;

import java.util.List;
import java.util.Objects;

public class TemperatureSensor extends Sensor {

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 1;

    public TemperatureSensor(Room currentRoom) {
        super(SensorType.TEMPERATURE, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice() {
        try {
            if(this.getCurrentState().getType()== StateType.ACTIVE) {
                List<ElectricalItem> items = getCurrentRoom().getElectricalItems().stream()
                        .filter(d -> Objects.equals(d.getMainType(), "device")).toList();

                ElectricalItem c = items.stream().filter(i -> Objects.equals(i.getName(), DeviceType.AIR_CONDITIONER.toString()))
                        .filter(i->i.getCurrentState().getType() == StateType.IDLE).filter(i->i.getCurrentRoom()==this.getCurrentRoom())
                        .findAny().get();
                c.usingDevice();
                Strategy strategy = observer.getStrategy();
                if(strategy!=null && c.getCurrentState().getType()==StateType.ACTIVE){
                    strategy.addActiveDevice((Device) c);
                }
                generateReportForObserver();
                breakingEvent();
            }
        } catch (Exception e) {
            Observer.getInstance().logAction("There is no available air conditioner in the room\n");
        }

    }
}
