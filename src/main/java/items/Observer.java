package items;

import events.Event;
import events.EventType;
import items.device.Device;
import items.device.DeviceType;
import items.equipment.SportEquipment;
import items.sensors.Sensor;
import items.sensors.SensorFactory;
import items.sensors.SensorType;
import items.state.StateType;
import livingEntities.Adult;
import livingEntities.LivingEntity;
import strategy.Strategy;

import java.util.List;
import java.util.Objects;

public class Observer {
    private static Observer instance = null;
    private final SensorFactory sensorFactory = SensorFactory.getInstance();
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
        //generate report: used times, electricity used, etc.
        if(device.getType() == DeviceType.FIRE_SUPPRESSION && device.getCurrentState().getType() == StateType.IDLE){
            for(LivingEntity e: device.getHouse().getLivingEntities()){
                e.setAlarmMode(false);
            }
            for(Sensor e: sensorFactory.getSensors()){
                e.setAlarmMode(false);
            }
        }
        else if(device.getCurrentState().getType()==StateType.BROKEN) {
            Adult.addTask(device);
        }
    }

    public void handleSportReport(SportEquipment equipment){
        //generate report: used times, electricity used, etc.
    }

    public void handleSensorReport(Sensor sensor){
        if(sensor.isAlarmMode() && sensor.getCurrentState().getType()!=StateType.BROKEN){
            System.out.println("!!!EmErGeNcY bEhAvIoR!!! here");
            for(LivingEntity e: sensor.getHouse().getLivingEntities()){
                e.setAlarmMode(true);
            }
        }
        else if(sensor.getCurrentState().getType() == StateType.BROKEN){
            Adult.addTask(sensor);
        }else{
            sensor.generateReportForDay();
        }
        System.out.println(sensor.getType() + " generated report");

    }

    public void eventHandler(Event event){
        System.out.println("Event " + event.getType() + " is handled at " + event.getTime());
        List<Sensor> sensors;
        if(event.getType() == EventType.ENTITY){
            sensors = sensorFactory.getSensors().stream().filter(s->s.getType() == SensorType.ENTITY).toList();
        }else if(event.getType() == EventType.WATER){
            sensors = sensorFactory.getSensors().stream().filter(s->s.getType() == SensorType.WATER).toList();
        }else if(event.getType() == EventType.ELECTRICITY){
            sensors = sensorFactory.getSensors().stream().filter(s->s.getType() == SensorType.ELECTRICITY).toList();
        }
        else {
            sensors = sensorFactory.getSensors().stream()
                    .filter(s -> s.getCurrentRoom() == event.getRoom()).filter(s -> Objects.equals(s.getName(), event.getType().toString())).toList();
        }

        ElectricalItem sens = null;
        for(ElectricalItem s: sensors){
            if(Objects.equals(s.getName(), event.getType().toString())){
                sens = s;
            }
        }
        if(sens!=null){
            sens.usingDevice();
            System.out.println("at time " + event.getTime());
        }else{
            System.out.println("!!!EmErGeNcY bEhAvIoR!!!");
        }
    }
}
