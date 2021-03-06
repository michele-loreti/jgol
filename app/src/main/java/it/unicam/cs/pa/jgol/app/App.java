/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package it.unicam.cs.pa.jgol.app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GOLApp.fxml")));
        primaryStage.setTitle("GOL App");
        primaryStage.setScene(new Scene(root, GOLAppController.WIDTH, GOLAppController.HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
