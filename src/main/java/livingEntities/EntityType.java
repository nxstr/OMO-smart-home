package livingEntities;

import items.sensors.SensorType;

import java.util.Objects;

public enum EntityType {
    CHILD("child"),
    DOG("dog"),
    CAT("cat"),
    FATHER("father"),
    MOTHER("mother");

    EntityType(String name) {
        this.name = name;
    }

    private final String name;

    public static EntityType getTypeByName(String name) {
        for (EntityType entity : EntityType.values()) {
            if (Objects.equals(entity.name, name)) return entity;
        }
        return null;
    }
}
