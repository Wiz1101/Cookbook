package services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Recipe;
import services.RecipeService;
import util.common.DbContext;
import util.common.Validator;
import util.constants.SuccessMessages;
import util.constants.Variables;
import util.exceptions.common.InvalidCountException;
import util.exceptions.common.InvalidLengthException;
import util.exceptions.recipe.InvalidRecipeDescriptionLengthException;
import util.exceptions.recipe.InvalidRecipeIngredientsCountException;
import util.exceptions.recipe.InvalidRecipeInstructionsLengthException;
import util.exceptions.recipe.InvalidRecipeNameLengthException;
import util.exceptions.recipe.InvalidRecipeServingSizeException;
import util.exceptions.recipe.InvalidRecipeTagsCountException;

public class RecipeServiceImpl implements RecipeService {

    private DbContext dbContext;

    public RecipeServiceImpl() {
        super();
        dbContext = new DbContext(Variables.DATABASE_PORT, Variables.DATABASE_USER, Variables.DATABASE_PASS);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return dbContext.getAllRecipes();
    }

    @Override
    public Recipe getRecipeById(UUID id) {
        return dbContext.getRecipeById(id);
    }

    @Override
    public List<Recipe> getRecipesFiltered(Set<String> filters) {
        List<Recipe> allRecipes = dbContext.getAllRecipes();
        List<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            boolean isValid = true;
            for (String filter : filters) {
                if (!recipe.toString().contains(filter)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                filteredRecipes.add(recipe);
            }
        }

        return filteredRecipes;
    }

    @Override
    public String addRecipe(UUID recipeId, String name, String picturePath, String description,
            String instructions, UUID authorId,
            Map<Ingredient, Float> ingredients, byte servingSize) throws InvalidRecipeNameLengthException,
            InvalidRecipeDescriptionLengthException, InvalidRecipeInstructionsLengthException,
            InvalidRecipeServingSizeException, InvalidRecipeTagsCountException, InvalidRecipeIngredientsCountException {
        validateName(name);
        validateDescription(description);
        validateInstructions(instructions);
        // validateIngredients(ingredients);
        validateServingSize(servingSize);
        dbContext.addRecipe(recipeId, name, picturePath, description, instructions, servingSize, authorId);
        Set<Ingredient> keys = ingredients.keySet();
        for (Ingredient ingredient : keys) {
            dbContext.addRecipeIngredient(recipeId, ingredient.getId(), ingredients.get(ingredient));
        }
        return String.format(SuccessMessages.RECIPE_ADDED);
    }

    @Override
    public String editRecipeName(UUID recipeId, String name) throws InvalidRecipeNameLengthException {
        validateName(name);
        return dbContext.editRecipeName(recipeId, name);
    }

    @Override
    public String removeRecipe(UUID recipeId) {
        return dbContext.removeRecipeById(recipeId);
    }

    @Override
    public List<Recipe> getRecipesWithNameLike(String name) {
        return dbContext.getRecipesWithNameLike(name);
    }

    @Override
    public String editRecipeDescription(UUID recipeId, String description)
            throws InvalidRecipeDescriptionLengthException {
        validateDescription(description);
        dbContext.editRecipeDescription(recipeId, description);
        return null;
    }

    @Override
    public String editRecipeInstructions(UUID recipeId, String instructions)
            throws InvalidRecipeInstructionsLengthException {
        validateInstructions(instructions);
        dbContext.editRecipeInstructions(recipeId, instructions);
        return null;
    }

    @Override
    public String editRecipeServingSize(UUID recipeId, byte servingSize) throws InvalidRecipeServingSizeException {
        validateServingSize(servingSize);
        dbContext.editRecipeServingSize(recipeId, servingSize);
        return null;
    }

    @Override
    public void validateName(String name) throws InvalidRecipeNameLengthException {
        try {
            Validator.validateStringLength(name,
                    Variables.MIN_RECIPE_NAME_LENGTH, Variables.MAX_RECIPE_NAME_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidRecipeNameLengthException();
        }
    }

    @Override
    public void validateDescription(String description) throws InvalidRecipeDescriptionLengthException {
        try {
            Validator.validateStringLength(description, Variables.MIN_RECIPE_DESC_LENGTH,
                    Variables.MAX_RECIPE_DESC_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidRecipeDescriptionLengthException();
        }
    }

    @Override
    public void validateInstructions(String instructions) throws InvalidRecipeInstructionsLengthException {
        try {
            Validator.validateStringLength(instructions, Variables.MIN_RECIPE_INSTRUCTIONS_LENGTH,
                    Variables.MAX_RECIPE_INSTRUCTIONS_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidRecipeInstructionsLengthException();
        }
    }

    @Override
    public void validateServingSize(byte servingSize) throws InvalidRecipeServingSizeException {
        try {
            Validator.validateCount(servingSize, Variables.MIN_RECIPE_SERVING_SIZE, Variables.MAX_RECIPE_SERVING_SIZE);
        } catch (InvalidCountException e) {
            throw new InvalidRecipeServingSizeException();
        }
    }

    @Override
    public void validateIngredients(Map<Ingredient, Float> ingredients)
            throws InvalidRecipeIngredientsCountException {
        try {
            Validator.validateCount(ingredients.size(),
                    Variables.MIN_RECIPE_INGREDIENTS, Variables.MAX_RECIPE_INGREDIENTS);
        } catch (InvalidCountException e) {
            throw new InvalidRecipeIngredientsCountException();
        }
    }

    @Override
    public List<Comment> getCommentsByRecipeId(UUID recipeId) {
        return dbContext.getCommentsByRecipeId(recipeId);
    }

    @Override
    public Comment getCommentById(UUID commentId) {
        return dbContext.getCommentById(commentId);
    }

    @Override
    public void addRecipeIngredients(UUID recipeId, Map<Ingredient, Float> selectedIngredients) {
        dbContext.addRecipeIngredients(recipeId, selectedIngredients);
    }

    @Override
    public void addTagToRecipe(UUID tagId, UUID recipeId) {
       dbContext.addRecipeTag(recipeId, tagId);        
    }

    @Override
    public void removeTagFromRecipe(UUID recipeId, UUID tagId) {
        dbContext.removeRecipeTagById(recipeId, tagId);
    }

    @Override
    public void editRecipeImage(UUID recipeId, String picturePath) {
        dbContext.editRecipeImage(recipeId, picturePath);
    }

    public void removeRecipeIngredientsByRecipeId(UUID recipeId) {
        dbContext.removeRecipeIngredientsByRecipeId(recipeId);
    }

}
