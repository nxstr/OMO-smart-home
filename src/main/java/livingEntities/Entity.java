package livingEntities;

import events.EntityEvent;
import events.Event;
import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.equipment.SportEquipment;
import items.state.IdleState;

public abstract class Entity implements LivingEntity{
    private final String name;
    private Room room;
    private final EntityType type;
    private Room prevRoom;
    private int currentBackActionProgress = 0;
    private ElectricalItem currentDevice = null;
    private SportEquipment currentEq = null;
    private final int hungerTicks;
    private int currentHunger = 0;
    private final Observer observer = Observer.getInstance();
    private boolean isAlarmMode = false;
    private boolean isAsleep = false;

    public Entity(String name, EntityType type, Room room, int hungerTicks) {
        this.room = room;
        this.name = name;
        this.type = type;
        this.hungerTicks = hungerTicks;
    }

    public int getHungerTicks() {
        return hungerTicks;
    }

    public boolean isHungry() {
        if(getCurrentHunger()>=getHungerTicks()){
            return true;
        }
        return false;
    }

    public boolean isAlarmMode() {
        return isAlarmMode;
    }

    public void setAlarmMode(boolean alarmMode) {
        isAlarmMode = alarmMode;
        if(alarmMode) {
            System.out.println("alarm is setted for " + this.getName());
            if(isAsleep){
                setAsleep(false);
            }
            stopCurrentActivity();
            if(getCurrentRoom()!=null){
                goOut();
            }
        }else{
            comeBack();
        }
    }

    public int getCurrentHunger(){
        return currentHunger;
    }
    public void increaseHunger(){
        currentHunger++;
    }

    public void resetCurrentHunger() {
        this.currentHunger = 0;
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

    @Override
    public void waiting() {
        System.out.println(this.name + " is waiting for now");
    }

    @Override
    public void moveToRoom(Room room){
        if (this.room == room) {
            return;
        }
        this.prevRoom = this.room;
        this.room = room;
        System.out.println(this.name + " moves to -------------- " + room.getName());
    }

    @Override
    public void goOut(){
        this.prevRoom = this.room;
        this.room = null;
        house.goOut(this);
        System.out.println(this.name + " goes out from house ");
        Event event = new EntityEvent(getPrevRoom(), house.getTime());
        observer.eventHandler(event);
    }

    @Override
    public void comeBack(){
        if(isAlarmMode){
            return;
        }
        house.comeBack(this);
        this.room = this.prevRoom;
        this.prevRoom = null;
        System.out.println(this.name + " comes back to house ");
        Event event = new EntityEvent(getPrevRoom(), house.getTime());
        observer.eventHandler(event);
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
            getCurrentDevice().stopDevice();
            setCurrentDevice(null);
        }
        if(getCurrentEq()!=null){
            getCurrentEq().setCurrentState(new IdleState(getCurrentEq()));
            setCurrentEq(null);
        }
    }

    public ElectricalItem getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(ElectricalItem currentDevice) {
        this.currentDevice = currentDevice;
    }

    public SportEquipment getCurrentEq() {
        return currentEq;
    }

    public void setCurrentEq(SportEquipment currentEq) {
        this.currentEq = currentEq;
    }

    public void sleeping(){
        if(!isAsleep()){
            setAsleep(true);
        }
        increaseHunger();

    }

    public boolean isAsleep() {
        return isAsleep;
    }

    public void setAsleep(boolean asleep) {
        isAsleep = asleep;
        if(asleep) {
            stopCurrentActivity();
            System.out.println(this.getName() + " is sleeping");
        }else{
            System.out.println(this.getName() + " is woke up");
        }
    }
}
