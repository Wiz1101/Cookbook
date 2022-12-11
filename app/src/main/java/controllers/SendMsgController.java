package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.entities.Recipe;
import models.entities.User;
import services.impl.UserServiceImpl;
import util.common.SceneContext;
import util.common.UserListener;
import util.constants.FailMessages;
import util.constants.SuccessMessages;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class SendMsgController implements Initializable {
    private UserServiceImpl userService = new UserServiceImpl();
    private User user = SceneContext.getUser();
    private Recipe recipe;
    private UserListener userListener;

    @FXML
    private TextField nicknameTxtField;

    @FXML
    private Button send;

    @FXML
    private TextArea msgArea;

    @FXML
    private ImageView recipeImg;

    @FXML
    private Label successMessage;

    @FXML
    void close(MouseEvent event) {
        userListener.closeSendMsgListener();
    }

    public void setData(Recipe recipe, UserListener userListener) {
        this.recipe = recipe;
        if (recipe != null) {
            recipeImg.setVisible(true);
            recipeImg.setImage(recipe.getPicture());
        }
        this.userListener = userListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    userService.sendMessage(UUID.randomUUID(), user.getId(),
                            userService.getUserByNickname(nicknameTxtField.getText()).getId(), msgArea.getText(),
                            recipe != null ? recipe.getId() : null);
                            showInformation(SuccessMessages.MESSAGE_SENT, null);
                            SceneContext.changeScene(event, "/fxmlFiles/home.fxml");
                } catch (Exception e) {
                    showError(FailMessages.MESSAGE_SEND_FAIL, e.getMessage());
                }
            }

        });

    }


    private void showInformation(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

}
