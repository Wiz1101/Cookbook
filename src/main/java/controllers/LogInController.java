package controllers;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import models.entities.User;
import services.impl.UserServiceImpl;
import util.common.SceneContext;

public class LogInController implements Initializable{

    @FXML
    private AnchorPane anchorVisible;

    @FXML
    private Button login;

    @FXML
    private Button signup;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private ImageView logo;


    @FXML
    void login(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            User user = userService.loginUser(username.getText(), password.getText());
            if (user != null) {
                SceneContext.setUser(user);
                if (user.getUsername().equals("admin")) {
                    SceneContext.changeSceneOnPressedKey(event, "/fxmlFiles/adminPage.fxml");
                }else {
                    SceneContext.changeSceneOnPressedKey(event, "/fxmlFiles/home.fxml");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Credentials are incorrect");
                alert.show();
            }
        }

    }







    private UserServiceImpl userService = new UserServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Animation ----------- ----------- -----------
        logo.setVisible(true);
        anchorVisible.setVisible(false);


        RotateTransition rotate = new RotateTransition();
        rotate.setNode(logo);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.play();

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        visiblePause.setOnFinished(
                event -> logo.setVisible(false));
        visiblePause.play();
        PauseTransition visiblePause1 = new PauseTransition(
                Duration.seconds(3));
        visiblePause1.setOnFinished(
                event -> anchorVisible.setVisible(true));
        visiblePause1.play();
        // ----------- ----------- -----------

        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = userService.loginUser(username.getText(), password.getText());
                if (user != null) {
                    SceneContext.setUser(user);
                    if (user.getUsername().equals("admin")) {
                        SceneContext.changeScene(event, "/fxmlFiles/adminPage.fxml");
                    }else {
                        SceneContext.changeScene(event, "/fxmlFiles/home.fxml");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Credentials are incorrect");
                    alert.show();
                }

            }
        });

        signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneContext.changeScene(event, "/fxmlFiles/signUp.fxml");
            }
        });

    }

}