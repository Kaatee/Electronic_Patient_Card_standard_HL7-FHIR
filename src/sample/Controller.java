package sample;


import ca.uhn.fhir.model.dstu2.resource.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Controller {


    public Button buttonShow;
    public Button buttonDetails;
    public ListView listViewPatient;
    private Connection myConnect;
    private ArrayList<String> idList;

    public void showDetails(ActionEvent event){

        int selectIdx = listViewPatient.getSelectionModel().getSelectedIndex();
        System.out.println("Wybralem element " + selectIdx);
        System.out.println(idList.get(selectIdx));
    }

    public void showList(ActionEvent event){
        //connect to server
        myConnect = new Connection();
        myConnect.makeResult();

        ObservableList<String> items =FXCollections.observableArrayList ();
         idList = new ArrayList<String>();

        myConnect.getPatientList().forEach((myPatient)->{
                String name=myParser.myGetName(myPatient);
                if(name.length()>1) {
                    items.add(name);
                    idList.add(myPatient.getId().toString());
                }
            });

        listViewPatient.setItems(items);



    }



}
