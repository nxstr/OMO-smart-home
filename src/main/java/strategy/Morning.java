package strategy;

import house.Floor;
import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.ArrayList;
import java.util.List;

public class Morning implements Strategy{
    private List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;

    public Morning() {

    }

    public List<Device> getActiveDevices(){
        return activatedDevices;
    }

    public void setActivatedDevices(List<Device> activatedDevices) {
        this.activatedDevices = activatedDevices;
    }

    @Override
    public void setup(){
        Observer.getInstance().logAction("Its morning strategy\n");

        String[] arr = new String[]{
                "coffee_machine", "pet_feeder", "air_conditioner"
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
                        Observer.getInstance().logAction("This device does not exist in the house\n");
                    }
                }

            }
        }
    }
    public int getCurrentBackActionProgress(){
        return currentBackActionProgress;
    }
    public void increaseCBAP(){
        currentBackActionProgress++;
    }

    public void stopBackAction() {
        this.currentBackActionProgress = 0;
    }

    @Override
    public void addActiveDevice(Device device) {
        activatedDevices.add(device);
    }

    @Override
    public void removeActiveDevice(Device device) {
        activatedDevices.remove(device);
    }
}
