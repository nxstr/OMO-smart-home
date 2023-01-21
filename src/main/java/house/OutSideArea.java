package house;

import items.ElectricalItem;
import items.equipment.SportEquipment;
import livingEntities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class OutSideArea {
    private static OutSideArea instance = null;
    private List<ElectricalItem> electricalItems = new ArrayList<>();
    private List<SportEquipment> equipment = new ArrayList<>();
    private List<LivingEntity> entities = new ArrayList<>();

    public OutSideArea() {
    }

    public static OutSideArea getInstance() {
        if (instance == null){
            instance = new OutSideArea();
        }
        return instance;
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

    public List<LivingEntity> getEntities() {
        return entities;
    }

    public void addLivingEntity(LivingEntity entity){
        this.entities.add(entity);
    }
    public void removeLivingEntity(LivingEntity entity){
        this.entities.remove(entity);
    }
}
