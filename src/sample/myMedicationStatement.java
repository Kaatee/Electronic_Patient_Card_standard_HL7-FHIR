package sample;

import java.util.Date;

public class myMedicationStatement {
    private String id;
    private String note;
    private Date date;
    private String measureName;
    private String measure;


    public myMedicationStatement(String id,  String note, Date date,String measure, String measureName,int i){
        this.id=id;
        this.note=note;
        this.measure=measure;
        this.measureName=measureName;
        if(date==null){
            this.date=new Date(System.currentTimeMillis() + i*1000L);
        }
        else
            this.date=date;
        //System.out.println("Ustawilem date na " + this.date);
    }


    public String getId() {
        return id;
    }
    public Date getDate() {
        return date;
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

        name = "Medication Statement: \nId: " + getId() + "\nNote: "+ getNote() + "\nDate: " + getDate().toString()+"\nName of medication: "+getMeasureName()+"\nDosage: "+getMeasure();
        return name;
    }
}


