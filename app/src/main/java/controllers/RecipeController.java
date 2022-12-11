package controllers;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.entities.Recipe;
import util.common.UserListener;

public class RecipeController{
    private Recipe recipe;
    private UserListener userListener;

    @FXML
    private Label recipeName;

    @FXML
    private ImageView image;

    @FXML
    private ImageView heartImage;

    @FXML
    private TextArea recipeDescriptionHover;

    @FXML
    private Label dateLbl;

    @FXML
    void favoritesClick(MouseEvent event) {
        userListener.favClickListener(recipe, heartImage);
    }

    @FXML
    void click(MouseEvent mouseEvent) {
        userListener.onClickListener(recipe);
    }

    @FXML
    void descriptionEntered(MouseEvent event) {
        userListener.recipeEntered(recipe, recipeDescriptionHover);
    }
    @FXML
    void descriptionExited(MouseEvent event) {
        userListener.recipeExited(recipe, recipeDescriptionHover);
    }

    @FXML
    void clickDescription(MouseEvent event) {
        userListener.descriptionListener(recipe);
    }
    
    public void setData(Recipe recipe, Image heartImage, UserListener userListener) {
        this.recipe = recipe;
        this.userListener = userListener;
        recipeName.setText(recipe.getName());
        image.setImage(recipe.getPicture());
        this.heartImage.setImage(heartImage);
    }

    public void setDate(Date date) {
        DateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        String dateToString = df.format(date);

        dateLbl.setText(dateToString);
        dateLbl.setVisible(true);
    }

}
