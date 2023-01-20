package livingEntities;

import items.device.Device;
import house.House;
import house.Room;
import items.equipment.SportEquipment;

public interface LivingEntity {

    House house = House.getInstance();

    void moveToRoom(Room room);

    Room getCurrentRoom();

    void findActivity();

    void waiting();

    void stopCurrentActivity();

    void useDevice(Device device);

    void useEquipment(SportEquipment equipment);

    void goOut();

    void comeBack();

    EntityType getType();

}
