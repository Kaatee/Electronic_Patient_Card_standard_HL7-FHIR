package sample;


import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Region;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.LinkedList;

public class PatientDetailsController  {

    public Label textName;
    public Label textSex;
    public Label textBirth;
    public Label textAddress;
    public Label textEmail;
    public Label textTelephone;
    public LineChart<String ,Number> myLineChart;
    public ListView listViewMedication;

    private Connection myConnect;
    private double circleSize = 100.0;
    private ArrayList sortedList;
    private List<myMedication> medList ;
    private List<myMedicationStatement> medStatList ;
    private List<myObservation> obsList ;
    private ArrayList obsAndMedStat ;
    private Parameters parameters;
    private List<Bundle.BundleEntryComponent> params ;
    private List<Parameters.ParametersParameterComponent> paramList ;


    public void setTextStart( String name, String s,String b, String add, String em , String tel){
        this.textName.setText(name);
        this.textSex.setText(s);
        this.textBirth.setText(b);
        this.textAddress.setText(add);
        this.textEmail.setText(em);
        this.textTelephone.setText(tel);
    }

    public void createDetailsTimeLine(myPatient searchPat){

        myConnect = new Connection();

        //// ----------- POBIERANIE WSZYSTKICH PARAMETROW PACIENTA ----------------------
        parameters = myConnect.getClient()
                .operation()
                .onInstance(new IdType("Patient", searchPat.getIdNum()))
                .named("$everything")
                .withNoParameters(Parameters.class)
                .useHttpGet()
                .execute();

        params = new LinkedList<>();
        paramList = parameters.getParameter();

        for(int i=0;i<paramList.size(); i++){
            Bundle bundle = (Bundle) paramList.get(i).getResource();
            params.addAll(bundle.getEntry());
            for(;;){
                if(bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                    bundle = myConnect.getClient().loadPage().next(bundle).execute();
                    params.addAll(bundle.getEntry());
                }
                else break;
            }
        }




        sortedList = sortList(makeLists());
        addToLineChart();

    }

    private ArrayList makeLists(){

        medList = new LinkedList<>();
        medStatList = new LinkedList<>();
        obsList = new LinkedList<>();
        obsAndMedStat = new ArrayList();

        //if(params.size()==0) System.out.println("Trafiłaś na durnia bez parametrów");

        for(int i=0;i<params.size();i++){
            if(params.get(i).getResource() instanceof Medication){
                Medication medTmp = (Medication) params.get(i).getResource();

                List<Medication.MedicationIngredientComponent> ingridientList = medTmp.getIngredient();
                String ingStr = "";

                for (int j=0;j<ingridientList.size();j++){
                    ingStr = ingStr + ", " + ingridientList.get(j).toString() +": " +ingridientList.get(j).getAmount().toString();
                }
                myMedication med = new myMedication(medTmp.getId(),ingStr);
                medList.add(med);
            }
            else
            if(params.get(i).getResource() instanceof MedicationStatement){
                MedicationStatement medStetTmp = (MedicationStatement) params.get(i).getResource();

                List<Dosage> dosage = medStetTmp.getDosage();
                List<Annotation> note = medStetTmp.getNote();

                String dosageStr = "";
                String noteStr="";

                for (int j=0;i<dosage.size();i++){
                    dosageStr = dosageStr + ", " + dosage.get(j).getPatientInstruction();
                }
                for (int j=0;i<note.size();i++){
                    noteStr = noteStr +", " + note.get(j).getText();
                }
                String measure ="none";
                String measureName="";
                try {
                    measure = medStetTmp.getDosageFirstRep().getDoseSimpleQuantity().getValue().toString() + medStetTmp.getDosageFirstRep().getDoseSimpleQuantity().getUnit();
                    measureName=medStetTmp.getMedicationCodeableConcept().getCodingFirstRep().getDisplay();
                }catch(Exception e){ }
                myMedicationStatement medStet = new myMedicationStatement(medStetTmp.getId(), dosageStr, noteStr, medStetTmp.getDateAsserted(),measure, measureName);
                medStatList.add(medStet);
                obsAndMedStat.add(medStet);
            }
            else
            if(params.get(i).getResource() instanceof Observation){ //Schiller Fricostam???
                Observation obsTmp = (Observation) params.get(i).getResource();
                try {
                    myObservation obs = new myObservation(obsTmp.getIssued(), obsTmp.getStatus().getDisplay(), obsTmp.getCode().getText());
                    obsList.add(obs);
                    obsAndMedStat.add(obs);
                } catch(Exception e){
                    myObservation obs = new myObservation(new Date(), "",obsTmp.getCode().getText()); //tu coś ogarnąź z datą w zależności jak Piterowi wygodniej
                    obsList.add(obs);
                    obsAndMedStat.add(obs);
                }

            }

        }
        return  obsAndMedStat;
    }

    private ArrayList swap(ArrayList array, int idx1, int idx2){
        ArrayList sortedArray = array;

        Object tmpIdx1;
        Object tmpIdx2;
        tmpIdx1 = sortedArray.get(idx1);
        tmpIdx2 = sortedArray.get(idx2);
        sortedArray.remove(idx1);
        sortedArray.add(idx1, tmpIdx2);
        sortedArray.remove(idx2);
        sortedArray.add(idx2, tmpIdx1);

        return sortedArray;
    }

