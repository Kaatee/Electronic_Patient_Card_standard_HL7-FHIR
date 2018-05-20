package sample;


import ca.uhn.fhir.model.dstu2.resource.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class Controller {


    public Button buttonShow;
    public Button buttonDetails;
    public Button buttonSearch;
    public TextField textFieldSearch;
    public ListView listViewPatient;

    private Connection myConnect;
    private ArrayList<String> idList;

    public void showDetails(ActionEvent event){
        int selectIdx = listViewPatient.getSelectionModel().getSelectedIndex();
        String patientID = idList.get(selectIdx);
        System.out.println("patient id " + patientID);
        myPatient searchPat = myParser.searchMyPatient(myConnect,patientID);


        //System.out.println("Wybralem element " + selectIdx);
        //System.out.println("id elementu" + idList.get(selectIdx));

//        Parent root;
//        try {
//            root = FXMLLoader.load(getClass().getResource("patientDetails.fxml"));
//
//           // PatientDetailsController pat = new FXMLLoader().getController();
//            //pat.setTextStart("mama");
//            Stage stage = new Stage();
//            stage.setTitle("Patient Details");
//            stage.setScene(new Scene(root, 1000, 650));
//            stage.show();
//            // Hide this current window (if this is what you want)
//           // ((Node)(event.getSource())).getScene().getWindow().hide();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("PatientDetails.fxml"));
            try{
                Loader.load();
            }
            catch (IOException ex ){
                ex.printStackTrace();
            }

            PatientDetailsController pat = Loader.getController();
            pat.setTextStart(searchPat.getName(), searchPat.getSex(), searchPat.getBirth(), searchPat.getAddress(), searchPat.getEmail(), searchPat.getTelephone());
            Parent root = Loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Patient Details");
            stage.setScene(new Scene(root, 1000, 650));
            stage.show();

    }

    public void showList(ActionEvent event){
        //connect to server
        myConnect = new Connection();
        myConnect.makeResult();

        ObservableList<String> items =FXCollections.observableArrayList ();
        idList = new ArrayList<>();

        myConnect.getPatientList().forEach((p)->{
              items.add(p.getName() );
              idList.add(p.getId());
            });
        listViewPatient.setItems(items);
    }

    public void search (ActionEvent event){
        String searchName = textFieldSearch.getText();
        listViewPatient.getItems().clear();

        ObservableList<String> items =FXCollections.observableArrayList ();
        idList = new ArrayList<>();

        myConnect.getPatientList().forEach((p)->{
           if(p.getName().contains(searchName)){
            items.add(p.getName());
            idList.add(p.getId());
           }
        });
        listViewPatient.setItems(items);
        }

}
