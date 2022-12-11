package services.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.entities.Ingredient;
import services.IngredientService;
import util.common.DbContext;
import util.constants.Variables;

public class IngredientServiceImpl implements IngredientService {
    private DbContext dbContext;

    public IngredientServiceImpl() {
        super();
        dbContext = new DbContext(Variables.DATABASE_PORT, Variables.DATABASE_USER, Variables.DATABASE_PASS);
    }

    @Override
    public List<Ingredient> getIngredientWithNameLike(String name) {
        return dbContext.getIngredientsWithNameLike(name);
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return dbContext.getAllIngredients();
    }

    @Override
    public Map<Ingredient, Float> getIngredientsByRecipeId(UUID recipeId) {
        return dbContext.getIngredientsByRecipeId(recipeId);
    }

    @Override
    public Ingredient getIngredientById(UUID id) {
        return dbContext.getIngredientById(id);
    }
    
}
