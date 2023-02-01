package livingEntities;

import house.Room;
import items.Observer;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.device.PetFeeder;
import items.equipment.SportEquipment;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.state.StateType;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Pet extends Entity{

    private static final int hungerTicks = 29;
    private final List<Device> devices = DeviceFactory.getInstance().getPetDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();
    private static final Logger logger = Logger.getLogger("Smarthome");


    public Pet(String name, EntityType type, Room room) {
        super(name, type, room, hungerTicks);
    }

    public List<Device> getDevices() {
        return devices;
    }
    public List<SportEquipment> getEquipments() {
        return equipments;
    }

    @Override
    public void findActivity() {
        if(house.getTime().isBefore(LocalTime.of(8, 0))){
            sleeping();
        }else {
            if(isAsleep()) {
                setAsleep(false);
            }
            else if (isHungry()) {
                useFeed();
            } else {
                int rand = new Random().nextInt(100);
                if (rand < 20) {
                    useDevice();
                } else if (rand <= 70) {
                    useEquipment();
                } else {
                    waiting();
                }
            }
        }
    }

    public void useFeed(){
        List<Device> petDevices = getDevices().stream().filter(d->d.getCurrentState().getType() == StateType.IDLE).filter(d->d.getType() == DeviceType.PET_FEEDER).toList();
        for(Device d:petDevices) {
                PetFeeder p = (PetFeeder) d;
                if (!p.isPortionEated() && isHungry()) {
                    moveToRoom(p.getCurrentRoom());
                    setCurrentDevice(p);
                    logger.info(this.getName() + " is trying to eat at "+ house.getTime());
                    p.usingDeviceByPet();
                    if(p.getCurrentState().getType()==StateType.BROKEN){
                        stopCurrentActivity();
                        logger.info(this.getName() + " can not eat cause pet feeder is broken");
                        return;
                    }
                    resetCurrentHunger();
                    getReportGenerator().entityActivityReport(this, house.getTime());
                    return;
                }else if(p.isPortionEated() && isHungry()){
                    logger.info(this.getName() + " is hungry at "+ house.getTime());
                }
            }
    }

    @Override
    public void useDevice() {
        List<Device> petDevices = getDevices().stream().filter(d->d.getCurrentState().getType() == StateType.IDLE).filter(d->d.getType() != DeviceType.PET_FEEDER).toList();
        for(Device d:petDevices) {
            moveToRoom(d.getCurrentRoom());
            setCurrentDevice(d);
            logger.info(this.getName() + " is trying to drink at "+ house.getTime());
            d.usingDevice();
            if(d.getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                logger.info(this.getName() + " can not eat cause pet fountain is broken");
                return;
            }
            getReportGenerator().entityActivityReport(this, house.getTime());
            return;
            }
    }

    @Override
    public void useEquipment() {
        List<SportEquipment> eq = getEquipments().stream().filter(d->d.getType() == SportEquipmentType.PET_TOY).filter(d->d.getCurrentState().getType() == StateType.IDLE).toList();
        for(SportEquipment e: eq){
            moveToRoom(e.getCurrentRoom());
            setCurrentEq(e);
            logger.info(this.getName() + " is trying to play at "+ house.getTime());
            int rand = new Random().nextInt(30);
            if(rand<10){
                goOut();
                logger.info(this.getName() + " is outside the house while playing at "+ house.getTime());
            }
            e.usingEquipment();
            getReportGenerator().entityActivityReport(this, house.getTime());
            return;
        }
    }
}
