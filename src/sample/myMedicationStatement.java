package sample;

public class myMedicationStatement {
    private String id;
    private String dosage;
    private String note;

    public myMedicationStatement(String id, String dosage, String note){
        this.id=id;
        this.dosage=dosage;
        this.note=note;
    }


    public String getId() {
        return id;
    }

    public String getDosage() {
        return dosage;
    }

    public String getNote() {
        return note;
    }

    public void printIt(){
        System.out.println("Medication Statement: id: " + getId() + " dosage: " + getDosage() + " note: "+ getNote());
    }
}


