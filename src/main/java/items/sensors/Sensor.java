package items.sensors;

import house.House;
import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.DeviceFactory;
import items.device.Manual;
import items.state.*;
import java.util.Random;
import java.util.logging.Logger;

public abstract class Sensor implements ElectricalItem {
    private final SensorType type;
    private ObjectState currentState = new ActiveState(this);
    private boolean alarmMode = false;
    private final DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private int usingHours = 24*6;
    private final Room currentRoom;
    private int electricityUsed =0;
    private final int electricityInOnState;
    private final int electricityInBrokeState;
    private int brokenTimes =0;
    private Manual manual;
    private final House house = House.getInstance();
    private static boolean isEnergyOn = true;
    private static final Logger logger = Logger.getLogger("Smarthome");

    public Sensor(SensorType type, Room currentRoom, int electricityInOnState, int electricityInBrokeState) {
        this.type = type;
        this.currentRoom = currentRoom;
        this.electricityInOnState = electricityInOnState;
        this.electricityInBrokeState = electricityInBrokeState;
    }

    public House getHouse() {
        return house;
    }

    public DeviceFactory getDeviceFactory() {
        return deviceFactory;
    }

    public String getName(){
        return getType().name();
    }

    public String getMainType(){
        return "sensor";
    }

    public boolean isAlarmMode() {
        return alarmMode;
    }

    public void setAlarmMode(boolean alarmMode) {
        this.alarmMode = alarmMode;
    }

    public ObjectState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ObjectState currentState) {
        this.currentState = currentState;
    }

    public SensorType getType() {
        return type;
    }

    public int getUsingHours() {
        return usingHours;
    }

    public void setUsingHours(int usingHours) {
        this.usingHours = usingHours;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int getElectricityUsed() {
        return electricityUsed;
    }

    public void setElectricityUsed(int electricityUsed) {
        this.electricityUsed = electricityUsed;
    }

    public int getElectricityInOnState() {
        return electricityInOnState;
    }

    public int getElectricityInBrokeState() {
        return electricityInBrokeState;
    }

    public int getBrokenTimes() {
        return brokenTimes;
    }

    public void setBrokenTimes(int brokenTimes) {
        this.brokenTimes = brokenTimes;
    }

    public void resetElectricity(){
        this.electricityUsed = 0;
    }

    public void addUsedElectricity(int electricity) {
        electricityUsed += electricity;
    }

    public void breakingItem() {
        logger.info(this.getType() + " Sensor is broken at "+ getHouse().getTime());
        brokenTimes++;
        setCurrentState(new BrokenState(this));
        addUsedElectricity(getElectricityInOnState()*getUsingHours());
        generateReportForObserver();
    }

    public void fixingItem() {
        setUsingHours(12);
        setCurrentState(new FixingState(this));
        logger.info(this.getType() + " Sensor is fixing at "+ getHouse().getTime());
        Manual manual = getManual();
        manual.readDeviceManual();
        resetElectricity();
    }

    public Manual getManual() {
        if(manual==null){ manual = new Manual(this);}
        return manual;
    }

    public void generateReportForObserver(){
        observer.handleSensorReport(this);
    }

    public void breakingEvent(){
        int r = new Random().nextInt(0, 100);
        if(r<=15){
            breakingItem();
        }
    }

    public void usingDevice(){
        logger.info(this.getType() + " sensor got event at "+ getHouse().getTime());
        generateReportForObserver();
    }

    public void generateReportForDay(){
        logger.info( getElectricityInOnState()*getUsingHours() + " electricity was used this day by" +this.getType());
        addUsedElectricity(getElectricityInOnState()*getUsingHours());
    }

    public void stopDevice(){
        if(this.getCurrentState().getType()== StateType.FIXING) {
            logger.info(this.getName() + " finally fixed at " + house.getTime());
            this.setCurrentState(new ActiveState(this));
            if(!isIsEnergyOn()){
                this.setCurrentState(new NonEnergyState(this));
                logger.info(this.getName() + " is setted to non energy state cause electricity is off at "+house.getTime());
            }
        }
        else if(this.getCurrentState().getType()== StateType.BROKEN) {
            logger.info(this.getName() + " broken at " + house.getTime());
        }
    }

    public boolean isIsEnergyOn() {
        return isEnergyOn;
    }

    public void setIsEnergyOn(boolean isEnergyOn) {
        Sensor.isEnergyOn = isEnergyOn;
        if(!isEnergyOn){
            setCurrentState(new NonEnergyState(this));
            logger.info(this.getName() + " is setted to non energy state cause electricity is off at "+house.getTime());
        }else{
            setCurrentState(new ActiveState(this));
            logger.info(this.getName() + " is setted to active state electricity is on at "+house.getTime());
        }
    }
}
