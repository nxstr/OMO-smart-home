package items.sensors;

import house.Room;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.state.ActiveState;
import items.state.FixingState;
import items.state.IdleState;
import items.state.StateType;

public class ElectricitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;
    private boolean isEnergyOn = true;
    private DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private SensorFactory sensorFactory = SensorFactory.getInstance();

    public ElectricitySensor(Room currentRoom) {
        super(SensorType.ELECTRICITY, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()!= StateType.BROKEN) {
            if (isEnergyOn) {
                isEnergyOn = false;
                deviceFactory.getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() != StateType.FIXING)
                        .forEach(d -> d.setCurrentState(new FixingState(d)));
                sensorFactory.getSensors().stream()
                                .filter(i->i.getCurrentState().getType()!=StateType.FIXING)
                                        .forEach(d->d.setCurrentState(new FixingState(d)));
                System.out.println("There is no electricity in the house, all devices ans sensors are off");
            } else {
                isEnergyOn = true;
                deviceFactory.getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.FIXING)
                        .forEach(d -> d.setCurrentState(new IdleState(d)));
                sensorFactory.getSensors().stream()
                        .filter(i->i.getCurrentState().getType()==StateType.FIXING)
                        .forEach(d->d.setCurrentState(new ActiveState(d)));
                System.out.println("Electricity is on! All devices and sensors are available!");
            }
            System.out.println(this.getType() + " sensor got event");
            generateReportForObserver();
        }
    }
}
