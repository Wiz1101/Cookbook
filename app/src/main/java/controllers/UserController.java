package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import models.entities.User;
import util.common.AdminListener;

public class UserController {
    private AdminListener adminListener;
    private User user;
    
    @FXML
    private Button editUser;

    @FXML
    private TextArea email;

    @FXML
    private TextArea nickname;

    @FXML
    private TextArea password;

    @FXML
    private Button removeUser;

    @FXML
    private TextArea username;

    @FXML
    void edit(MouseEvent event) {
        adminListener.editUser(user, username.getText(), nickname.getText(), email.getText(), password.getText());
        username.setText("");
        nickname.setText("");
        email.setText("");
        password.setText("");
    }

    @FXML
    void remove(MouseEvent event) {
        adminListener.removeUser(user);
    }


    public void setData(User user, AdminListener adminListener){
        this.user = user;
        this.adminListener = adminListener;   
    
        email.setPromptText(user.getEmail());
        username.setPromptText(user.getUsername());
        nickname.setPromptText(user.getNickname());
        password.setPromptText("New password..");
    }
}