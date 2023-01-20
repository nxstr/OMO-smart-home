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
import strategy.*;

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
        DeviceType[] arr = new DeviceType[]{
                DeviceType.COFFEE_MACHINE, DeviceType.DISHWASHER, DeviceType.AIR_CONDITIONER,
                DeviceType.PET_FEEDER, DeviceType.TV, DeviceType.VACUUM_CLEANER, DeviceType.WASHING_MACHINE
        };
        for(DeviceType type:arr){
            deviceFactory.createDevice(room, type);
        }
        for(int i=0; i<interactionCount; i++){
            time = time.plusMinutes(10);
            dad.findActivity();
            if(time.getMinute()==0){
                hours = time.getHour();
                System.out.println(hours + " hours");
                if(hours==8){
                    strategy = new Morning();
                    observer.setStrategy(strategy);
                }
                if(hours==14){
                    strategy = new Afternoon();
                    observer.setStrategy(strategy);
                }
                if(hours==19){
                    strategy = new Evening();
                    observer.setStrategy(strategy);
                }
                if(hours==0){
                    days++;
                    System.out.println(days + " days");
                    strategy = new Night();
                    observer.setStrategy(strategy);

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
                            if(d.getType()== DeviceType.DISHWASHER || d.getType()== DeviceType.WASHING_MACHINE){
                                Adult.addTask(d);
                            }
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
