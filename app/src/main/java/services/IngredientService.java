package services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.entities.Ingredient;

public interface IngredientService {
    public List<Ingredient> getAllIngredients();
    public Ingredient getIngredientById(UUID id);
    public List<Ingredient> getIngredientWithNameLike(String name);
    public Map<Ingredient, Float> getIngredientsByRecipeId(UUID recipeId);
}
