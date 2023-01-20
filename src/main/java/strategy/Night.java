package strategy;

import items.device.Device;
import items.state.IdleState;
import livingEntities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Night implements Strategy{

    private List<Device> activeDevicec = new ArrayList<>();
    private int currentBackActionProgress = 0;

    public Night() {
        System.out.println("Its night strategy");
        for(Device d: deviceFactory.getDevices()){
            d.setCurrentState(new IdleState(d));
            d.generateReportForDay();
        }
    }

    @Override
    public void findActivity(LivingEntity entity) {

    }

    @Override
    public List<Device> getActiveDevices() {
        return activeDevicec;
    }

    public int getCurrentBackActionProgress(){
        return currentBackActionProgress;
    }
    public void increaseCBAP(){
        currentBackActionProgress++;
    }

    public void stopBackAction() {
        this.currentBackActionProgress = 0;
//        System.out.println("Person switched off the devices");
    }

    public void addActiveDevice(Device device){
        activeDevicec.add(device);
    }
}
