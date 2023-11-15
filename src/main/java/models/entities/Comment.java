package models.entities;

import java.util.UUID;
import util.constants.SuccessMessages;

public class Comment extends BaseEntity {
    private UUID user;
    private String text;
    private UUID recipeId;
    // TODO: Date, so we can sort by date posted


    public Comment(UUID id, UUID user, UUID recipeId, String text) {
        super.id = id;
        setUser(user);
        setText(text);
        setRecipe(recipeId);
    }

    // GETTERS
    public UUID getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public UUID getRecipe() {
        return this.recipeId;
    }

    // SETTERS
    public String setText(String text) {
        this.text = text;
        return String.format(SuccessMessages.COMMENT_SET_TEXT);
    }

    private String setUser(UUID user) {
        this.user = user;
        return String.format(SuccessMessages.COMMENT_SET_USER);
    }

    private String setRecipe(UUID recipeId) {
        this.recipeId = recipeId;
        return String.format(SuccessMessages.COMMENT_SET_RECIPE);
    }
}
