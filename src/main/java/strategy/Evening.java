package strategy;

import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.ArrayList;
import java.util.List;

public class Evening implements Strategy {
    private List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;
    public Evening() {


    }

    public void setActivatedDevices(List<Device> activatedDevices) {
        this.activatedDevices = activatedDevices;
    }

    @Override
    public void setup(){
        System.out.println("Its evening strategy");

        String[] arr = new String[]{
                "pet_feeder"
        };
        for(String s:arr){
            for(Device d:deviceFactory.getSystemDevices()) {

                if(d.getType()==DeviceType.getTypeByName(s) && d.getCurrentState().getType() == StateType.IDLE) {
                    try {
                        d.usingDevice();
                        if(d.getCurrentState().getType() == StateType.ACTIVE) {
                            activatedDevices.add(d);
                        }
                    } catch (Exception e) {
                        System.out.println("This device does not exist in the house");
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
