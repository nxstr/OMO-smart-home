package events;

import house.RoomFactory;
import items.Observer;

import java.time.LocalTime;
import java.util.Random;

public abstract class EventGenerator {

    private final RoomFactory roomFactory = RoomFactory.getInstance();

    private LocalTime randomEventTime;

    public EventGenerator() {
    }



    public void chooseRandomTime(){
        if(getRandomEventTime()==null) {
            Random rand = new Random();
            int hours = rand.nextInt(23);
            setRandomEventTime(LocalTime.of(hours, 0));
        }
    }

    public LocalTime getRandomEventTime() {
        return randomEventTime;
    }

    public void setRandomEventTime(LocalTime randomEventTime) {
        this.randomEventTime = randomEventTime;
    }

    public RoomFactory getRoomFactory() {
        return roomFactory;
    }

    public void generateEvent(LocalTime time, Observer observer){
        chooseRandomTime();
    }
}
