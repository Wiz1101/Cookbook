package models.entities;

import java.util.UUID;

import util.constants.SuccessMessages;

public class Ingredient extends BaseEntity {

    private String name;
    private String unit;

    public Ingredient(UUID id, String name, String unit) {
        super.id = id;
        setName(name);
        setUnit(unit);
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    // SETTERS
    private String setName(String name) {
        this.name = name;
        return String.format(SuccessMessages.INGREDIENT_SET_NAME, name);
    }

    private String setUnit(String unit) {
        this.unit = unit;
        return String.format(SuccessMessages.INGREDIENT_SET_UNIT, unit);
    }
}
