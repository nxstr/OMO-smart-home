package strategy;

import house.House;
import items.Observer;
import items.device.Device;
import items.equipment.SportEquipment;
import items.equipment.SportEquipmentFactory;
import items.state.IdleState;
import items.state.StateType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Night implements Strategy{

    private List<Device> activeDevicec = new ArrayList<>();
    private int currentBackActionProgress = 0;
    private static final Logger logger = Logger.getLogger("Smarthome");
    public Night() {
    }

    public void setActivatedDevices(List<Device> activatedDevices) {
        this.activeDevicec = activatedDevices;
    }

    @Override
    public void setup(){
        logger.info("Its night strategy");
        for(Device d: deviceFactory.getDevices()){
            if(d.getCurrentState().getType()== StateType.ACTIVE) {
                d.setCurrentState(new IdleState(d));
                logger.info("device " + d.getType() + " has been stopped by system at " + d.getHouse().getTime());
            }
            if(House.getInstance().getDay()!=0) {
                d.generateReportForDay();
            }
        }
        if(House.getInstance().getDay()!=0) {
            for (SportEquipment e : SportEquipmentFactory.getInstance().getEquipments()) {
                e.generateReportForDay();
            }
        }
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
    }

    public void addActiveDevice(Device device){
        activeDevicec.add(device);
    }

    @Override
    public void removeActiveDevice(Device device) {

    }
}
