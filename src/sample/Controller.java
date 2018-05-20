package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
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
        String patientID = idList.get(selectIdx);
        System.out.println("patient id " + patientID);
        myPatient searchPat = myParser.searchMyPatient(myConnect,patientID);

        //---OD KASI---------- 430 gutierez
//        if (searchPat.getPatient().getManagingOrganization() != null) {
//
//                Bundle observationBundle = myConnect.getClient()
//                        .search()
//                        .forResource(Observation.class)
//                        .where(Observation.PATIENT.hasId(searchPat.getPatient().getId()))
//                        //.where(Observation.PATIENT.hasId("http://hapi.fhir.org/baseDstu2/Patient/184")).
//                        //.include(Observation.INCLUDE_PATIENT)
//                        .returnBundle(Bundle.class)
//                        .execute();
//
//                if(observationBundle.getEntry().size()>=1) {
//                    Observation obs = (Observation) observationBundle.getEntry().get(0).getResource();
//                    System.out.println("OBS: " + obs.getCategory());
//                }
//
////            Bundle medicationStatementBundle = myConnect.getClient()
////                    .search()
////                    .forResource(MedicationStatement.class)
////                    //.where(MedicationStatement.PATIENT.hasId("101")
////                    //.include(MedicationStatement.INCLUDE_PATIENT)
////                .where(new StringClientParam("patient").matches().value("http://hapi.fhir.org/baseDstu2/Patient/430/_history/1"))
////                    .returnBundle(Bundle.class)
////                    .execute();
//            Bundle medicationStatementBundle = myConnect.getClient()
//                    .search()
//                    .forResource(MedicationStatement.class)
//                    .where(new StringClientParam("patient").matches().value("http://hapi.fhir.org/baseDstu2/Patient/430/_history/1"))
//                    .execute();
//
//                if(medicationStatementBundle.getEntry().size()>=1) {
//                    System.out.println("Typ ms: " + medicationStatementBundle.getType());
//                    MedicationStatement ms = (MedicationStatement) medicationStatementBundle.getEntry().get(1).getResource();
//                    System.out.println("MS: " + ms.getMedication());
//                }
//
//        }
        //----------------- ----------------------

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
