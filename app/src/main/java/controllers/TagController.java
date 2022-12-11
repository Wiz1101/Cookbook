package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import models.entities.Tag;
import util.common.UserListener;

public class TagController {

    private Tag tag;
    private UserListener userListener;
    private String caller;
    private Boolean isPressed = false;
    @FXML
    private Label tagLbl;

    @FXML
    private ImageView tagButton;

    @FXML
    private void tagClick(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            String imgFile = "/img/uncheck.png";
            Image uncheck = new Image(getClass().getResourceAsStream(imgFile));
            tagButton.setImage(uncheck);
        } else {
            isPressed = true;
            String imgFile = "/img/check.png";
            Image check = new Image(getClass().getResourceAsStream(imgFile));
            tagButton.setImage(check);
        }

        if (caller.equals("HomeController")) {
            userListener.tagClickListener(tag);
        } else if (caller.equals("DetailedViewController")) {
            if (isPressed) {
                userListener.addTagToRecipe(tag);
            } else {
                userListener.removeTagFromRecipe(tag);
            }
        }
    }

    public void setData(Tag tag, String caller, UserListener userListener) {
        this.tag = tag;
        this.caller = caller;
        this.userListener = userListener;
        tagLbl.setText(tag.getName());
    }

    public void checkBox() {
        isPressed = true;
        String imgFile = "/img/check.png";
        Image check = new Image(getClass().getResourceAsStream(imgFile));
        tagButton.setImage(check);
    }

    public void setSelected(boolean bool) {
        this.isPressed = bool;
        checkBox();
    }

}
