package items;

import house.House;
import items.device.Device;
import items.device.DeviceType;
import items.device.Dishwasher;
import items.device.WashingMachine;
import items.state.StateType;
import livingEntities.Adult;
import livingEntities.EntityType;

import java.util.LinkedList;
import java.util.Queue;

public class TaskList {
    private final Adult adult;
    private static House house = House.getInstance();

    private static final Queue<ElectricalItem> devicesToControl = new LinkedList<>();

    public TaskList(Adult adult) {
        this.adult = adult;
    }

    public static void addTask(ElectricalItem object) {
        if(!devicesToControl.contains(object)) {
            devicesToControl.add(object);
        }
    }

    public void getTask() {
        ElectricalItem item = devicesToControl.poll();

        if (adult.getType() == EntityType.MOTHER) {
            if (item != null) {
                adult.moveToRoom(item.getCurrentRoom());
                if (item.getMainType() == "device") {
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
            }
        }else if(adult.getType() == EntityType.FATHER){
            if (item.getCurrentState().getType() == StateType.BROKEN) {
                adult.repairDevice(item);
            }
        }
    }

    public Queue<ElectricalItem> getToDoList() {
        return devicesToControl;
    }
}
