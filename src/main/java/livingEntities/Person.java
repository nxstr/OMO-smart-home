package livingEntities;

import house.Room;
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

public abstract class Person extends Entity{
    private final int age;
    private final List<Device> devices = DeviceFactory.getInstance().getHumanDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();


    public Person(String name, EntityType type, Room room, int age, int hungerTicks) {
        super(name, type, room, hungerTicks);
        this.age = age;
    }

    @Override
    public void findActivity() {
        if((house.getTime().isAfter(LocalTime.of(22, 0)) || house.getTime().isBefore(LocalTime.of(8, 0))) && age<16){
            sleeping();
        }else {
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
            System.out.println(this.getName() + " is eating!!!");
            fridges.get(0).usingDevice();
            setCurrentDevice(fridges.get(0));
            if(fridges.get(0).getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                return false;
            }
            resetCurrentHunger();
            System.out.println(this.getName() + " has hunger " + getCurrentHunger());
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
            System.out.println(this.getName() + " is goes sport outside!!!");
            setCurrentEq(e);
            if(e.getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                return;
            }
        }
    }

    public void useRandomDevice(List<Device> freeDevices){
        int rand = new Random().nextInt(freeDevices.size());
        moveToRoom(freeDevices.get(rand).getCurrentRoom());
        freeDevices.get(rand).usingDevice();
        setCurrentDevice(freeDevices.get(rand));
        System.out.println(this.getName() + " is using device " + freeDevices.get(rand).getType());
        if(freeDevices.get(rand).getCurrentState().getType()==StateType.BROKEN){
            stopCurrentActivity();
            return;
        }
    }
}
