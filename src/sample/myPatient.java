package sample;



import org.hl7.fhir.dstu3.model.Patient;

import java.text.SimpleDateFormat;

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

            if( patient.getGender().toString().length()>1 && patient.getGender().toString()!="[]")
                return patient.getGender().toString();
            else
                return "none";
        }
        catch(Exception e){
            return "none";
        }
    }

    public String getBirth(){
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if( patient.getBirthDate().toString().length()>1 && patient.getBirthDate().toString()!="[]")
                return dt1.format(patient.getBirthDate());
            else
                return "none";
        }
        catch(Exception e){
            return "none";
        }
    }

    public String getAddress(){
        try{
            if (patient.getAddress().get(0).getCountry().length()>2  && patient.getAddress().get(0).getCity().length() >2 )
                return patient.getAddress().get(0).getCountry() + ", " +patient.getAddress().get(0).getCity() ;
            else
                return "none";
        }
        catch(Exception e){
            return "none";
        }
    }
    public String getEmail(){
        try{
            if(patient.getTelecom().get(1).toString().length()>1 && patient.getTelecom().get(1).toString()!="[]")
                return patient.getTelecom().get(1).getValue();
            else
                return "none";
        }
        catch(Exception e){
            return "none";
        }
    }
    public String getTelephone(){
        try{
            if (patient.getTelecom().get(0).toString().length()>1 && patient.getTelecom().get(0).toString()!="[]")
                return patient.getTelecom().get(0).getValue();
            else
                return "none";
        }
        catch(Exception e){
            return "none";
        }
    }

    public String getIdMed(){
        String name;

        String [] arr = patient.getId().toString().split("/");
        name = arr[4] +"/" +arr[5];
        return name;

    }

    public String getIdNum(){
        String num;

        String [] arr = patient.getId().toString().split("/");
        num = arr[5];
        return num;

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
