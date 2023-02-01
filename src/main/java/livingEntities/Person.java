package livingEntities;

import house.Room;
import items.Observer;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.equipment.SportEquipment;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.state.StateType;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public abstract class Person extends Entity{
    private final int age;
    private final List<Device> devices = DeviceFactory.getInstance().getHumanDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Person(String name, EntityType type, Room room, int age, int hungerTicks) {
        super(name, type, room, hungerTicks);
        this.age = age;
    }

    @Override
    public void findActivity() {
        if((house.getTime().isAfter(LocalTime.of(22, 0)) || house.getTime().isBefore(LocalTime.of(8, 0))) && age<16){
            sleeping();
        }else if(house.getTime().isBefore(LocalTime.of(8, 0)) && age>=16){
            sleeping();
        }
        else {
            if(isAsleep()) {
                setAsleep(false);
            }
            else if (isHungry()) {
                useFeed();
            } else {
                int rand = new Random().nextInt(100);
                if (rand < 35) {
                    useDevice();
                } else if (rand <= 75) {
                    useEquipment();
                } else {
                    waiting();
                }
            }
        }
    }

    public boolean useFeed(){
        List<Device> fridges = devices.stream().filter(d->d.getType()== DeviceType.FRIDGE).filter(d->d.getCurrentState().getType()== StateType.IDLE).toList();
        if(!fridges.isEmpty()){
            moveToRoom(fridges.get(0).getCurrentRoom());
            logger.info(this.getName() + " is trying to eat" + " at "+ house.getTime());
            fridges.get(0).usingDevice();
            setCurrentDevice(fridges.get(0));
            if(fridges.get(0).getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                logger.info(this.getName() + " can not eat cause fridge is broken");
                return false;
            }
            resetCurrentHunger();
            getReportGenerator().entityActivityReport(this, house.getTime());
            return true;
        }
        return false;
    }

    public List<Device> getDevices() {
        return devices;
    }

    @Override
    public void useEquipment() {
        List<SportEquipment> freeEquipment = equipments.stream().filter(e->e.getType()!= SportEquipmentType.PET_TOY).filter(e->e.getCurrentState().getType()==StateType.IDLE).toList();
        if(!freeEquipment.isEmpty()) {
            int rand = new Random().nextInt(freeEquipment.size());
            SportEquipment e = freeEquipment.get(rand);
            moveToRoom(e.getCurrentRoom());
            e.usingEquipment();
            goOut();
            logger.info(this.getName() + " is goes sport outside" + " at "+ house.getTime());
            setCurrentEq(e);
            if(e.getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                logger.info(this.getName() + " can not sport cause equipment is broken");
                return;
            }
            getReportGenerator().entityActivityReport(this, house.getTime());
        }
    }

    public void useRandomDevice(List<Device> freeDevices){
        int rand = new Random().nextInt(freeDevices.size());
        moveToRoom(freeDevices.get(rand).getCurrentRoom());
        freeDevices.get(rand).usingDevice();
        setCurrentDevice(freeDevices.get(rand));
        logger.info(this.getName() + " is using device " + freeDevices.get(rand).getType() + " at "+ house.getTime());
        if(freeDevices.get(rand).getCurrentState().getType()==StateType.BROKEN){
            stopCurrentActivity();
            logger.info(this.getName() + " can not sport cause device is broken");
            return;
        }
        getReportGenerator().entityActivityReport(this, house.getTime());
    }
}
