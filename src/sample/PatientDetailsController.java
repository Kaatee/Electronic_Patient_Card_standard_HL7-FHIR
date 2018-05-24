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

public class PatientDetailsController {

    public Label textName;
    public Label textSex;
    public Label textBirth;
    public Label textAddress;
    public Label textEmail;
    public Label textTelephone;

    private Connection myConnect;
    public LineChart<String ,Number> myLineChart;
    private double circleSize = 100.0;

    public void setTextStart( String name, String s,String b, String add, String em , String tel){
        this.textName.setText(name);
        this.textSex.setText(s);
        this.textBirth.setText(b);
        this.textAddress.setText(add);
        this.textEmail.setText(em);
        this.textTelephone.setText(tel);
    }

    public void createDetailsTimeLine(myPatient searchPat){
        ////////////////////////////////////////
        System.out.println("Jestem TU");
        System.out.println("Id wyswietlanego pacjenta 1: " + searchPat.getIdNum());
        Connection myConnect = new Connection();

        //// ----------- POBIERANIE WSZYSTKICH PARAMETROW PACIENTA ----------------------
        Parameters parameters = myConnect.getClient()
                .operation()
                .onInstance(new IdType("Patient", searchPat.getIdNum()))
                .named("$everything")
                .withNoParameters(Parameters.class)
                .useHttpGet()
                .execute();

        List<Bundle.BundleEntryComponent> params = new LinkedList<>();
        List<Parameters.ParametersParameterComponent> paramList = parameters.getParameter();

        for(int i=0;i<paramList.size(); i++){
            Bundle bundle = (Bundle) paramList.get(i).getResource();
            params.addAll(bundle.getEntry());
            //Bundle partialBundle =  bundle;
            for(;;){
                if(bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                    bundle = myConnect.getClient().loadPage().next(bundle).execute();
                    params.addAll(bundle.getEntry());
                }
                else break;
            }
        }
        System.out.println("Dlugosc listy parametrów:  " + params.size());


        //--------------- POBIERANIE LISTY MEDYKAMENTOW PACJEMTA -------------------------
        List<myMedication> medList = new LinkedList<>();
        List<myMedicationStatement> medStatList = new LinkedList<>();
        List<myObservation> obsList = new LinkedList<>();
        if(params.size()==0) System.out.println("Trafiłaś na durnia bez parametrów");
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

                myMedicationStatement medStet = new myMedicationStatement(medStetTmp.getId(), dosageStr, noteStr);
                medStatList.add(medStet);
            }
            ///////////////////////tomoze trzeba bedzie przelozyc do medication statement zeby sie wspolnie wyswietlaly
            else
            if(params.get(i).getResource() instanceof Observation){ //Schiller Fricostam???
                Observation obsTmp = (Observation) params.get(i).getResource();
                try {
                    myObservation obs = new myObservation(obsTmp.getIssued(), obsTmp.getStatus().getDisplay());
                    obsList.add(obs);
                } catch(Exception e){
                    myObservation obs = new myObservation(new Date(), ""); //tu coś ogarnąź z datą w zależności jak Piterowi wygodniej
                    obsList.add(obs);
                }

            }
            else System.out.println("Twój duren jednak ma parametry ale nie te kore chcesz");



        }
        System.out.println("Wyswietlam pobrane rzeczy: ");
        for (int a=0;a<medList.size();a++) medList.get(a).printIt();
        for (int a=0;a<medStatList.size();a++) medStatList.get(a).printIt();
        for (int a=0;a<obsList.size();a++) obsList.get(a).printIt();


///////////////////////////////////////////////////////
    }


    public void initialize(){

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


        ObservableList<XYChart.Data<String, Number>> dataObservableList = FXCollections.observableArrayList();

        for (int i = 1; i < 10; i++) {
           // series.getData().add(new XYChart.Data<String , Number>( "Date "+i+"", 1.0));
            XYChart.Data<String, Number> data = new XYChart.Data<>("Date"+i, 1);
            Region region = new Region();
            region.setShape(new Circle(circleSize));
            region.setPrefHeight(circleSize);
            region.setPrefWidth(circleSize);
            try{
                region.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(new File(getClass().getResource("/lupa.png").toURI()))), null, null, null, null)));
            }
        catch (Exception e) {
            e.printStackTrace();
        }

           // series.setNode(region);
            data.setNode(region);
            dataObservableList.add(data);
        }

        series.getData().addAll(dataObservableList);
        myLineChart.getData().add(series);



        myLineChart.setPrefWidth(150*10);


        Set<Node> nodes = myLineChart.lookupAll(".default-color0.chart-line-symbol.series0.");
        nodes.forEach((element) -> element.setOnMouseEntered((MouseEvent event) -> {
            for (int i = 0; i< dataObservableList.size(); i++){

                if (event.getSource().toString().contains("data"+i)){
                    Tooltip tooltip = new Tooltip("Myyysasaysyasa" + i);
                    Tooltip.install(element, tooltip);
                }
            }
        }));



    }


}
