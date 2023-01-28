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

public class Pet extends Entity{

    private static final int hungerTicks = 29;
    private final List<Device> devices = DeviceFactory.getInstance().getPetDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();


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
                    Observer.getInstance().logAction(this.getName() + " is eating\n");
                    p.usingDeviceByPet();
                    if(p.getCurrentState().getType()==StateType.BROKEN){
                        stopCurrentActivity();
                        return;
                    }
                    resetCurrentHunger();
                    getReportGenerator().entityActivityReport(this, house.getTime());
                    return;
                }else if(p.isPortionEated() && isHungry()){
                    Observer.getInstance().logAction(this.getName() + " is hungry\n");

                }
            }
    }

    @Override
    public void useDevice() {
        List<Device> petDevices = getDevices().stream().filter(d->d.getCurrentState().getType() == StateType.IDLE).filter(d->d.getType() != DeviceType.PET_FEEDER).toList();

        for(Device d:petDevices) {
                moveToRoom(d.getCurrentRoom());
                setCurrentDevice(d);
                Observer.getInstance().logAction(this.getName() + " is drinking\n");
                d.usingDevice();
                if(d.getCurrentState().getType()==StateType.BROKEN){
                    stopCurrentActivity();
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
            Observer.getInstance().logAction(this.getName() + " is playing\n");
            int rand = new Random().nextInt(30);
            if(rand<10){
                goOut();
                Observer.getInstance().logAction(getName() + " is outside the house\n");
            }
            e.usingEquipment();
            getReportGenerator().entityActivityReport(this, house.getTime());
            return;
        }
    }
}
