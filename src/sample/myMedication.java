package sample;

public class myMedication {
    private String name;
    private String dosage;


    public myMedication( String name,String dosage){
        this.name=name;
        this.dosage=dosage;
    }


    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public String printIt(){
        String name;

        name = "Medication: \nName: "+ getName() +"\nDosage: "+ getDosage();
        return name;
    }

}
