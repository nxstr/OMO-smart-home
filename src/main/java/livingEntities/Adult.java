package livingEntities;

import house.Room;
import items.ElectricalItem;
import items.device.Device;
import items.device.DeviceType;
import items.device.Dishwasher;
import items.device.WashingMachine;
import items.equipment.SportEquipment;
import items.state.StateType;

import java.util.LinkedList;
import java.util.Queue;

public class Adult extends Person{

    private static final Queue<Device> toDoList = new LinkedList<>();


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
    public void stopCurrentActivity() {

    }

    @Override
    public void useDevice(Device device) {

    }

    @Override
    public void useEquipment(SportEquipment equipment) {

    }

    private void doTasks(){
        Device device = toDoList.poll();
        //move to
        if(device.getType()== DeviceType.DISHWASHER && device.getCurrentState().getType()== StateType.IDLE){
            Dishwasher d = (Dishwasher) device;
            if(d.isEmpty()){
                d.fill();
                System.out.println("Person filled dishwasher");
            }else{
                if(d.isClean()){
                    d.emptyDevice();
                    System.out.println("Person emptied dishwasher");
                    addTask(d);
                }
            }
        }
        if(device.getType()== DeviceType.WASHING_MACHINE && device.getCurrentState().getType()== StateType.IDLE){
            WashingMachine d = (WashingMachine) device;
            if(d.isEmpty()){
                d.fill();
                System.out.println("Person filled washing machine");
            }else{
                if(d.isClean()){
                    d.emptyDevice();
                    System.out.println("Person emptied washing machine");
                    addTask(d);
                }
            }
        }
    }

    public static Queue<Device> getToDoList() {
        return toDoList;
    }

    public static void addTask(Device object) {
        toDoList.add(object);
    }
}
