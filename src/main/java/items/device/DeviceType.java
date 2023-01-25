package items.device;

import java.util.Objects;

public enum DeviceType {
    GAME_CONSOLE("game_console"),
    COFFEE_MACHINE("coffee_machine"),
    COMPUTER("computer"),
    FRIDGE("fridge"),
    PET_FOUNTAIN("pet_fountain"),
    PET_FEEDER("pet_feeder"),
    VACUUM_CLEANER("vacuum_cleaner"),
    TV("tv"),
    DISHWASHER("dishwasher"),
    WASHING_MACHINE("washing_machine"),
    AIR_CONDITIONER("air_conditioner"),
    LOCK("lock"),
    FIRE_SUPPRESSION("fire_suppression");

    private final String name;

    DeviceType(String name) {
        this.name = name;
    }

    public static DeviceType getTypeByName(String name) {
        for (DeviceType device : DeviceType.values()) {
            if (Objects.equals(device.name, name)) return device;
        }
        return null;
    }
}
