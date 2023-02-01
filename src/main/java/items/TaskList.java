package items;

import house.House;
import items.device.Device;
import items.device.DeviceType;
import items.device.Dishwasher;
import items.device.WashingMachine;
import items.state.StateType;
import livingEntities.Adult;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;

public class TaskList {
    private static TaskList instance;

    private static final Queue<ElectricalItem> devicesToControl = new LinkedList<>();
    private static final Logger logger = Logger.getLogger("Smarthome");

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
            if (item != null) {
                adult.moveToRoom(item.getCurrentRoom());
                if (Objects.equals(item.getMainType(), "device")) {
                    Device device = (Device) item;
                    if (device.getType() == DeviceType.DISHWASHER && device.getCurrentState().getType() == StateType.IDLE) {
                        Dishwasher d = (Dishwasher) device;
                        if (d.isEmpty()) {
                            d.fill();
                            logger.info(adult.getName() + " filled dishwasher at " + House.getInstance().getTime());
                        } else {
                            if (d.isClean()) {
                                d.emptyDevice();
                                logger.info(adult.getName() + " emptied dishwasher at " + House.getInstance().getTime());
                                addTask(d);
                            }
                        }
                    }
                    if (device.getType() == DeviceType.WASHING_MACHINE && device.getCurrentState().getType() == StateType.IDLE) {
                        WashingMachine d = (WashingMachine) device;
                        if (d.isEmpty()) {
                            d.fill();
                            logger.info(adult.getName() + " filled washing machine at " + House.getInstance().getTime());
                        } else {
                            if (d.isClean()) {
                                d.emptyDevice();
                                logger.info(adult.getName() + " emptied washing machine at " + House.getInstance().getTime());
                                addTask(d);
                            }
                        }
                    }
                }
            if (item.getCurrentState().getType() == StateType.BROKEN) {
                adult.setCurrentDevice(item);
                adult.repairDevice(item);
            }
        }
    }

    public Queue<ElectricalItem> getToDoList() {
        return devicesToControl;
    }
}
