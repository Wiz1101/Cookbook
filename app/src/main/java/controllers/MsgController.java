package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.entities.Message;
import models.entities.Recipe;
import services.RecipeService;
import services.UserService;
import services.impl.RecipeServiceImpl;
import services.impl.UserServiceImpl;
import util.common.UserListener;


public class MsgController{
    private UserService userService = new UserServiceImpl();
    private RecipeService recipeService = new RecipeServiceImpl();
    private Message message;
    private Recipe recipe;
    private UserListener userListener;

    @FXML
    private Label MsgUserLbl;

    @FXML
    private TextArea messageReceivedArea;

    @FXML
    private Button openRecipe;

    @FXML
    private ImageView recipeImg;

    @FXML
    void reply(MouseEvent event) {
        userListener.replyMsgListener(message);
    }

    @FXML
    void removeMsg(MouseEvent event) {
        userListener.removeMsgListener(message);
    }

    @FXML
    void recipeOpen(MouseEvent event) {
        userListener.openRecipeListener(recipe);
    }

    public void setData(Message message, UserListener userListener){
        this.message = message;
        if (message.getRecipeId() != null) {
            openRecipe.setVisible(true);
            recipe = recipeService.getRecipeById(message.getRecipeId());
            recipeImg.setVisible(true);
            recipeImg.setImage(recipe.getPicture());
        }
        this.userListener = userListener;
        messageReceivedArea.setText(message.getText());
        MsgUserLbl.setText(userService.getUserById(message.getSender()).getNickname());
    }

}