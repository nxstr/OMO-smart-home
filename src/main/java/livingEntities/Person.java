package livingEntities;

import house.Room;

public abstract class Person implements LivingEntity{
    private final String name;
    private Room room;
    private final EntityType type;
    private final int age;
    private Room prevRoom;
    private int currentBackActionProgress = 0;


    public Person(String name, EntityType type, int age, Room room) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.room = room;
        this.prevRoom = null;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    public Room getPrevRoom(){
        return prevRoom;
    }

    @Override
    public void waiting() {
        System.out.println("Person: " + name + " is waiting for now");
    }

    @Override
    public void moveToRoom(Room room){
        if (this.room == room) {
            return;
        }
        this.prevRoom = this.room;
        this.room = room;
        System.out.println(this.name + " moves to " + this.room.getName());
    }

    @Override
    public void goOut(){
        this.prevRoom = this.room;
        this.room = null;
        house.goOut(this);

    }

    @Override
    public void comeBack(){
        house.comeBack(this);
        this.room = this.prevRoom;
        this.prevRoom = null;
        System.out.println(this.name + " comes back to house ");
    }

    @Override
    public Room getCurrentRoom(){
        return this.room;
    }

    public int getCurrentBackActionProgress(){
        return currentBackActionProgress;
    }
    public void increaseCBAP(){
        currentBackActionProgress++;
    }

    public void stopBackAction() {
        this.currentBackActionProgress = 0;
//        System.out.println("Person switched off the devices");
    }
}
