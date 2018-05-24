package sample;

import java.util.Date;

public class myMedicationStatement {
    private String id;
    private String dosage;
    private String note;
    private Date date;

    public myMedicationStatement(String id, String dosage, String note, Date date){
        this.id=id;
        this.dosage=dosage;
        this.note=note;
        this.date=date;
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

    public String printIt(){
       // System.out.println("Medication Statement: id: " + getId() + " dosage: " + getDosage() + " note: "+ getNote() + " data: " + getDate().toString());
        String name;

        name = "Medication Statement: id: " + getId() + " dosage: " + getDosage() + " note: "+ getNote() + " data: " + getDate().toString();
        return name;
    }
}


