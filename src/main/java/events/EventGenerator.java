package events;


import house.House;
import house.Room;
import house.RoomFactory;
import items.Observer;

import java.time.LocalTime;
import java.util.Random;

public class EventGenerator {
    private static EventGenerator instance = null;
    private EventType activeEventType;
    private RoomFactory roomFactory = RoomFactory.getInstance();
    private int eventTicks;
    private int currentTicks = 0;
    private LocalTime randomEventTime;

    public EventGenerator() {
    }

    public static EventGenerator getInstance() {
        if (instance == null){
            instance = new EventGenerator();
        }
        return instance;
    }

    public static void setInstance(EventGenerator instance) {
        EventGenerator.instance = instance;
    }

    public boolean checkTime(){
        if(currentTicks>=eventTicks) {
            return true;
        }
        currentTicks++;
        return false;
    }

    public void chooseRandomTime(LocalTime time){
        if(randomEventTime==null) {
            Random rand = new Random();
            int hours = rand.nextInt(23);
            randomEventTime = LocalTime.of(hours, 0);
//            System.out.println("CHOSEN EVENT TIME ======================== " + hours);
        }
    }

    public void generateEvent(LocalTime time, Observer observer){
        chooseRandomTime(time);
        if(time.equals(randomEventTime)) {
            if (activeEventType == null) {
                EventType[] arr = new EventType[]{
                        EventType.WATER, EventType.TEMPERATURE, EventType.FIRE
                };
                int rand = new Random().nextInt(arr.length);
                EventType type = arr[rand];
                if (type == EventType.WATER) {
                    activeEventType = type;
                    Event event = new Event(type, roomFactory.getRooms().get(0), randomEventTime);
                    observer.eventHandler(event);
                    eventTicks = new Random().nextInt(25);
                } else {
                    int room = new Random().nextInt(roomFactory.getRooms().size());
                    Event event = new Event(type, roomFactory.getRooms().get(room), randomEventTime);
                    observer.eventHandler(event);
                }
                randomEventTime = null;
            }
        }else if (activeEventType == EventType.WATER) {
            if (checkTime()) {
                for (Room r : roomFactory.getRooms()) {
                    Event event = new Event(activeEventType, r, time);
                    observer.eventHandler(event);
                }
                activeEventType = null;
                randomEventTime = null;
            }
        }
    }
}
