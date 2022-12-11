package util.common;

import java.io.IOException;

import javafx.scene.input.KeyEvent;
import models.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SceneContext {
    public static User user;

    public static void changeSceneOnPressedKey(KeyEvent event, String fxmlFile) {
        Parent root = null;
        if (!fxmlFile.equals("")){
            try {
                FXMLLoader loader = new FXMLLoader(DbContext.class.getResource(fxmlFile));
                root = loader.load();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try {
                root = FXMLLoader.load(DbContext.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //SIZES OF THE DIFFERENT SCENES
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (fxmlFile.equals("/fxmlFiles/login.fxml") || fxmlFile.equals("/fxmlFiles/signUp.fxml")){
            stage.setScene(new Scene(root, 600,400 ));
            String title = String.format("Welcome to Cookbook");
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } else {
            stage.setScene(new Scene(root, 1315.0,810.0 ));
            stage.centerOnScreen();
            String title = String.format("Welcome to Cookbook");
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        }
    }

    public static void changeScene(ActionEvent event, String fxmlFile) {
        Parent root = null;
        if (!fxmlFile.equals("")){
            try {
                FXMLLoader loader = new FXMLLoader(DbContext.class.getResource(fxmlFile));
                root = loader.load();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try {
                root = FXMLLoader.load(DbContext.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //SIZES OF THE DIFFERENT SCENES
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (fxmlFile.equals("/fxmlFiles/login.fxml") || fxmlFile.equals("/fxmlFiles/signUp.fxml")){
            stage.setScene(new Scene(root, 600,400 ));
            String title = String.format("Welcome to Cookbook");
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } else {
            stage.setScene(new Scene(root, 1315.0,810.0 ));
            stage.centerOnScreen();
            String title = String.format("Welcome to Cookbook");
            stage.setTitle(title);
            stage.setResizable(false);

            stage.show();
        }
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static User getUser() {
        return user;
    }
}
