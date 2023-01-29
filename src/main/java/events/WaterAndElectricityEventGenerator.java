package events;

import items.Observer;

import java.time.LocalTime;
import java.util.Random;

    public class WaterAndElectricityEventGenerator extends EventGenerator{
    private static WaterAndElectricityEventGenerator instance;
    private int eventTicks;
    private int currentTicks = 0;


    private Event event;

    public static EventGenerator getInstance() {
        if (instance == null){
            instance = new WaterAndElectricityEventGenerator();
        }
        return instance;
    }


    public boolean checkTime(){
        if(currentTicks>=eventTicks) {
            return true;
        }
        currentTicks++;
        return false;
    }

    public void generateEvent(LocalTime time, Observer observer){
        super.generateEvent(time, observer);
        if(time.equals(getRandomEventTime())) {
            if (event == null) {
                EventType[] arr = new EventType[]{
                        EventType.ELECTRICITY, EventType.WATER
                };
                int rand = new Random().nextInt(arr.length);
                EventType type = arr[rand];
                event = new EnvironmentEvent(type, getRoomFactory().getRooms().get(new Random().nextInt(getRoomFactory().getRooms().size())), getRandomEventTime());
                observer.eventHandler(event);
                eventTicks = new Random().nextInt(25);
                setRandomEventTime(null);
            }
        }else if(event!=null){
            if (checkTime()) {
                    event = new EnvironmentEvent(event.getType(), getRoomFactory().getRooms().get(new Random().nextInt(getRoomFactory().getRooms().size())), time);
                    observer.eventHandler(event);
                event = null;
                setRandomEventTime(null);
            }
        }
    }
}
