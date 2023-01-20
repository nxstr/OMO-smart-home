package house;

import items.ElectricalItem;
import items.device.Device;
import items.equipment.SportEquipment;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final List<ElectricalItem> electricalItems = new ArrayList<>();
    private final List<SportEquipment> equipment = new ArrayList<>();
    private final Floor floor;

    public Room(String name, Floor floor) {
        this.name = name;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public Floor getFloor() {
        return floor;
    }

    public List<ElectricalItem> getElectricalItems() {
        return electricalItems;
    }

    public void addElectricalItem(ElectricalItem electricalItem){
        this.electricalItems.add(electricalItem);
    }
    public void removeElectricalItem(ElectricalItem electricalItem){
        this.electricalItems.remove(electricalItem);
    }

    public List<SportEquipment> getEquipment() {
        return equipment;
    }

    public void addEquipment(SportEquipment equipment1){
        this.equipment.add(equipment1);
    }
    public void removeEquipment(SportEquipment equipment1){
        this.equipment.remove(equipment1);
    }

}
