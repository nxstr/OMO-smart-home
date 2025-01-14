package events;

import items.Observer;

import java.time.LocalTime;
import java.util.Random;

public class FireAndTemperatureEventGenerator extends EventGenerator{
    private static FireAndTemperatureEventGenerator instance;
    public static EventGenerator getInstance() {
        if (instance == null){
            instance = new FireAndTemperatureEventGenerator();
        }
        return instance;
    }

    public void generateEvent(LocalTime time, Observer observer){
        super.generateEvent(time, observer);
        if(time.equals(getRandomEventTime())) {
            EventType[] arr = new EventType[]{
                    EventType.TEMPERATURE, EventType.FIRE
            };
            int rand = new Random().nextInt(arr.length);
            EventType type = arr[rand];
            int room = new Random().nextInt(getRoomFactory().getRooms().size());
            Event event = new EnvironmentEvent(type, getRoomFactory().getRooms().get(room), getRandomEventTime());
            observer.eventHandler(event);
            setRandomEventTime(null);
        }
    }
}
