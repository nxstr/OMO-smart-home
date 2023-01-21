package items.equipment;

import java.util.Objects;

public enum SportEquipmentType {
    BICYCLE("bicycle"),
    SKI("ski"),
    PET_TOY("pet_toy");

    private final String name;

    SportEquipmentType(String name) {
        this.name = name;
    }

    public static SportEquipmentType getTypeByName(String name) {
        for (SportEquipmentType eq : SportEquipmentType.values()) {
            if (Objects.equals(eq.name, name)) return eq;
        }
        return null;
    }
}
