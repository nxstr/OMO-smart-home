package events;

import house.Room;

import java.time.LocalTime;

public class EntityEvent extends Event{

    public EntityEvent(Room room, LocalTime time) {
        super(EventType.ENTITY, room, time);
    }
}
