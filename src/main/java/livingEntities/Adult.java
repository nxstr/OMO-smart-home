package livingEntities;

import house.Room;
import items.device.Device;
import items.equipment.SportEquipment;

public class Adult extends Person{


    public Adult(String name, EntityType type, int age, Room room) {
        super(name, type, age, room);
    }

    @Override
    public void findActivity() {

    }

    @Override
    public void stopCurrentActivity() {

    }

    @Override
    public void useDevice(Device device) {

    }

    @Override
    public void useEquipment(SportEquipment equipment) {

    }
}
