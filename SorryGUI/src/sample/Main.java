package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.*;


public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Play Sorry");
        primaryStage.setScene(new Scene(root, 628, 629));
        primaryStage.setResizable(false);
        File f = new File("style.css"); // open style sheet
        primaryStage.getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));//add image to stage
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);

    }


}
