package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import models.entities.User;
import services.impl.UserServiceImpl;
import util.constants.SuccessMessages;
import util.common.AdminListener;
import util.common.SceneContext;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class AdminController implements Initializable {
    private UserServiceImpl userService = new UserServiceImpl();
    private AdminListener adminListener;

    @FXML
    private Button addUserButton;

    @FXML
    private TextArea email;

    @FXML
    private GridPane grid;

    @FXML
    private Button logout;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button search;

    @FXML
    private TextField searchField;

    @FXML
    private TextArea userName;

    @FXML
    private TextArea userPassword;

    private List<User> usersList = userService.getUsers();

    private void initializeUsersGrid() {
        grid.getChildren().clear();
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < usersList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/user.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                UserController userController = fxmlLoader.getController();
                userController.setData(usersList.get(i), adminListener);
                if (column == 1) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminListener = new AdminListener() {

            @Override
            public void removeUser(User user) {
                
                showInformation(userService.removeUserById(user.getId()), null);
                usersList.remove(user);
                initializeUsersGrid();
            }

            @Override
            public void editUser(User user, String username, String nickname, String email, String password) {
                StringBuilder message = new StringBuilder();
                try {
                    if (!username.equals("")) {
                        message.append(userService.changeUsername(user.getId(), username));
                    }
                    if (!nickname.equals("")) {
                        message.append(userService.changeNickname(user.getId(), nickname));
                    }
                    if (!email.equals("")) {
                        message.append(userService.changeEmail(user.getId(), email));
                    }
                    if (!password.equals("")) {
                        message.append(userService.changePassword(user.getId(), password));
                    }
                    if (!message.toString().equals("")) {
                        showInformation(SuccessMessages.USER_MODIFIED, message.toString());
                        usersList = userService.getUsers();
                        initializeUsersGrid();    
                    }
                } catch (Exception e) {
                    showAlert(e.getMessage());
                }
            }
        };

        initializeUsersGrid();

        addUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    
                    showInformation(userService.addUser(UUID.randomUUID(), userName.getText(), email.getText(), userPassword.getText()), null);
                    userName.setText("");
                    email.setText("");
                    userPassword.setText("");

                    usersList = userService.getUsers();
                    initializeUsersGrid();

                } catch (Exception e) {
                    showAlert(e.getMessage());
                }
            }
        });

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                usersList = userService.getUsersLike(searchField.getText());
                initializeUsersGrid();
            }
        });

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneContext.setUser(null);
                SceneContext.changeScene(event, "/fxmlFiles/login.fxml");
            }
        });
    }

}
