package sample;


import ca.uhn.fhir.model.dstu2.resource.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


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
        //System.out.println("Wybralem element " + selectIdx);
        //System.out.println("id elementu" + idList.get(selectIdx));
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
            items.add(p.getName() );
            idList.add(p.getId());
           }
        });
        listViewPatient.setItems(items);
        }

}
