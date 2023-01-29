package items.device;

import house.Room;

import java.util.ArrayList;
import java.util.List;

public class DeviceFactory {

    private static DeviceFactory instance = null;
    private final List<Device> devices = new ArrayList<>();
    private final List<Device> petDevices = new ArrayList<>();
    private final List<Device> humanDevices = new ArrayList<>();
    private final List<Device> systemDevices = new ArrayList<>();

    public DeviceFactory() {
    }

    public static DeviceFactory getInstance() {
        if (instance == null){
            instance = new DeviceFactory();
        }
        return instance;
    }

    public void createDevice(Room room, DeviceType type){
        Device device = switch (type) {
            case COMPUTER -> new Computer(room);
            case PET_FOUNTAIN -> new PetFountain(room);
            case FRIDGE -> new Fridge(room);
            case GAME_CONSOLE -> new GameConsole(room);
            case COFFEE_MACHINE -> new CoffeeMachine(room);
            case VACUUM_CLEANER -> new VacuumCleaner(room);
            case PET_FEEDER -> new PetFeeder(room);
            case TV -> new Television(room);
            case DISHWASHER -> new Dishwasher(room);
            case WASHING_MACHINE -> new WashingMachine(room);
            case AIR_CONDITIONER -> new AirConditioner(room);
            case LOCK -> new Lock(room);
            case FIRE_SUPPRESSION -> new FireSuppression(room);
            default -> null;
        };
        if(device!=null) {
            room.addElectricalItem(device);
            devices.add(device);
            switch (device.getType()) {
                case TV, GAME_CONSOLE,
                        COMPUTER, FRIDGE -> humanDevices.add(device);
                case PET_FEEDER -> {
                    petDevices.add(device);
                    systemDevices.add(device);
                }
                case PET_FOUNTAIN -> petDevices.add(device);
                case LOCK, AIR_CONDITIONER, DISHWASHER,
                        VACUUM_CLEANER, WASHING_MACHINE, FIRE_SUPPRESSION -> systemDevices.add(device);
                default -> {
                    humanDevices.add(device);
                    systemDevices.add(device);
                }
            }
        }
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<Device> getPetDevices() {
        return petDevices;
    }

    public List<Device> getHumanDevices() {
        return humanDevices;
    }

    public List<Device> getSystemDevices() {
        return systemDevices;
    }
}
