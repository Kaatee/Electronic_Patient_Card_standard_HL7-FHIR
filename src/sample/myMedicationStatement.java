package sample;

import java.util.Date;

public class myMedicationStatement {
    private String id;
    private String dosage;
    private String note;
    private Date date;
    private String measureName;
    private String measure;

    public myMedicationStatement(String id, String dosage, String note, Date date,String measure, String measureName){
        this.id=id;
        this.dosage=dosage;
        this.note=note;
        this.date=date;
        this.measure=measure;
        this.measureName=measureName;
    }


    public String getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }

    public String getDosage() {
        return dosage;
    }

    public String getNote() {
        return note;
    }
    public String getMeasure() {
        return measure;
    }
    public String getMeasureName() {
        return measureName;
    }

    public String printIt(){
        // System.out.println("Medication Statement: id: " + getId() + " dosage: " + getDosage() + " note: "+ getNote() + " data: " + getDate().toString());
        String name;

        name = "Medication Statement: \nid: " + getId() + "\ndosage: " + getDosage() + "\nnote: "+ getNote() + "\ndata: " + getDate().toString()+"\nnazwa badania: "+getMeasureName()+"\nmeasure: "+getMeasure();
        return name;
    }
}


