package items.sensors;

import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.AirConditioner;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;
import strategy.Strategy;

import java.util.List;

public class TemperatureSensor extends Sensor {

    private static final int usingHours = 24;
    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;

    public TemperatureSensor(Room currentRoom) {
        super(SensorType.TEMPERATURE, usingHours, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice() {
        try {
            if(this.getCurrentState().getType()== StateType.ACTIVE) {
                List<ElectricalItem> items = getCurrentRoom().getElectricalItems().stream()
                        .filter(d -> d.getMainType() == "device").toList();

                ElectricalItem c = items.stream().filter(i -> i.getName() == DeviceType.AIR_CONDITIONER.toString()).filter(i->i.getCurrentState().getType() == StateType.IDLE).findAny().get();
                c.usingDevice();
                Strategy strategy = Observer.getInstance().getStrategy();
                if(strategy!=null){
                    strategy.addActiveDevice((Device) c);
                }
            }else{
                throw new Exception("this sensor is not working");
            }
        } catch (Exception e) {
            setAlarmMode(true);
            System.out.println("There is no available air conditioner in the room");
            generateReportForObserver();
        }

    }
}