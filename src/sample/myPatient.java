package sample;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class myPatient {

    private Patient patient;
    private String name;
    private String id;


    public myPatient(Patient p, String n, String i){
        this.patient=p;
        this.name = n;
        this.id = i;
    }



    public String getSex(){
        try{
            return patient.getGender();
        }
    catch(Exception e){
            return "none";
        }
    }

    public String getBirth(){
        try{
            return patient.getBirthDate().toString();
        }
        catch(Exception e){
            return "none";
        }
    }

    public String getAddress(){
        try{
            return patient.getAddress().toString();
        }
        catch(Exception e){
            return "none";
        }
    }
    public String getEmail(){
        try{
            return patient.getTelecom().get(0).toString();
        }
        catch(Exception e){
            return "none";
        }
    }
    public String getTelephone(){
        try{
            return patient.getTelecom().get(1).toString();
        }
        catch(Exception e){
            return "none";
        }
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
