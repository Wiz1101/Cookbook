package services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Recipe;
import util.exceptions.recipe.InvalidRecipeDescriptionLengthException;
import util.exceptions.recipe.InvalidRecipeIngredientsCountException;
import util.exceptions.recipe.InvalidRecipeInstructionsLengthException;
import util.exceptions.recipe.InvalidRecipeNameLengthException;
import util.exceptions.recipe.InvalidRecipeServingSizeException;
import util.exceptions.recipe.InvalidRecipeTagsCountException;

public interface RecipeService {
    public List<Recipe> getAllRecipes();

    public Recipe getRecipeById(UUID id);

    public List<Recipe> getRecipesWithNameLike(String name);

    public List<Recipe> getRecipesFiltered(Set<String> filters);

    public String addRecipe(UUID recipeId, String name, String picturePath, String description,
    String instructions, UUID authorId,
    Map<Ingredient, Float> ingredients, byte servingSize) throws InvalidRecipeNameLengthException, InvalidRecipeDescriptionLengthException, InvalidRecipeInstructionsLengthException, InvalidRecipeServingSizeException, InvalidRecipeTagsCountException, InvalidRecipeIngredientsCountException;

    public void addRecipeIngredients(UUID recipeId, Map<Ingredient, Float> selectedIngredients);
    
    public List<Comment> getCommentsByRecipeId(UUID recipeId);
    
    public String editRecipeName(UUID recipeId, String name) throws InvalidRecipeNameLengthException;

    public String editRecipeDescription(UUID recipeId, String description) throws InvalidRecipeDescriptionLengthException;

    public String editRecipeInstructions(UUID recipeId, String instructions) throws InvalidRecipeInstructionsLengthException;

    public String editRecipeServingSize(UUID recipeId, byte servingSize) throws InvalidRecipeServingSizeException;

    public String removeRecipe(UUID recipeId);

    public void validateName(String name) throws InvalidRecipeNameLengthException;

    public void validateDescription(String description) throws InvalidRecipeDescriptionLengthException;

    public void validateInstructions(String instructions) throws InvalidRecipeInstructionsLengthException;

    public void validateIngredients(Map<Ingredient, Float> ingredients) throws InvalidRecipeIngredientsCountException;

    public void validateServingSize(byte servingSize) throws InvalidRecipeServingSizeException;

    public Comment getCommentById(UUID commentId);

    public void addTagToRecipe(UUID tagId, UUID recipeId);

    public void removeTagFromRecipe(UUID recipeId, UUID tagId);

    public void editRecipeImage(UUID recipeId, String picturePath);
}
