package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.entities.Ingredient;
import util.common.UserListener;

public class IngredientCartController {
    private Ingredient ingredient;
    private UserListener userListener;


    @FXML
    private Label countLbl;

    @FXML
    private Label ingredientLbl;




    public void setData(Ingredient ingredient, Float quantity, UserListener userListener){
        this.ingredient = ingredient;
        this.userListener = userListener;
        String ingredientQuantity =String.valueOf(quantity);
        countLbl.setText(ingredientQuantity);
        ingredientLbl.setText(quantity + ingredient.getUnit() + ingredient.getName());
    }
}
