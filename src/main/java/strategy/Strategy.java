package strategy;

import house.House;
import items.device.Device;
import items.device.DeviceFactory;
import livingEntities.LivingEntity;

import java.util.List;

public interface Strategy {

    DeviceFactory deviceFactory = DeviceFactory.getInstance();

    void findActivity(LivingEntity entity);

    List<Device> getActiveDevices();

    int getCurrentBackActionProgress();

    void increaseCBAP();

    void stopBackAction();

    void addActiveDevice(Device device);
}