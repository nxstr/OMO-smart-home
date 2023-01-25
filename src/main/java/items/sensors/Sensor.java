package items.sensors;

import house.House;
import house.Room;
import items.ElectricalItem;
import items.Observer;
import items.device.DeviceFactory;
import items.device.Manual;
import items.state.*;
import livingEntities.LivingEntity;

import java.time.LocalTime;
import java.util.Random;

public abstract class Sensor implements ElectricalItem {
    private final SensorType type;
    private ObjectState currentState = new ActiveState(this);
    private boolean alarmMode = false;


    private int usingHours = 24*6;
    private final Room currentRoom;
    private int electricityUsed =0;
    private final int electricityInOnState;
    private final int electricityInBrokeState;
    private int brokenTimes =0;
    private final Manual manual = new Manual(this);
    private final House house = House.getInstance();

    public Sensor(SensorType type, Room currentRoom, int electricityInOnState, int electricityInBrokeState) {
        this.type = type;
        this.currentRoom = currentRoom;
        this.electricityInOnState = electricityInOnState;
        this.electricityInBrokeState = electricityInBrokeState;
    }

    public House getHouse() {
        return house;
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

    public void resetBrokenTimes(){
        this.brokenTimes = 0;
    }

    public void addUsedElectricity(int electricity) {
        electricityUsed += electricity;
    }

    public void usingElectricity() {
        currentState.getElectricity();
    }

    public void breakingItem() {
        System.out.println( getType() + " Sensor is broken");
        brokenTimes++;
        setCurrentState(new BrokenState(this));
        addUsedElectricity(getElectricityInOnState()*getUsingHours());
        System.out.println(getElectricityInOnState()*getUsingHours() + " electricity was used this day before breaking");
        generateReportForObserver();
    }

    public void fixingItem() {
        setUsingHours(12);
        setCurrentState(new FixingState(this));
        Manual manual = getManual();
        manual.readDeviceManual();
        resetBrokenTimes();
        resetElectricity();
    }

    public Manual getManual() {
        return manual;
    }

    public void generateReportForObserver(){
        Observer.getInstance().handleSensorReport(this);
    }

    public void breakingEvent(){
        int r = new Random().nextInt(0, 100);
        if(r<=15){
            breakingItem();
        }
    }

    public void usingDevice(){
        System.out.println(this.getType() + "sensor got event");
        generateReportForObserver();
    }

    public void generateReportForDay(){
        System.out.println(getElectricityInOnState()*getUsingHours() + " electricity was used this day");
        addUsedElectricity(getElectricityInOnState()*getUsingHours());
    }

    public void stopDevice(){
        if(this.getCurrentState().getType()== StateType.FIXING) {
            System.out.println(this.getName() + " is finally fixed at " + house.getTime());
            this.setCurrentState(new ActiveState(this));
        }
        else if(this.getCurrentState().getType()== StateType.BROKEN) {
            System.out.println(this.getName() + " is broken at  " + house.getTime());
        }
    }
}
