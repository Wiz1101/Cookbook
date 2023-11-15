package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import models.entities.Message;
import services.impl.UserServiceImpl;
import util.common.SceneContext;
import util.common.UserListener;
import util.constants.FailMessages;
import util.constants.SuccessMessages;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ReplyController implements Initializable {
    private UserServiceImpl userService = new UserServiceImpl();
    private UserListener userListener;
    private Message message;
    private UUID senderId;

    @FXML
    private Button close;

    @FXML
    private TextArea replyMsgArea;
    @FXML
    private TextArea receivedMsgArea;

    @FXML
    private Button send;

    @FXML
    private Label sender;

    @FXML
    private Label successMessage;

    @FXML
    void closeMsgReply(MouseEvent event) {
        userListener.closeMsgListener();
    }

    public void setData(Message message, UserListener userListener) {
        this.userListener = userListener;
        this.message = message;
        this.senderId = message.getSender();
        String senderNickname = userService.getUserById(senderId).getNickname();
        sender.setText(senderNickname);
        receivedMsgArea.setText(message.getText());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    userService.sendMessage(UUID.randomUUID(), message.getReceiver(), message.getSender(),
                            replyMsgArea.getText(), null);
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
