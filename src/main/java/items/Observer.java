package items;

import items.device.Device;
import items.equipment.SportEquipment;
import items.sensors.Sensor;
import items.state.ObjectState;
import items.state.StateType;
import livingEntities.Adult;
import livingEntities.EntityType;
import livingEntities.LivingEntity;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Observer {
    private static Observer instance = null;
    private List<Device> devices = new ArrayList<>();
    private List<SportEquipment> sports = new ArrayList<>();
    private List<Sensor> sensors = new ArrayList<>();
    private Strategy strategy;

    private Observer() {
    }

    public static Observer getInstance() {
        if (instance == null) instance = new Observer();
        return instance;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public static void setInstance(Observer instance) {
        Observer.instance = instance;
    }

    public void handleDeviceReport(Device device){
//        device.generateReportForDay();
        //generate task for adult
        Adult.addTask(device);
    }

    public void handleSportReport(SportEquipment equipment){
        //generate task for adult
    }

    public Device getFreeDevice(LivingEntity entity){
        List<Device> freeDevices = null;
        if(entity.getType()== EntityType.DOG || entity.getType()== EntityType.CAT){
            freeDevices = devices.stream()
                    .filter(device -> device.getCurrentState().getType() == StateType.IDLE).filter(device -> !device.isForHuman())
                    .collect(Collectors.toList());
        }else{
            freeDevices = devices.stream()
                    .filter(device -> device.getCurrentState().getType() == StateType.IDLE).filter(Device::isForHuman)
                    .collect(Collectors.toList());
            List<Device> nearestDevices = freeDevices.stream().filter(d -> d.getCurrentRoom()==entity.getCurrentRoom()).toList();
            if(!nearestDevices.isEmpty()){
                freeDevices = nearestDevices;
            }
        }
        if (freeDevices.isEmpty()) {
            return null;
        }

        int randomIndexOfList = new Random().nextInt(freeDevices.size());
        return freeDevices.get(randomIndexOfList);
    }

    public SportEquipment getFreeSport(){
        List<SportEquipment> freeSports = sports.stream()
                .filter(sport -> sport.getCurrentState().getType() == StateType.IDLE)
                .collect(Collectors.toList());
        if (freeSports.isEmpty()) {
            return null;
        }

        int randomIndexOfList = new Random().nextInt(freeSports.size());
        return freeSports.get(randomIndexOfList);
    }

    public void handleSensorReport(Sensor sensor){
        if(sensor.isAlarmMode()){
            System.out.println("!!!EmErGeNcY bEhAvIoR!!!");
        }
        else if(sensor.getCurrentState().getType() == StateType.BROKEN){
            //add task to adult
        }else{
//            sensor.getElectricityUsed();
            sensor.generateReportForDay();
        }
        System.out.println(sensor.getType() + " generated report");

    }

    public void eventHandler(){
        System.out.println("things dont go good, its time for EmErGeNcY bEhAvIoR");
    }
}
