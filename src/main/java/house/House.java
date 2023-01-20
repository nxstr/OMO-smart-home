package house;

import items.ElectricalItem;
import livingEntities.LivingEntity;
import strategy.Strategy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class House {
    private static House instance = null;
    private final List<Floor> floors;
    private final List<LivingEntity> livingEntities; //maybe pak udelat get access pres streamy idk

    private final int houseId;

    public House(HouseBuilder builder) {
        this.floors = builder.floors;
        this.livingEntities = builder.livingEntities;
        this.houseId = builder.houseId;
        instance = this;
    }

    public static HouseBuilder newBuilder() {
        if (instance != null) {
            throw new IllegalStateException("Home has already been built.");
        }
        return new HouseBuilder();
    }

    public static House getInstance(){
        return instance;
    }

    public List<Floor> getFloors() {
        return instance.floors;
    }

    public List<LivingEntity> getLivingEntities() {
        return instance.livingEntities;
    }

    public int getHouseId() {
        return houseId;
    }

    public void goOut(LivingEntity livingEntity) {
        livingEntities.remove(livingEntity);
    }

    public void comeBack(LivingEntity livingEntity) {
        livingEntities.add(livingEntity);
    }

    public static class HouseBuilder {
        private int houseId;
        private List<Floor> floors = new ArrayList<>();
        private List<LivingEntity> livingEntities = new ArrayList<>();

        public HouseBuilder() {
        }



        public HouseBuilder setId(int houseId){
            this.houseId = houseId;
            return this;
        }

        public HouseBuilder addFloor(Floor floor) {
            this.floors.add(floor);
            return this;
        }

        public HouseBuilder setFloors(List<Floor> floors) {
            this.floors = floors;
            return this;
        }

        public HouseBuilder setLivingEntities(List<LivingEntity> livingEntities) {
            this.livingEntities = livingEntities;
            return this;
        }

        public HouseBuilder addLivingEntity(LivingEntity livingEntity) {
            this.livingEntities.add(livingEntity);
            return this;
        }

        public House build() {
            return new House(this);
        }
    }

}
