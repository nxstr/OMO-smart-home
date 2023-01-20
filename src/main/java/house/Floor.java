package house;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final int level;
    private final List<Room> rooms = new ArrayList<>();

    public Floor(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

}
