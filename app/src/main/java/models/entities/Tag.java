package models.entities;

import java.util.UUID;

import util.constants.SuccessMessages;

public class Tag extends BaseEntity {

    private String name;
    private UUID user;

    public Tag(UUID id, String name, UUID userId) {
        super.id = id;
        setName(name);
        setUser(userId);
    }

    public UUID getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    private String setName(String name) {
            this.name = name;
            return String.format(SuccessMessages.TAG_SET_NAME, name);
    }

    public void setUser(UUID userId) {
        user = userId;
    }

    // private void validateName(String name) throws InvalidLengthException {
    //     Validator.validateStringLength(name,
    //             Variables.MIN_TAG_NAME_LENGTH, Variables.MAX_TAG_NAME_LENGTH);
    // }

}
