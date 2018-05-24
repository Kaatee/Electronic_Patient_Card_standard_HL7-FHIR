package sample;

public class myMedication {
    private String id;
    private String ingridients;


    public myMedication(String id, String ingridients){
        this.id=id;
        this.ingridients=ingridients;
    }

    public String getId() {
        return id;
    }


    public String getIngridients() {
        return ingridients;
    }

    public String printIt(){
        String name;

        name = "Observation: \nId: " + getId() + "\ningredients: " + getIngridients();
        return name;
    }

}
