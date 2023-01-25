package livingEntities;

import house.House;
import house.Room;
import items.ElectricalItem;
import items.device.Device;
import items.equipment.SportEquipment;

public interface LivingEntity {

    House house = House.getInstance();

    void moveToRoom(Room room);

    Room getCurrentRoom();

    void findActivity();

    void waiting();

    void stopCurrentActivity();

    void useDevice();

    void useEquipment();

    void goOut();

    void comeBack();

    EntityType getType();

    int getCurrentBackActionProgress();

    void increaseCBAP();

    ElectricalItem getCurrentDevice();

    SportEquipment getCurrentEq();

    void sleeping();

    void increaseHunger();

}
