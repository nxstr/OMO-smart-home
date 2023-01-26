package events;

import house.Room;

import java.time.LocalTime;

public abstract class Event {
    private final EventType type;
    private final Room room;
    private final LocalTime time;

    public Event(EventType type, Room room, LocalTime time) {
        this.type = type;
        this.room = room;
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public Room getRoom() {
        return room;
    }

    public LocalTime getTime() {
        return time;
    }
}