    private ArrayList sortList(ArrayList array){
        myMedicationStatement medS =null;
        myMedicationStatement medS1= null;
        myObservation obs = null;
        myObservation obs1 = null;


        //tu sie wyjebuje dla Smith Evelyn jbc !

        int numberOfEl = array.size();
        if ( array.size()>1) {
            do {
                for (int x = 0; x < numberOfEl - 1; x++) {
                    if (array.get(x) instanceof myMedicationStatement) {
                        medS = (myMedicationStatement) array.get(x);

                    } else {
                        obs = (myObservation) array.get(x);
                    }

                    if (array.get(x + 1) instanceof myMedicationStatement) {
                        medS1 = (myMedicationStatement) array.get(x + 1);

                    } else {
                        obs1 = (myObservation) array.get(x + 1);
                    }


                    if (medS != null && medS1 != null) {

                        if (medS.getDate().after(medS1.getDate())) {
                            array = swap(array, x, x + 1);
                        }
                        medS = null;
                        medS1 = null;

                    } else if (medS != null && obs1 != null) {

                        if (medS.getDate().after(obs1.getDate())) {
                            array = swap(array, x, x + 1);
                        }
                        medS = null;
                        obs1 = null;

                    } else if (obs != null && medS1 != null) {
                        if (obs.getDate().after(medS1.getDate())) {
                            array = swap(array, x, x + 1);
                        }
                        obs = null;
                        medS1 = null;
                    } else if (obs != null && obs1 != null) {
                        if (obs.getDate().after(obs1.getDate())) {
                            array = swap(array, x, x + 1);
                        }
                        obs = null;
                        obs1 = null;
                    }


                }

                numberOfEl -= 1;


            } while (numberOfEl > 1);

        }


        return array;

    }

    private void  addToLineChart(){

        XYChart.Series series = new XYChart.Series();
        //podpisy
        myLineChart.getYAxis().setTickLabelsVisible(false);
        myLineChart.getYAxis().setOpacity(0.0);
        myLineChart.getXAxis().setTickLabelsVisible(true);
        // linie siatki
        myLineChart.setHorizontalGridLinesVisible(false);
        myLineChart.setVerticalGridLinesVisible(false);
        myLineChart.setVerticalZeroLineVisible(false);
        myLineChart.setHorizontalZeroLineVisible(false);
       //wart
        NumberAxis rangeAxis = (NumberAxis)myLineChart.getYAxis();
        rangeAxis.setUpperBound(2.0);
        rangeAxis.setLowerBound(0.0);
        rangeAxis.setAutoRanging(false);

        myMedicationStatement medS=null;
        myObservation obs=null;

        ObservableList<XYChart.Data<String, Number>> dataObservableList = FXCollections.observableArrayList();
        //System.out.println(sortedList.size());

        //dodanie warat na osi X
        for (int i = 0; i < sortedList.size(); i++) {
            XYChart.Data<String, Number> data ;
            if(sortedList.get(i) instanceof myMedicationStatement) {
                medS= (myMedicationStatement) sortedList.get(i);
            data = new XYChart.Data<>(medS.getDate().toString(), 1);
            }
            else {
                obs = (myObservation )sortedList.get(i);
                data = new XYChart.Data<>(obs.getDate().toString() , 1);
            }

            Region region = new Region();
            region.setShape(new Circle(circleSize));
            region.setPrefHeight(circleSize);
            region.setPrefWidth(circleSize);
            try{
                if (medS!=null) {
                    region.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(new File(getClass().getResource("/recepta.png").toURI()))), null, null, null, null)));
                }
                else if (obs!=null){
                    region.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(new File(getClass().getResource("/lupa.png").toURI()))), null, null, null, null)));
                }
                }
        catch (Exception e) {
            System.out.println("Nie ustawilem backgroundu");
        }

            data.setNode(region);
            dataObservableList.add(data);
            medS=null;
            obs=null;
        }

        series.getData().addAll(dataObservableList);
        myLineChart.getData().add(series);
        myLineChart.setPrefWidth(200*sortedList.size());

        //tworzenie tooltipu o danym medykamencie
        Set<Node> nodes = myLineChart.lookupAll(".default-color0.chart-line-symbol.series0.");
        nodes.forEach((element) -> element.setOnMouseEntered((MouseEvent event) -> {
            for (int i = 0; i< dataObservableList.size(); i++){
                myMedicationStatement medS1=null;
                myObservation obs1=null;
                if(sortedList.get(i) instanceof myMedicationStatement) {
                    medS1= (myMedicationStatement) sortedList.get(i);
                }
                else {
                    obs1 = (myObservation )sortedList.get(i);
                }

                if (event.getSource().toString().contains("data"+i) && obs1!=null){
                    Tooltip tooltip = new Tooltip(obs1.printIt());
                    Tooltip.install(element, tooltip);
                }
                else if (event.getSource().toString().contains("data"+i) && medS1!=null){
                    Tooltip tooltip = new Tooltip(medS1.printIt());
                    Tooltip.install(element, tooltip);
                }

            }

        }));


        updateMed();
    }

    private void updateMed(){
        System.out.println("Rozmiar med " + medList.size());
        listViewMedication.getItems().clear();
        ObservableList<String> items =FXCollections.observableArrayList ();
        medList.forEach((p)->{ items.add(p.printIt()); });
        listViewMedication.setItems(items);
    }


}
