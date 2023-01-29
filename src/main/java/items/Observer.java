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

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Observer {
    private static Observer instance = null;
    private final SensorFactory sensorFactory = SensorFactory.getInstance();
    private Strategy strategy;
    private final ReportGenerator reportGenerator = new ReportGenerator();

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

    public void handleDeviceReport(Device device){
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

    public void handleSportDayReport(SportEquipment equipment){
        reportGenerator.usageReport(equipment);
    }

    public void handleSensorReport(Sensor sensor){
        if(sensor.isAlarmMode() && sensor.getCurrentState().getType()!=StateType.BROKEN){
            logAction("!!!EmErGeNcY bEhAvIoR!!!\n");
            for(LivingEntity e: sensor.getHouse().getLivingEntities()){
                e.setAlarmMode(true);
            }
        }
        else if(sensor.getCurrentState().getType() == StateType.BROKEN){
            Adult.addTask(sensor);
        }else{
            sensor.generateReportForDay();
        }
        logAction(sensor.getType() + " generated report.\n");
        try{
            reportGenerator.eventSensorReport(sensor);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void eventHandler(Event event){
        logAction("Event " + event.getType() + " is generated at " + event.getTime() + "\n");
        try {
            reportGenerator.eventReport(event);
        }catch (IOException e){
            e.printStackTrace();
        }
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
        }
    }

    public void handleDayConsumptionReport(ElectricalItem item){
        reportGenerator.consumptionReport(item);
    }

    public void logAction(String s){
        reportGenerator.simulationRunLog(s);
    }
}
