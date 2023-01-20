import events.Event;
import events.EventHandler;
import events.EventType;
import house.Floor;
import house.House;
import house.Room;
import house.RoomFactory;
import items.Observer;
import items.device.AirConditioner;
import items.device.Device;
import items.device.DeviceFactory;
import items.device.DeviceType;
import items.sensors.TemperatureSensor;
import items.state.IdleState;
import livingEntities.Adult;
import livingEntities.EntityType;
import livingEntities.Person;
import strategy.Morning;
import strategy.Night;
import strategy.Strategy;

import java.time.LocalTime;

public class Simulation {
    private int hours = 0;
    private int days = 0;
    private final int interactionCount; //1 = 10 min
    private LocalTime time;
    private final House.HouseBuilder house = House.newBuilder();
    private final RoomFactory roomFactory = RoomFactory.getInstance();
    private final DeviceFactory deviceFactory = DeviceFactory.getInstance();
    private Strategy strategy;
    private Observer observer = Observer.getInstance();
    public Simulation(int interactionCount, LocalTime time) {
        this.interactionCount = interactionCount;
        this.time = time;
    }

    public void start(){
        Floor floor = new Floor(0);
        house.addFloor(floor);
        Room room = roomFactory.create("Bedroom", floor);
        floor.addRoom(room);
        TemperatureSensor sensor = new TemperatureSensor(room);
        room.addElectricalItem(sensor);
        Adult dad = new Adult("Bob", EntityType.ADULT, 30, room);
        house.addLivingEntity(dad);
        Device condit = deviceFactory.createDevice(room, DeviceType.AIR_CONDITIONER);
        Device coffee = deviceFactory.createDevice(room, DeviceType.COFFEE_MACHINE);
        Device tv = deviceFactory.createDevice(room, DeviceType.TV);
        Device feeder = deviceFactory.createDevice(room, DeviceType.PET_FEEDER);
//        AirConditioner condit = new AirConditioner(room);
        room.addElectricalItem(condit);
        room.addElectricalItem(coffee);
        room.addElectricalItem(tv);
        room.addElectricalItem(feeder);
        for(int i=0; i<interactionCount; i++){
            time = time.plusMinutes(10);
            if(time.getMinute()==0){
                hours = time.getHour();
                System.out.println(hours + " hours");
                if(hours==8){
                    strategy = new Morning();
                    observer.setStrategy(strategy);
                }
                if(hours==23){
                    hours=0;
                    days++;
                    strategy = new Night();
                    observer.setStrategy(strategy);
                    System.out.println(days + " days");
                    Event event = new Event(EventType.TEMPERATURE, room, time);
                    EventHandler e = new EventHandler(event);
                    e.notifySystem();
                }
            }
            if(strategy!=null) {
                if (!strategy.getActiveDevices().isEmpty()) {
                    for (Device d : strategy.getActiveDevices()) {
                        if (strategy.getCurrentBackActionProgress() == d.getUsingHours()) {
                            d.setCurrentState(new IdleState(d));
                            System.out.println(d.getType() + " switched off at " + time);
                        }
                    }
                    strategy.increaseCBAP();
                }
                if (strategy.getActiveDevices().isEmpty() && strategy.getCurrentBackActionProgress()!=0) {
                    strategy.stopBackAction();
                }
            }
        }
    }
}
