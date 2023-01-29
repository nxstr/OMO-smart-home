package items.device;

import house.House;
import house.Room;
import items.state.*;
import items.*;

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
    private Manual manual;
    private final House house = House.getInstance();

    private static boolean isWaterOn = true;
    private static boolean isEnergyOn = true;


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
        Observer.getInstance().logAction(this.getName() + " is starting at " + house.getTime() + "\n");
        breakingEvent();
        generateReportForObserver();
    }

    public void breakingItem() {
        brokenTimes++;
        setCurrentState(new BrokenState(this));
        Observer.getInstance().logAction(this.getName() + " is broke at " + house.getTime() + "\n");
        generateReportForObserver();
        resetUsedTimes();
    }

    public void fixingItem() {
        setUsingHours(12);
        setCurrentState(new FixingState(this));
        Manual manual = getManual();
        manual.readDeviceManual();
        resetElectricity();
    }

    public Manual getManual() {
        if(manual==null){ manual = new Manual(this);}
        return manual;
    }

    public void generateReportForObserver(){
        observer.handleDeviceReport(this);
    }

    public void breakingEvent(){
        int r = new Random().nextInt(100);
        if(r<=5){
            breakingItem();
        }
    }

    public void generateReportForDay(){
        int electricity = (getElectricityInOnState()*getUsingHours()+getElectricityInOffState()*(24*6-getUsingHours()))*(getHouse().getDay());
        setElectricityUsed(electricity);
        Observer.getInstance().logAction(electricity + " electricity was used this day by " + this.getType() + "\n");
        observer.handleDayConsumptionReport(this);
    }

    public void stopDevice(){
        if(this.getCurrentState().getType()== StateType.ACTIVE) {
            Observer.getInstance().logAction(this.getName() + " switched off at " + house.getTime() + "\n");
            changeState();
        }
        else if(this.getCurrentState().getType()== StateType.FIXING) {
            Observer.getInstance().logAction(this.getName() + " is finally fixed at " + house.getTime()+ "\n");
            changeState();
        }
        else if(this.getCurrentState().getType()== StateType.BROKEN) {
            Observer.getInstance().logAction(this.getName() + " is broken at  " + house.getTime()+ "\n");
        }else if(this.getCurrentState().getType()== StateType.NON_ENERGY){
            changeState();
        }
    }

    public void changeState(){
        this.setCurrentState(new IdleState(this));
        if(!isIsWaterOn() && (this.getType()==DeviceType.DISHWASHER || this.getType()==DeviceType.WASHING_MACHINE || this.getType()==DeviceType.FIRE_SUPPRESSION)){
            this.setCurrentState(new NonEnergyState(this));
        }
        if(!isIsEnergyOn()){
            this.setCurrentState(new NonEnergyState(this));
        }
    }

    public static boolean isIsWaterOn() {
        return isWaterOn;
    }

    public void setIsWaterOn(boolean isWaterOn) {
        Device.isWaterOn = isWaterOn;
        if(!isWaterOn){
            setCurrentState(new NonEnergyState(this));
        }else{
            setCurrentState(new IdleState(this));
        }
    }

    public static boolean isIsEnergyOn() {
        return isEnergyOn;
    }

    public void setIsEnergyOn(boolean isEnergyOn) {
        Device.isEnergyOn = isEnergyOn;
        if(!isEnergyOn){
            setCurrentState(new NonEnergyState(this));
        }else{
            setCurrentState(new IdleState(this));
        }
    }
}
