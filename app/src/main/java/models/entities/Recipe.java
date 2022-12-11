package models.entities;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import util.constants.SuccessMessages;
import javafx.scene.image.Image;

public class Recipe extends BaseEntity {

    private String name;
    private Image picture;
    private String description;
    private String instructions;
    private UUID author;
    private List<UUID> tags;
    private Map<UUID, Float> ingredients;
    private byte servingSize;
    private List<UUID> comments;


    public Recipe(UUID id, String name, Image picture, String description,
    String instructions, UUID author, List<UUID> tags,
    Map<UUID, Float> ingredients, List<UUID> comments, byte servingSize) {
        super.id = id;
        setName(name);
        setPicture(picture);
        setDescription(description);
        setInstructions(instructions);
        setAuthor(author);
        setTags(tags);
        setIngredients(ingredients);
        setComments(comments);
        setServingSize(servingSize);
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public Image getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public UUID getAuthor() {
        return author;
    }

    public List<UUID> getTags() {
        return tags;
    }

    public Map<UUID, Float> getIngredients() {
        return ingredients;
    }

    public byte getServingSize() {
        return servingSize;
    }

    public List<UUID> getComments() {
        return comments;
    }
    
    

    // SETTERS
    public String setName(String name) {
            this.name = name;
            return String.format(SuccessMessages.RECIPE_SET_NAME, name);
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String setDescription(String description) {
            this.description = description;
            return String.format(SuccessMessages.RECIPE_SET_DESC);
    }

    public String setInstructions(String instructions) {
            this.instructions = instructions;
            return String.format(SuccessMessages.RECIPE_SET_INSTRUCTIONS);
    }

    public String setTags(List<UUID> tags) {
            this.tags = tags;
            return String.format(SuccessMessages.RECIPE_SET_TAGS);
    }

    public String setIngredients(Map<UUID, Float> ingredients) {
            this.ingredients = ingredients;
            return String.format(SuccessMessages.RECIPE_SET_INGREDIENTS);
    }

    public String setServingSize(byte servingSize) {
            this.servingSize = servingSize;
            return String.format(SuccessMessages.RECIPE_SET_SERVING_SIZE, servingSize);
    }

    private String setAuthor(UUID author) {
            this.author = author;
            return String.format(SuccessMessages.RECIPE_SET_AUTHOR);
    }
    
    private void setComments(List<UUID> comments) {
        this.comments = comments;
    }
}
