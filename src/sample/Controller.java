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


public class Controller {


    public Button buttonShow;
    public ListView listViewPatient;


    public void showList(ActionEvent event){
        //connect to server
        Connection myConnect = new Connection();
        myConnect.makeResult();

        ObservableList<String> items =FXCollections.observableArrayList ();

        myConnect.getPatientList().forEach((myPatient)->{


            String name ;
            name=myGetName(myPatient);
            if(name.length()>1)
                items.add(name);

        });

        listViewPatient.setItems(items);


    }

    public String myGetName(Patient patient){
        String name = "";

        for (int i=0;i<patient.getName().size();i++){
            if(patient.getName().get(i).getFamilyAsSingleString().length()>0  )
            name = name + " " + patient.getName().get(i).getFamilyAsSingleString();
        }
        name+=" ";
        for (int i=0;i<patient.getName().size();i++){
            if(patient.getName().get(i).getGivenAsSingleString().length()>0)
                name = name + " " + patient.getName().get(i).getGivenAsSingleString();
        }

        return name;
    }




}
