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

public class PatientDetailsController {

    public Label textName;
    public Label textSex;
    public Label textBirth;
    //public Label textDeath;
    public Label textAddress;
    public Label textEmail;
    public Label textTelephone;
//    public void initialize(){
//        setTextStart("mamam");
//    }

    public void setTextStart( String name, String s,String b, String add, String em , String tel){
        this.textName.setText(name);
        this.textSex.setText(s);
        this.textBirth.setText(b);
        //this.textDeath.setText(d);
        this.textAddress.setText(add);
        this.textEmail.setText(em);
        this.textTelephone.setText(tel);
    }

}
