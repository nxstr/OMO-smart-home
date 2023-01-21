package livingEntities;

import house.Room;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.device.PetFeeder;
import items.equipment.SportEquipment;
import items.equipment.SportEquipmentFactory;
import items.equipment.SportEquipmentType;
import items.state.IdleState;
import items.state.StateType;

import java.util.List;
import java.util.Random;

public class Pet implements LivingEntity{

    private final String name;
    private Room room;
    private final EntityType type;
    private Room prevRoom;
    private int currentBackActionProgress = 0;
    private final List<Device> devices = DeviceFactory.getInstance().getDevices();
    private final List<SportEquipment> equipments = SportEquipmentFactory.getInstance().getEquipments();
    private boolean isHungry = false;
    private Device currentDevice = null;
    private SportEquipment currentEq = null;


    public Pet(String name, EntityType type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.prevRoom = null;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public Room getPrevRoom(){
        return prevRoom;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<SportEquipment> getEquipments() {
        return equipments;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }

    @Override
    public void waiting() {
        System.out.println(name + " is waiting for now");
    }

    @Override
    public void moveToRoom(Room room){
        if (this.room == room) {
            return;
        }
        this.prevRoom = this.room;
        this.room = room;
        System.out.println(this.name + " moves to " + this.room.getName());
    }

    @Override
    public void goOut(){
        this.prevRoom = this.room;
        this.room = null;
        house.goOut(this);
    }

    @Override
    public void findActivity() {
        if(isHungry()){
            useDevice();
        }else{
            int rand = new Random().nextInt(100);
            if(rand<20){
                useDevice();
            }else if(rand>=20 && rand<=70){
                useEquipment();
            }else{
                waiting();
            }
        }
    }

    @Override
    public void comeBack(){
        house.comeBack(this);
        this.room = this.prevRoom;
        this.prevRoom = null;
        System.out.println(this.name + " comes back to house ");
    }

    @Override
    public Room getCurrentRoom(){
        return this.room;
    }

    public int getCurrentBackActionProgress(){
        return currentBackActionProgress;
    }
    public void increaseCBAP(){
        currentBackActionProgress++;
    }

    public void stopCurrentActivity() {

        this.currentBackActionProgress = 0;
        if(getCurrentRoom()==null){
            comeBack();
        }
        if(getCurrentDevice()!=null){
            getCurrentDevice().stopDevice(this);
            setCurrentDevice(null);
        }
        if(getCurrentEq()!=null){
            getCurrentEq().setCurrentState(new IdleState(getCurrentEq()));
            setCurrentEq(null);
        }
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public SportEquipment getCurrentEq() {
        return currentEq;
    }

    public void setCurrentEq(SportEquipment currentEq) {
        this.currentEq = currentEq;
    }

    @Override
    public void useDevice() {
        List<Device> petDevices = getDevices().stream().filter(d->!d.isForHuman()).filter(d->d.getCurrentState().getType() == StateType.IDLE).toList();
        for(Device d:petDevices) {
            if (d.getType() == DeviceType.PET_FEEDER && d.getCurrentState().getType()!=StateType.BROKEN) {
                PetFeeder p = (PetFeeder) d;
                if (!p.isPortionEated() && isHungry()) {
                    setCurrentDevice(p);
                    System.out.println(this.getName() + " is eating!!!");
                    p.usingDeviceByPet();
                    if(p.getCurrentState().getType()==StateType.BROKEN){
                        stopCurrentActivity();
                        return;
                    }
                    setHungry(false);
                    return;
                }else if(p.isPortionEated() && isHungry()){
                    System.out.println(this.getName() + " is hungry!!!!!!!!");

                }
            }
        }

        for(Device d:petDevices) {
            if (d.getType() == DeviceType.PET_FOUNTAIN && d.getCurrentState().getType()!=StateType.BROKEN) {
                setCurrentDevice(d);
                System.out.println(this.getName() + " is drinking!!!");
                d.usingDevice();
                if(d.getCurrentState().getType()==StateType.BROKEN){
                    stopCurrentActivity();
                    return;
                }
                return;
            }
        }
    }

    @Override
    public void useEquipment() {
        List<SportEquipment> eq = getEquipments().stream().filter(d->d.getType() == SportEquipmentType.PET_TOY).filter(d->d.getCurrentState().getType() == StateType.IDLE).toList();
        for(SportEquipment e: eq){
            setCurrentEq(e);
            System.out.println(this.getName() + " is playing!!!");
            int rand = new Random().nextInt(30);
            if(rand<10){
                goOut();
                System.out.println(getName() + " is outside the house");
            }
            e.usingEquipment();
            return;
        }
    }
}
