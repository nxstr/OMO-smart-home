package strategy;

import house.Floor;
import items.device.Device;
import items.device.DeviceType;
import items.state.ActiveState;
import items.state.IdleState;
import items.state.ObjectState;
import items.state.StateType;
import livingEntities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Morning implements Strategy{
    List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;

    public Morning() {
        System.out.println("Its morning strategy");
        try {
            Device device = deviceFactory.findDeviceByType(DeviceType.COFFEE_MACHINE);
            device.usingDevice();
            activatedDevices.add(device);
            device.setCurrentState(new IdleState(device));
            device = deviceFactory.findDeviceByType(DeviceType.TV);
            device.usingDevice();
            activatedDevices.add(device);
//            device.setCurrentState(new IdleState(device));
            //ma vypnout clovek
            device = deviceFactory.findDeviceByType(DeviceType.PET_FEEDER);
            device.usingDevice();
            activatedDevices.add(device);
            device.setCurrentState(new IdleState(device));
            device = deviceFactory.findDeviceByType(DeviceType.AIR_CONDITIONER);
            device.usingDevice();
            activatedDevices.add(device);
//            device.setCurrentState(new IdleState(device));
            //ma vypnout clovek
        }catch (Exception e){
            System.out.println("This device doesnot exist in the house");
        }
    }

    public List<Device> getActiveDevices(){
        return activatedDevices.stream().filter(d->d.getCurrentState().getType()==StateType.ACTIVE).toList();
    }


    @Override
    public void findActivity(LivingEntity entity) {

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

    @Override
    public void addActiveDevice(Device device) {
        activatedDevices.add(device);
    }
}
