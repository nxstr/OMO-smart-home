package livingEntities;

import house.Room;
import items.ElectricalItem;
import items.device.*;
import items.equipment.SportEquipment;
import items.state.StateType;

import java.util.LinkedList;
import java.util.Queue;

public class Adult extends Person{

    private static final Queue<Device> devicesToControl = new LinkedList<>();


    public Adult(String name, EntityType type, int age, Room room) {
        super(name, type, age, room);
    }

    @Override
    public void findActivity() {
        if(!getToDoList().isEmpty()){
            doTasks();
        }
    }

    @Override
    public void useDevice() {

    }

    @Override
    public void useEquipment() {

    }

    private void doTasks(){
        Device device = devicesToControl.poll();
        //move to
        if(device.getType()== DeviceType.DISHWASHER && device.getCurrentState().getType()== StateType.IDLE){
            Dishwasher d = (Dishwasher) device;
            if(d.isEmpty()){
                d.fill();
                System.out.println("Person filled dishwasher at " +house.getTime());
            }else{
                if(d.isClean()){
                    d.emptyDevice();
                    System.out.println("Person emptied dishwasher at " +house.getTime());
                    addTask(d);
                }
            }
        }
        if(device.getType()== DeviceType.WASHING_MACHINE && device.getCurrentState().getType()== StateType.IDLE){
            WashingMachine d = (WashingMachine) device;
            if(d.isEmpty()){
                d.fill();
                System.out.println("Person filled washing machine at "+house.getTime());
            }else{
                if(d.isClean()){
                    d.emptyDevice();
                    System.out.println("Person emptied washing machine at "+house.getTime());
                    addTask(d);
                }
            }
        }
        if(device.getCurrentState().getType() == StateType.BROKEN){
            repairDevice(device);
        }
    }

    public static Queue<Device> getToDoList() {
        return devicesToControl;
    }

    public static void addTask(Device object) {
        if(!devicesToControl.contains(object)) {
            devicesToControl.add(object);
        }
    }

    public void repairDevice(Device item){
        System.out.println(this.getName() + " repairing " + item.getType() + " at " +house.getTime());
        Manual manual = item.getManual();
        manual.readDeviceManual();
        setCurrentDevice(item);
        item.fixingItem();
    }
}
