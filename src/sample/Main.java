package sample;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(" --> Patient Card <-- [ HL7 FHIR ]");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
    }



    public static void main(String[] args) {

        launch(args);


    }
}
