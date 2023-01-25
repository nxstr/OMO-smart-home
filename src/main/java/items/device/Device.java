package items.device;

import house.House;
import house.Room;
import items.state.*;
import items.*;
import livingEntities.LivingEntity;

import java.time.LocalTime;
import java.util.Random;

public abstract class Device implements ElectricalItem{

    private final DeviceType type;

    private ObjectState currentState = new IdleState(this);

    private int usingHours;
    private final Room currentRoom;
    private int electricityUsed =0;
    private final int electricityInOnState;
    private final int electricityInOffState;
    private final int electricityInBrokeState;
    private int usedTimes =0;
    private int brokenTimes =0;
    private final Manual manual = new Manual(this);
    private final House house = House.getInstance();


    public Device(DeviceType type, int usingHours, Room currentRoom, int electricityInOnState, int electricityInOffState) {
        this.type = type;
        this.usingHours = usingHours;
        this.currentRoom = currentRoom;
        this.electricityInOnState = electricityInOnState;
        this.electricityInOffState = electricityInOffState;
        this.electricityInBrokeState = electricityInOnState;
    }

    public House getHouse() {
        return house;
    }

    public String getName(){
        return getType().name();
    }

    public String getMainType(){
        return "device";
    }

    public ObjectState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ObjectState currentState) {
        this.currentState = currentState;
    }

    public DeviceType getType() {
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
    } //add to report when broken?

    public void setElectricityUsed(int electricityUsed) {
        this.electricityUsed = electricityUsed;
    }

    public int getElectricityInOnState() {
        return electricityInOnState;
    }

    public int getElectricityInOffState() {
        return electricityInOffState;
    }

    public int getElectricityInBrokeState() {
        return electricityInBrokeState;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
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

    public void resetUsedTimes(){
        this.usedTimes = 0;
    }

    public void resetBrokenTimes(){
        this.brokenTimes = 0;
    }

    public void addUsedElectricity(int electricity) {
        electricityUsed += electricity;
    }

    public void usingDevice(){
        usedTimes++;
        setCurrentState(new ActiveState(this));
        System.out.println(this.getName() + " is starting at " + house.getTime());
        breakingEvent();
        generateReportForObserver();
    }

    public void breakingItem() {
        brokenTimes++;
        setCurrentState(new BrokenState(this));
        System.out.println(this.getName() + " " + System.identityHashCode(this) + " is -----BROKE----- at " + house.getTime());
        generateReportForObserver();
        resetUsedTimes();
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
        Observer.getInstance().handleDeviceReport(this);
    }

    public void breakingEvent(){
        int r = new Random().nextInt(100);
        if(r<=5){
            breakingItem();
        }
    }

    public void generateReportForDay(){
        int electricity = getElectricityInOnState()*getUsedTimes()+getElectricityInOffState()*(24*6-getUsingHours());
        System.out.println(electricity + " electricity was used this day by " + this.getType());
        addUsedElectricity(electricity);

    }

    public void stopDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            System.out.println(this.getName() + " switched off at " + house.getTime());
            this.setCurrentState(new IdleState(this));
        }
        else if(this.getCurrentState().getType()== StateType.FIXING) {
            System.out.println(this.getName() + " is finally fixed at " + house.getTime());
            this.setCurrentState(new IdleState(this));
        }
        else if(this.getCurrentState().getType()== StateType.BROKEN) {
            System.out.println(this.getName() + " is broken at  " + house.getTime());
        }
    }
}
