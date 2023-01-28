package livingEntities;

import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.TaskList;
import items.device.*;
import items.state.StateType;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class Adult extends Person{

    private static final int hungerTicks = 36;

    public Adult(String name, EntityType type, Room room, int age) {
        super(name, type, room, age, hungerTicks);
    }

    @Override
    public void findActivity() {
        if(house.getTime().isBefore(LocalTime.of(8, 0))){
            sleeping();
        }else {
            if(isAsleep()) {
                setAsleep(false);
                getReportGenerator().entityActivityReport(this, house.getTime());
            }
            else if (isHungry()) {
                boolean isFeeded = useFeed();
                if(!isFeeded && !TaskList.getInstance().getToDoList().isEmpty()){
                    doTasks();
                }
            } else if (!TaskList.getInstance().getToDoList().isEmpty()) {
                doTasks();
            } else {
                int rand = new Random().nextInt(100);
                if (rand < 40) {
                    useDevice();
                } else if (rand <= 80) {
                    useEquipment();
                } else {
                    waiting();
                }
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
        TaskList.getInstance().getTask(this);
    }

    public static void addTask(ElectricalItem object) {
        TaskList.getInstance().addTask(object);
    }

    public void repairDevice(ElectricalItem item){
        Observer.getInstance().logAction(this.getName() + " repairing " + item.getName() + " at " +house.getTime()+"\n");
        item.fixingItem();
    }

}
