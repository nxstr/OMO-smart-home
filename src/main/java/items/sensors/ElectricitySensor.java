package items.sensors;

import house.Room;
import items.Observer;
import items.device.DeviceType;
import items.state.StateType;

import java.util.logging.Logger;

public class ElectricitySensor extends Sensor{

    private static final int electricityInOnState = 1; //per one tick (10 minutes)
    private static final int electricityInBrokeState = 1;
    private final SensorFactory sensorFactory = SensorFactory.getInstance();
    private static final Logger logger = Logger.getLogger("Smarthome");

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
                logger.info("There is no electricity in the house, all devices and sensors are off at " + getHouse().getTime());
            } else {
                getDeviceFactory().getDevices().stream()
                        .filter(i -> i.getCurrentState().getType() == StateType.NON_ENERGY)
                        .forEach(d -> d.setIsEnergyOn(true));
                sensorFactory.getSensors().stream()
                        .filter(i->i.getCurrentState().getType()==StateType.NON_ENERGY)
                        .forEach(d->d.setIsEnergyOn(true));
                logger.info("Electricity is on! All devices and sensors are available at " + getHouse().getTime());
            }
            logger.info(this.getType() + " sensor got event at " + getHouse().getTime());
            generateReportForObserver();
        }
    }
}
