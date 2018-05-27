package sample;


import org.hl7.fhir.dstu3.model.Bundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


public class Controller {


    public Button buttonShow;
    public Button buttonDetails;
    public Button buttonSearch;
    public TextField textFieldSearch;
    public ListView listViewPatient;
    public DatePicker datePickerFrom;
    public DatePicker datePickerTo;

    private Connection myConnect;
    private ArrayList<String> idList;

    public void showDetails(ActionEvent event){
        int selectIdx = listViewPatient.getSelectionModel().getSelectedIndex();
        String patientID = idList.get(selectIdx);
        myPatient searchPat = myParser.searchMyPatient(myConnect,patientID);
        Date to,from;

        try{
            LocalDate localDate = datePickerFrom.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            from= Date.from(instant);
            LocalDate localDate1 = datePickerTo.getValue();
            Instant instant1 = Instant.from(localDate1.atStartOfDay(ZoneId.systemDefault()));
            to = Date.from(instant1);

        }
        catch (NullPointerException e) {
            from = null;
            to = null;
        }

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


        pat.createDetailsTimeLine(searchPat,from,to);

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

        myConnect = new Connection();
        myConnect.makeResult(searchName);


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
