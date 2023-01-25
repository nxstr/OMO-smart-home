package livingEntities;

import house.Room;
import items.ElectricalItem;
import items.device.*;
import items.equipment.SportEquipment;
import items.sensors.Sensor;
import items.state.StateType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Adult extends Person{

    private static final int hungerTicks = 36;
    private Sensor fixingSensor = null;
    private static final Queue<ElectricalItem> devicesToControl = new LinkedList<>();


    public Adult(String name, EntityType type, Room room, int age) {
        super(name, type, room, age, hungerTicks);
    }

    @Override
    public void findActivity() {
        if(isHungry()){
            useFeed();
        }else if(!getToDoList().isEmpty()){
            doTasks();
        }else{
            int rand = new Random().nextInt(100);
            if(rand<40){
                useDevice();
            }else if(rand>=40 && rand<=80){
                useEquipment();
            }else{
                waiting();
            }
        }
    }

    @Override
    public void useDevice() {
        List<Device> freeDevices = getDevices().stream().filter(d->d.getCurrentState().getType()==StateType.IDLE).filter(d->d.getType() != DeviceType.FRIDGE).toList();
        if(!freeDevices.isEmpty()) {
            useRandomDevice(freeDevices);
        }
    }



    private void doTasks(){
        ElectricalItem item = devicesToControl.poll();
        //move to

        if(item!=null) {
            moveToRoom(item.getCurrentRoom());
            if(item.getMainType()=="device") {
                Device device = (Device) item;
                if (device.getType() == DeviceType.DISHWASHER && device.getCurrentState().getType() == StateType.IDLE) {
                    Dishwasher d = (Dishwasher) device;
                    if (d.isEmpty()) {
                        d.fill();
                        System.out.println("Person filled dishwasher at " + house.getTime());
                    } else {
                        if (d.isClean()) {
                            d.emptyDevice();
                            System.out.println("Person emptied dishwasher at " + house.getTime());
                            addTask(d);
                        }
                    }
                }
                if (device.getType() == DeviceType.WASHING_MACHINE && device.getCurrentState().getType() == StateType.IDLE) {
                    WashingMachine d = (WashingMachine) device;
                    if (d.isEmpty()) {
                        d.fill();
                        System.out.println("Person filled washing machine at " + house.getTime());
                    } else {
                        if (d.isClean()) {
                            d.emptyDevice();
                            System.out.println("Person emptied washing machine at " + house.getTime());
                            addTask(d);
                        }
                    }
                }
            }
                if (item.getCurrentState().getType() == StateType.BROKEN) {
                    repairDevice(item);
                }


        }
    }

    public static Queue<ElectricalItem> getToDoList() {
        return devicesToControl;
    }

    public static void addTask(ElectricalItem object) {
        if(!devicesToControl.contains(object)) {
            devicesToControl.add(object);
        }
    }

    public void repairDevice(ElectricalItem item){
        System.out.println(this.getName() + " repairing " + item.getName() + " at " +house.getTime());
        setCurrentDevice(item);
        item.fixingItem();
    }

}
