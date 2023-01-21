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
    private final boolean isForHuman;

    private ObjectState currentState = new IdleState(this);

    private int usingHours;
    private final Room currentRoom;
    private int electricityUsed =0;
    private final int electricityInOnState;
    private final int electricityInOffState;
    private final int electricityInBrokeState;
    private int usedTimes =0;
    private int brokenTimes =0;
    private Manual manual;
    private final House house = House.getInstance();


    public Device(DeviceType type, boolean isForHuman, int usingHours, Room currentRoom, int electricityInOnState, int electricityInOffState) {
        this.type = type;
        this.isForHuman = isForHuman;
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

    public boolean isForHuman() {
        return isForHuman;
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

    public void usingElectricity() {
        currentState.getElectricity();
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
    }

    public void fixingItem() {
        setUsingHours(12);
        setCurrentState(new FixingState(this));
    }

    public Manual getManual() {
        if (manual == null) manual = new Manual(this);
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

    public void stopDevice(LivingEntity e){
        if(e.getCurrentDevice().getCurrentState().getType()== StateType.ACTIVE) {
            System.out.println(e.getCurrentDevice().getType() + " switched off at " + house.getTime());
            e.getCurrentDevice().setCurrentState(new IdleState(e.getCurrentDevice()));
        }
        else if(e.getCurrentDevice().getCurrentState().getType()== StateType.FIXING) {
            System.out.println(e.getCurrentDevice().getType() + " is finally fixed at " + house.getTime());
            e.getCurrentDevice().setCurrentState(new IdleState(e.getCurrentDevice()));
        }
        else if(e.getCurrentDevice().getCurrentState().getType()== StateType.BROKEN) {
            System.out.println(e.getCurrentDevice().getType() + " is broken at  " + house.getTime());
        }
    }
}
