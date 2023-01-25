package livingEntities;

import house.Room;
import items.device.Device;
import items.device.DeviceType;
import items.state.StateType;

import java.util.List;

public class Child extends Person{

    private static final int hungerTicks = 24;

    public Child(String name, EntityType type, Room room, int age) {
        super(name, type, room, age, hungerTicks);
    }

    @Override
    public void useDevice() {
        List<Device> freeDevices = getDevices().stream().filter(d->d.getCurrentState().getType()== StateType.IDLE).
                filter(d->d.getType() != DeviceType.FRIDGE && d.getType() != DeviceType.COFFEE_MACHINE).toList();
        if(!freeDevices.isEmpty()) {
            useRandomDevice(freeDevices);
        }
    }
}
