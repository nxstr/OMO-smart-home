package events;

import house.Room;

import java.time.LocalTime;

public class EnvironmentEvent extends Event{

    public EnvironmentEvent(EventType type, Room room, LocalTime time) {
        super(type, room, time);
    }
}