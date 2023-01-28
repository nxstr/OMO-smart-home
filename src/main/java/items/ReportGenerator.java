package items;

import events.Event;
import house.Floor;
import house.House;
import house.Room;
import items.ElectricalItem;
import items.device.Device;
import items.equipment.SportEquipment;
import items.sensors.Sensor;
import livingEntities.LivingEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {
    private FileWriter configuration = null;
    private static FileWriter eventReport = null;

    public ReportGenerator() {
        try {
            this.configuration = new FileWriter("src/main/resources/HouseConfigurationReport.txt");
            eventReport = new FileWriter("src/main/resources/EventReport.txt");
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
                    for(SportEquipment e: room.getEquipment()){
                        configuration.write("    - " + e.getType() + ";\n");
                    }
                }
                if(room.getElectricalItems().stream().anyMatch(e->e.getMainType()=="device")){
                    List<ElectricalItem> devices = room.getElectricalItems().stream().filter(e->e.getMainType()=="device").toList();
                    configuration.write("   - " + devices.size() + " devices(s):\n");
                    for(ElectricalItem e: devices){
                        configuration.write("    - " + e.getName() + ";\n");
                    }
                }
                if(room.getElectricalItems().stream().anyMatch(e->e.getMainType()=="sensor")){
                    List<ElectricalItem> sensors = room.getElectricalItems().stream().filter(e->e.getMainType()=="sensor").toList();
                    configuration.write("   - " + sensors.size() + " sensor(s):\n");
                    for(ElectricalItem e: sensors){
                        configuration.write("    - " + e.getName() + ";\n");
                    }
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
        eventReport.write("Event " + event.getType() + " is handled at " + event.getTime() + "\n");
        eventReport.flush();
    }


    public void eventSensorReport(Sensor sensor) throws IOException {
        eventReport.write("Sensor " + sensor.getType() + " handled event and generated report " + sensor.getHouse().getTime() + "\n");
        if(sensor.isAlarmMode()){
            eventReport.write("Alarm mode is on\n");
        }else{
            eventReport.write("Alarm mode is off\n");
        }
        eventReport.flush();
    }
}
