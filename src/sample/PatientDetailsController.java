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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class PatientDetailsController {

    public Label textName;
    public Label textSex;
    public Label textBirth;
    public Label textAddress;
    public Label textEmail;
    public Label textTelephone;

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
