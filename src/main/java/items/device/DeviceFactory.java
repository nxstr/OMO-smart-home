package items.device;

import house.Room;

import java.util.ArrayList;
import java.util.List;

public class DeviceFactory {

    private static DeviceFactory instance = null;
    private final List<Device> devices = new ArrayList<>();

    public DeviceFactory() {
    }

    public static DeviceFactory getInstance() {
        if (instance == null){
            instance = new DeviceFactory();
        }
        return instance;
    }

    public Device createDevice(Room room, DeviceType type){
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
            default -> null;
        };

        room.addElectricalItem(device);
        devices.add(device);
        return device;
    }

    public Device findDeviceByName(String name) {
        for (Device device : devices) {
            if (device.getName().equals(name)) return device;
        }
        return null;
    }

    public Device findDeviceByType(DeviceType name) {
        for (Device device : devices) {
            if (device.getType().equals(name)) return device;
        }
        return null;
    }

    public List<Device> getDevices() {
        return devices;
    }
}