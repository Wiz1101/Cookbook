package models.entities;

import java.util.UUID;

public abstract class BaseEntity {

    protected UUID id;

    public BaseEntity() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}