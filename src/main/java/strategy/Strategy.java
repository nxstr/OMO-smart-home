package strategy;

import items.device.Device;
import items.device.DeviceFactory;

import java.util.List;

public interface Strategy {

    DeviceFactory deviceFactory = DeviceFactory.getInstance();

    void setup();

    List<Device> getActiveDevices();

    int getCurrentBackActionProgress();

    void increaseCBAP();

    void stopBackAction();

    void addActiveDevice(Device device);

    void removeActiveDevice(Device device);

    void setActivatedDevices(List<Device> activatedDevices);
}
