package house;

import livingEntities.LivingEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class House {
    private static House instance = null;
    private final List<Floor> floors;
    private final List<LivingEntity> livingEntities;
    private LocalTime time;
    private final OutsideArea outSideArea;
    private int day = 0;
    private static final Logger logger = Logger.getLogger("Smarthome");

    private final int houseId;

    public House(HouseBuilder builder) {
        this.floors = builder.floors;
        this.livingEntities = builder.livingEntities;
        this.houseId = builder.houseId;
        this.outSideArea = builder.outSideArea;
        instance = this;
    }

    public static HouseBuilder newBuilder() {
            if (instance != null) {
                logger.log(Level.WARNING, "Home has already been built.");
                throw new IllegalStateException("Home has already been built.");
            }
            return new HouseBuilder();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public static House getInstance(){
        return instance;
    }

    public List<Floor> getFloors() {
        return instance.floors;
    }

    public OutsideArea getOutSideArea() {
        return outSideArea;
    }

    public List<LivingEntity> getLivingEntities() {
        return instance.livingEntities;
    }

    public int getHouseId() {
        return houseId;
    }

    public void goOut(LivingEntity livingEntity) {
        outSideArea.addLivingEntity(livingEntity);
    }

    public void comeBack(LivingEntity livingEntity) {
        outSideArea.removeLivingEntity(livingEntity);
    }

    public static class HouseBuilder {
        private int houseId;
        private List<Floor> floors = new ArrayList<>();
        private List<LivingEntity> livingEntities = new ArrayList<>();
        private OutsideArea outSideArea;

        public HouseBuilder() {
        }

        public HouseBuilder setId(int houseId){
            this.houseId = houseId;
            return this;
        }

        public HouseBuilder setOutSideArea(OutsideArea outSideArea) {
            this.outSideArea = outSideArea;
            return this;
        }

        public HouseBuilder addFloor(Floor floor) {
            this.floors.add(floor);
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
