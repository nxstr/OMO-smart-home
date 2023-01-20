package items.device;

import house.Room;
import items.state.*;
import items.*;

import java.time.LocalTime;
import java.util.Random;

public abstract class Device implements ElectricalItem{

    private final DeviceType type;
    private final boolean isForHuman;

    private ObjectState currentState = new IdleState(this);

    private final int usingHours;
    private final Room currentRoom;
    private int electricityUsed =0;
    private final int electricityInOnState;
    private final int electricityInOffState;
    private final int electricityInBrokeState;
    private int usedTimes =0;
    private int brokenTimes =0;
    private Manual manual;


    public Device(DeviceType type, boolean isForHuman, int usingHours, Room currentRoom, int electricityInOnState, int electricityInOffState) {
        this.type = type;
        this.isForHuman = isForHuman;
        this.usingHours = usingHours;
        this.currentRoom = currentRoom;
        this.electricityInOnState = electricityInOnState;
        this.electricityInOffState = electricityInOffState;
        this.electricityInBrokeState = electricityInOnState;
//        this.currentRoom.addElectricalItem(this);
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
//        if (currentState.getType() != StateType.ACTIVE) {
//            return currentState.getUsingHours();
//        }
        return usingHours;
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

    public void usingDevice(LocalTime time){
        usedTimes++;
        setCurrentState(new ActiveState(this));
        System.out.println(this.getName() + " is starting at " + time);
//        breakingEvent();
        generateReportForObserver();
    }

    public void breakingItem() {
        brokenTimes++;
        setCurrentState(new BrokenState(this));
        System.out.println(this.getName() + " is broke");
        generateReportForObserver();
    }

    public void fixingItem() {
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
        int r = new Random().nextInt(0, 100);
        if(r<=20){
            breakingItem();
        }
    }

    public void generateReportForDay(){
        int electricity = getElectricityInOnState()*getUsedTimes()+getElectricityInOffState()*(24*6-getUsingHours());
        System.out.println(electricity + " electricity was used this day by " + this.getType());
        addUsedElectricity(electricity);

    }
}
