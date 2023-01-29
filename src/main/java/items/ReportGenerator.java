package items;

import events.Event;
import house.Floor;
import house.House;
import house.Room;
import items.device.Device;
import items.equipment.SportEquipment;
import items.sensors.Sensor;
import items.sensors.SensorType;
import items.sensors.WaterSensor;
import items.state.StateType;
import livingEntities.LivingEntity;
import strategy.Strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ReportGenerator {
    private FileWriter configuration = null;
    private static FileWriter eventReport = null;
    private static FileWriter activity = null;
    private static FileWriter consumption = null;
    private static FileWriter simulationRun = null;
    private static String filename = null;

    public ReportGenerator() {
    }

    public void setTrace(String name){
        try {
            if(filename==null){
                name = name.substring(0, name.indexOf(".json"));
                filename = name;
            }
            this.configuration = new FileWriter("src/main/resources/"+filename+"-HouseConfigurationReport.txt");
            eventReport = new FileWriter("src/main/resources/"+filename+"-EventReport.txt");
            activity = new FileWriter("src/main/resources/"+filename+"-ActivityReport.txt");
            consumption = new FileWriter("src/main/resources/"+filename+"-ConsumptionReport.txt");
            simulationRun = new FileWriter("src/main/resources/"+filename+"-SimulationRun.txt");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void houseConfigurationReport() throws IOException {
        House house = House.getInstance();
        configuration.write("House with id " + house.getHouseId() + " has one outsideArea and " + house.getFloors().size() + " floor(s):\n");
        for(Floor floor: house.getFloors()){
            configuration.write(" - Floor " + floor.getLevel() + " has " + floor.getRooms().size() + " room(s):\n");
            for(Room room: floor.getRooms()){
                configuration.write("  - Room named " + room.getName() + " has:\n");
                if(!room.getEquipment().isEmpty()){
                    configuration.write("   - " + room.getEquipment().size() + " equipment(s):\n");
                    configuration.write("    - ");
                    for(SportEquipment e: room.getEquipment()){
                        configuration.write(e.getType() + "; ");
                    }
                    configuration.write("\n");
                }
                if(room.getElectricalItems().stream().anyMatch(e-> Objects.equals(e.getMainType(), "device"))){
                    List<ElectricalItem> devices = room.getElectricalItems().stream().filter(e-> Objects.equals(e.getMainType(), "device")).toList();
                    configuration.write("   - " + devices.size() + " devices(s):\n");
                    configuration.write("    - ");
                    for(ElectricalItem e: devices){
                        configuration.write(e.getName() + "; ");
                    }
                    configuration.write("\n");
                }
                if(room.getElectricalItems().stream().anyMatch(e-> Objects.equals(e.getMainType(), "sensor"))){
                    List<ElectricalItem> sensors = room.getElectricalItems().stream().filter(e-> Objects.equals(e.getMainType(), "sensor")).toList();
                    configuration.write("   - " + sensors.size() + " sensor(s):\n");
                    configuration.write("    - ");
                    for(ElectricalItem e: sensors){
                        configuration.write(e.getName() + "; ");
                    }
                    configuration.write("\n");
                }
            }
        }
        configuration.write("There are " + house.getLivingEntities().size() + " living entities in the house:\n");
        for(LivingEntity e: house.getLivingEntities()){
            configuration.write(" - Entity " + e.getType() + " named " + e.getName() + " in the room " + e.getCurrentRoom().getName() + ";\n");
        }
        configuration.flush();
        configuration.close();
    }

    public void eventReport(Event event) throws IOException {
        eventReport.write("Event " + event.getType() + " is generated at " + event.getTime() + "\n");
        eventReport.flush();
    }


    public void eventSensorReport(Sensor sensor) throws IOException {
        eventReport.write("Sensor " + sensor.getType() + " handled event and generated report " + sensor.getHouse().getTime() + "\n");
        if(sensor.isAlarmMode()){
            eventReport.write("Alarm mode is on\n");
        }else{
            eventReport.write("Alarm mode is off\n");
        }
        if(sensor.getType() == SensorType.ELECTRICITY){
            if(sensor.isIsEnergyOn()){
                eventReport.write("Electricity is on!\n");
            }else{
                eventReport.write("There is no electricity in the house\n");
            }
        }
        if(sensor.getType() == SensorType.WATER){
            WaterSensor water = (WaterSensor) sensor;
            if(water.isWaterOn()){
                eventReport.write("Water is on!\n");
            }else{
                eventReport.write("There is no water in the house\n");
            }
        }
        eventReport.write("\n");
        eventReport.flush();
    }

    public void writeEventDay(int day) throws IOException {
        eventReport.write("\n---------------" + day + " DAY---------------\n\n");
        eventReport.flush();
    }

    public void writeActivityDay(int day) throws IOException {
        activity.write("\n---------------" + day + " DAY---------------\n\n");
        activity.flush();
    }

    public void entityActivityReport(LivingEntity entity, LocalTime time) {
        try{
            if(entity.getCurrentRoom()==null){
                activity.write(entity.getName() + " is outside the house at " + time + ". ");
                if(entity.getCurrentEq()!=null){
                    activity.write(entity.getName() + " is using " + entity.getCurrentEq().getType()+"\n");
                }
            }else{
                if(!entity.isAsleep()) {
                    activity.write(entity.getName() + " is in the " + entity.getCurrentRoom().getName() + " at " + time + ". ");
                    if (entity.getCurrentEq() != null) {
                        activity.write(entity.getName() + " is using " + entity.getCurrentEq().getType() + "\n");
                    } else if (entity.getCurrentDevice() != null) {
                        if(entity.getCurrentDevice().getCurrentState().getType()== StateType.ACTIVE) {
                            activity.write(entity.getName() + " is using " + entity.getCurrentDevice().getName() + "\n");
                        }else if(entity.getCurrentDevice().getCurrentState().getType()== StateType.FIXING) {
                            activity.write(entity.getName() + " is fixing " + entity.getCurrentDevice().getName() + "\n");
                        }
                    }else{
                        activity.write("\n");
                    }
                }else{
                    activity.write(entity.getName() + " is sleeping\n");
                }
            }
            activity.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void strategyReport(Device device, LocalTime time){
        try {

            if (device.getCurrentState().getType() == StateType.ACTIVE) {
                activity.write(device.getName() + " is switched on by system at " + time + " at room " + device.getCurrentRoom().getName() + "\n");
            }else if(device.getCurrentState().getType() == StateType.IDLE){
                activity.write(device.getName() + " is switched off by system at " + time + " at room " + device.getCurrentRoom().getName() + "\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void strategySetupReport(Strategy strategy){
        try{
            activity.write("It is " + strategy.getClass().getName() + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void consumptionReport(ElectricalItem item){
        try{
            consumption.write("\n" + item.getName() + " was broken " + item.getBrokenTimes() + " times\n");
            if(Objects.equals(item.getMainType(), "device")){
                Device d = (Device) item;
                consumption.write("\n" + item.getName() + " was used " + d.getUsedTimes() + " times\n");
            }
            consumption.write(item.getElectricityUsed() + " electricity was used by " + item.getName() + "\n\n");
            consumption.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void usageReport(SportEquipment item){
        try{
            consumption.write("\n"+item.getType() + " was used " + item.getUsedTimes() + " times\n\n");
            consumption.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeConsumptionDay(int day) throws IOException {
        consumption.write("\n---------------" + day + " DAY---------------\n\n");
        consumption.flush();
    }

    public void simulationRunLog(String s){
        try{
            simulationRun.write(s);
            simulationRun.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
