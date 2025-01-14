package items.sensors;

import house.Room;
import items.ElectricalItem;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FireSensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 1;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public FireSensor(Room currentRoom) {
        super(SensorType.FIRE, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        setAlarmMode(true);
        logger.info(this.getType() + " sensor got fire event, alarm is activated at "+ getHouse().getTime());
        generateReportForObserver();
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            List<ElectricalItem> items = getCurrentRoom().getElectricalItems().stream()
                    .filter(d -> Objects.equals(d.getName(), DeviceType.FIRE_SUPPRESSION.name())).filter(i->i.getCurrentState().getType() == StateType.IDLE).toList();
            for(ElectricalItem c: items) {
                c.usingDevice();
                Strategy strategy = observer.getStrategy();
                if (strategy != null && c.getCurrentState().getType() == StateType.ACTIVE) {
                    strategy.stopBackAction();
                    strategy.setActivatedDevices(new ArrayList<>());
                    strategy.addActiveDevice((Device) c);
                }
            }
            breakingEvent();
        }
    }
}
