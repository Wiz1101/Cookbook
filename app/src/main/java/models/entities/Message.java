package models.entities;

import java.util.UUID;
import util.common.Validator;
import util.constants.FailMessages;
import util.constants.SuccessMessages;
import util.constants.Variables;
import util.exceptions.common.InvalidInstanceException;
import util.exceptions.common.InvalidLengthException;

public class Message extends BaseEntity {
    private UUID senderId;
    private UUID receiverId;
    private String text;
    private boolean isRead;
    private UUID recipeId;
    // TODO: Date, so we can sort by date received

    // Import an existing Message
    public Message(UUID id, UUID senderId, UUID receiverId, String text, boolean isRead, UUID recipeId) {
        super.id = id;
        setSender(senderId);
        setReceiver(receiverId);
        setText(text);
        this.isRead = isRead;
        setRecipeId(recipeId);
    }

    // OPERATIONS

    
    // GETTERS
    public UUID getSender() {
        return senderId;
    }

    public UUID getReceiver() {
        return receiverId;
    }

    public String getText() {
        isRead = true;
        return text;
    }

    public boolean isRead() {
        return isRead;
    }

    public UUID getRecipeId() {
        return recipeId;
    }


    // SETTERS
    private String setSender(UUID senderId) {
        try {
            validateUser(senderId);
            this.senderId = senderId;
            return String.format(SuccessMessages.MESSAGE_SET_SENDER);
        } catch (InvalidInstanceException e) {
            return String.format(FailMessages.MESSAGE_INVALID_SENDER);
        }
    }

    private String setReceiver(UUID receiverId) {
        try {
            validateUser(receiverId);
            this.receiverId = receiverId;
            return String.format(SuccessMessages.MESSAGE_SET_RECEIVER);
        } catch (InvalidInstanceException e) {
            return String.format(FailMessages.MESSAGE_INVALID_RECEIVER);
        }
    }

    private String setText(String text) {
        try {
            validateText(text);
            this.text = text;
            return String.format(SuccessMessages.MESSAGE_SET_TEXT);
        } catch (InvalidLengthException e) {
            return String.format(FailMessages.MESSAGE_INVALID_TEXT_LENGTH);
        }
    }

    private void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }



    // VALIDATORS
    private void validateText(String text) throws InvalidLengthException {
        Validator.validateStringLength(text, Variables.MIN_MESSAGE_TEXT_LENGTH,
                Variables.MAX_MESSAGE_TEXT_LENGTH);
    }

    private void validateUser(UUID userId) throws InvalidInstanceException {
        Validator.validateUser(userId);
    }
}
