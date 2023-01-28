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
import java.util.Objects;
import java.util.Queue;

public class TaskList {
    private static TaskList instance;

    private static final Queue<ElectricalItem> devicesToControl = new LinkedList<>();

    public TaskList() {

    }

    public static TaskList getInstance() {
        if (instance == null) instance = new TaskList();
        return instance;
    }

    public static void addTask(ElectricalItem object) {
        if(!devicesToControl.contains(object)) {
            devicesToControl.add(object);
        }
    }

    public void getTask(Adult adult) {
        ElectricalItem item = devicesToControl.poll();

        if (adult.getType() == EntityType.MOTHER) {
            if (item != null) {
                adult.moveToRoom(item.getCurrentRoom());
                if (Objects.equals(item.getMainType(), "device")) {
                    Device device = (Device) item;
                    if (device.getType() == DeviceType.DISHWASHER && device.getCurrentState().getType() == StateType.IDLE) {
                        Dishwasher d = (Dishwasher) device;
                        if (d.isEmpty()) {
                            d.fill();
                            System.out.println(adult.getName() + " filled dishwasher at " + House.getInstance().getTime());
                        } else {
                            if (d.isClean()) {
                                d.emptyDevice();
                                System.out.println(adult.getName() + " emptied dishwasher at " + House.getInstance().getTime());
                                addTask(d);
                            }
                        }
                    }
                    if (device.getType() == DeviceType.WASHING_MACHINE && device.getCurrentState().getType() == StateType.IDLE) {
                        WashingMachine d = (WashingMachine) device;
                        if (d.isEmpty()) {
                            d.fill();
                            System.out.println(adult.getName() + " filled washing machine at " + House.getInstance().getTime());
                        } else {
                            if (d.isClean()) {
                                d.emptyDevice();
                                System.out.println(adult.getName() + " emptied washing machine at " + House.getInstance().getTime());
                                addTask(d);
                            }
                        }
                    }
                }
            }
            if(item!=null){
                if (item.getCurrentState().getType() == StateType.BROKEN) {
                    adult.repairDevice(item);
                    System.out.println(adult.getName() + " ------------ is fixing " + item.getName());
                }
            }
        }
    }

    public Queue<ElectricalItem> getToDoList() {
        for(ElectricalItem item: devicesToControl){
            System.out.println(item.getName() + " ---------------- in device list");
        }
        return devicesToControl;
    }
}
