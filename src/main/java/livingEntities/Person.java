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
    private List<Device> devices = DeviceFactory.getInstance().getHumanDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();


    public Person(String name, EntityType type, Room room, int age, int hungerTicks) {
        super(name, type, room, hungerTicks);
        this.age = age;
    }

    @Override
    public void findActivity() {
        if(house.getTime().isAfter(LocalTime.of(22, 0)) && age<16){
            sleeping();
        }else if(isHungry()){
            useFeed();
        }else{
            int rand = new Random().nextInt(100);
            if(rand<35){
                useDevice();
            }else if(rand>=35 && rand<=75){
                useEquipment();
            }else{
                waiting();
            }
        }
    }

    public void useFeed(){
        List<Device> fridges = devices.stream().filter(d->d.getType()== DeviceType.FRIDGE).filter(d->d.getCurrentState().getType()== StateType.IDLE).toList();
        if(!fridges.isEmpty()){
            System.out.println(this.getName() + " is eating!!!");
            fridges.get(0).usingDevice();
            setCurrentDevice(fridges.get(0));
            if(fridges.get(0).getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                return;
            }
            resetCurrentHunger();
            System.out.println(this.getName() + " has hunger " + getCurrentHunger());
        }
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<SportEquipment> getEquipments() {
        return equipments;
    }

    public int getAge() {
        return age;
    }

    @Override
    public void useEquipment() {
        List<SportEquipment> freeEquipment = equipments.stream().filter(e->e.getType()!= SportEquipmentType.PET_TOY).filter(e->e.getCurrentState().getType()==StateType.IDLE).toList();
        if(!freeEquipment.isEmpty()) {
            int rand = new Random().nextInt(freeEquipment.size());
            goOut();
            freeEquipment.get(rand).usingEquipment();
            System.out.println(this.getName() + " is goes sport outside!!!");
            setCurrentEq(freeEquipment.get(rand));
            if(freeEquipment.get(rand).getCurrentState().getType()==StateType.BROKEN){
                stopCurrentActivity();
                return;
            }
        }
    }

    public void useRandomDevice(List<Device> freeDevices){
        int rand = new Random().nextInt(freeDevices.size());
        freeDevices.get(rand).usingDevice();
        setCurrentDevice(freeDevices.get(rand));
        System.out.println(this.getName() + " is using device " + freeDevices.get(rand).getType());
        if(freeDevices.get(rand).getCurrentState().getType()==StateType.BROKEN){
            stopCurrentActivity();
            return;
        }
    }
}
