package strategy;

import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Evening implements Strategy {
    private List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;
    private static final Logger logger = Logger.getLogger("Smarthome");
    public Evening() {


    }

    public void setActivatedDevices(List<Device> activatedDevices) {
        this.activatedDevices = activatedDevices;
    }

    @Override
    public void setup(){
        logger.info("Its evening strategy");
        String[] arr = new String[]{
                "pet_feeder"
        };
        for(String s:arr){
            for(Device d:deviceFactory.getSystemDevices()) {

                if(d.getType()==DeviceType.getTypeByName(s) && d.getCurrentState().getType() == StateType.IDLE) {
                    try {
                        d.usingDevice();
                        logger.info("device " + d.getType() + " is activated by system at " + d.getHouse().getTime());
                        if(d.getCurrentState().getType() == StateType.ACTIVE) {
                            activatedDevices.add(d);
                        }
                    } catch (Exception e) {
                        logger.warning("device with name " + s + " is not exist in the house ");
                    }
                }

            }
        }
    }

    @Override
    public List<Device> getActiveDevices() {
        return activatedDevices;
    }

    @Override
    public int getCurrentBackActionProgress() {
        return currentBackActionProgress;
    }

    @Override
    public void increaseCBAP() {
        currentBackActionProgress++;
    }

    @Override
    public void stopBackAction() {
        currentBackActionProgress = 0;
    }

    @Override
    public void addActiveDevice(Device device) {
        activatedDevices.add(device);
    }

    @Override
    public void removeActiveDevice(Device device) {

    }
}
