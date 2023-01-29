package house;

import items.ElectricalItem;
import items.equipment.SportEquipment;
import livingEntities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class OutsideArea {
    private static OutsideArea instance = null;
    private List<LivingEntity> entities = new ArrayList<>();

    public OutsideArea() {
    }

    public static OutsideArea getInstance() {
        if (instance == null){
            instance = new OutsideArea();
        }
        return instance;
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
