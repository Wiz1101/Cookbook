package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

import models.entities.Ingredient;
import util.common.UserListener;

public class IngredientController implements Initializable {
    private Ingredient ingredient;
    private UserListener userListener;

    @FXML
    private Label ingredientLbl;

    @FXML
    private ImageView ingredientButton;

    @FXML
    private void ingredientClick(MouseEvent event) {
        userListener.ingredientClickListener(ingredient, ingredientButton);
    }

    public void setData(Ingredient ingredient, UserListener userListener) {
        this.ingredient = ingredient;
        this.userListener = userListener;
        ingredientLbl.setText(ingredient.getName());
    }

    public void checkBox() {
        String imgFile = "/img/check.png";
        Image check = new Image(getClass().getResourceAsStream(imgFile));
        ingredientButton.setImage(check);
    }

    public void uncheckBox() {
        String imgFile = "/img/uncheck.png";
        Image uncheck = new Image(getClass().getResourceAsStream(imgFile));
        ingredientButton.setImage(uncheck);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
