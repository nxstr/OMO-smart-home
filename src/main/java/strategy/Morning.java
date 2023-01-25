package strategy;

import house.Floor;
import items.device.Device;
import items.device.DeviceType;
import items.state.ActiveState;
import items.state.IdleState;
import items.state.ObjectState;
import items.state.StateType;
import livingEntities.LivingEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Morning implements Strategy{
    List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;
    private LocalTime time = LocalTime.of(8, 0);

    public Morning() {

    }

    public List<Device> getActiveDevices(){
        return activatedDevices;
    }


    @Override
    public void setup(){
        System.out.println("Its morning strategy");

        String[] arr = new String[]{
                "coffee_machine", "pet_feeder", "air_conditioner"
        };
        for(String s:arr){
            for(Device d:deviceFactory.getDevices()) {

                if(d.getType()==DeviceType.getTypeByName(s) && d.getCurrentState().getType() == StateType.IDLE) {
                    try {
                        d.usingDevice();
                        if(d.getCurrentState().getType() == StateType.ACTIVE) {
                            activatedDevices.add(d);
                        }
                    } catch (Exception e) {
                        System.out.println("This device doesnot exist in the house");
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
