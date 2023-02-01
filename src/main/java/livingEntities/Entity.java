package livingEntities;

import events.EntityEvent;
import events.Event;
import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.ReportGenerator;
import items.equipment.SportEquipment;

import java.util.logging.Logger;

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
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Entity(String name, EntityType type, Room room, int hungerTicks) {
        this.room = room;
        this.name = name;
        this.type = type;
        this.hungerTicks = hungerTicks;
    }



    public int getHungerTicks() {
        return hungerTicks;
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public boolean isHungry() {
        return getCurrentHunger() >= getHungerTicks();
    }

    public boolean isAlarmMode() {
        return isAlarmMode;
    }

    public void setAlarmMode(boolean alarmMode) {
        isAlarmMode = alarmMode;
        if(alarmMode) {
            logger.info("alarm is setted for " + this.getName() + " at "+ house.getTime());
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
        logger.info(this.name + " is waiting at " +  house.getTime());
    }

    @Override
    public void moveToRoom(Room room){
        if (this.room == room) {
            return;
        }
        this.prevRoom = this.room;
        this.room = room;
        logger.info(this.name + " moves to " + room.getName() + " at "+ house.getTime());
    }

    @Override
    public void goOut(){
        this.prevRoom = this.room;
        this.room = null;
        house.goOut(this);
        logger.info(this.name + " goes out from house" + " at "+ house.getTime());
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
        logger.info(this.name + " comes back to house" + " at "+ house.getTime());
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
            getCurrentEq().stopEquipment();
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
            logger.info(this.getName() + " is sleeping" + " at "+ house.getTime());
        }else{
            logger.info(this.getName() + " is woke up" + " at "+ house.getTime());
        }
    }
}
