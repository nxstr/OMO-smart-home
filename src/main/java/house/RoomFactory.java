package house;

import items.device.DeviceFactory;

import java.util.ArrayList;
import java.util.List;

public class RoomFactory {
    private static RoomFactory instance = null;
    private final List<Room> rooms = new ArrayList<>();

    public RoomFactory() {
    }

    public static RoomFactory getInstance() {
        if (instance == null){
            instance = new RoomFactory();
        }
        return instance;
    }

    public Room create(String name, Floor floor){
        Room room = new Room(name, floor);
        rooms.add(room);
        return room;
    }

    public Room findRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equals(name)) return room;
        }
        return null;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
