package items.sensors;

import house.Room;
import items.Observer;
import items.device.DeviceType;
import items.state.StateType;

public class ElectricitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 2;
    private final SensorFactory sensorFactory = SensorFactory.getInstance();

    public ElectricitySensor(Room currentRoom) {
        super(SensorType.ELECTRICITY, currentRoom, electricityInOnState, electricityInBrokeState);
    }

    public void usingDevice(){
        if(this.getCurrentState().getType()!= StateType.BROKEN) {
            if (isIsEnergyOn()) {
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() != StateType.FIXING)
                        .filter(d->d.getType()!= DeviceType.FIRE_SUPPRESSION)
                        .forEach(d -> d.setIsEnergyOn(false));
                sensorFactory.getSensors().stream()
                                .filter(i->i.getCurrentState().getType()!=StateType.FIXING)
                                        .forEach(d->d.setIsEnergyOn(false));
                Observer.getInstance().logAction("There is no electricity in the house, all devices and sensors are off\n");
            } else {
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.OFF)
                        .forEach(d -> d.setIsEnergyOn(true));
                sensorFactory.getSensors().stream()
                        .filter(i->i.getCurrentState().getType()==StateType.OFF)
                        .forEach(d->d.setIsEnergyOn(true));
                Observer.getInstance().logAction("Electricity is on! All devices and sensors are available!\n");
            }
            Observer.getInstance().logAction(this.getType() + " sensor got event\n");
            generateReportForObserver();
        }
    }
}
