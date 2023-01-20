package strategy;

import items.device.Device;
import items.device.DeviceType;
import livingEntities.LivingEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Evening implements Strategy {
    List <Device> activatedDevices = new ArrayList<>();
    private int currentBackActionProgress = 0;
    private LocalTime time = LocalTime.of(19, 0);
    public Evening() {
        System.out.println("Its afternoon strategy");

        String[] arr = new String[]{
                "pet_feeder"
        };
        for(String s:arr){
            try {
                Device device = deviceFactory.findDeviceByType(DeviceType.getTypeByName(s));
                device.usingDevice(time);
                activatedDevices.add(device);
            }catch (Exception e){
                System.out.println("This device does not exist in the house");
            }
        }

    }

    @Override
    public void findActivity(LivingEntity entity) {
        //
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
}
