package sample;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public class myParser {

    public static String myGetName(Patient patient){
        String name = "";

        for (int i=0;i<patient.getName().size();i++){
            if(patient.getName().get(i).getFamilyAsSingleString().length()>0  )
                name = name + " " + patient.getName().get(i).getFamilyAsSingleString();
        }
        name+=" ";
        for (int i=0;i<patient.getName().size();i++){
            if(patient.getName().get(i).getGivenAsSingleString().length()>0)
                name = name + " " + patient.getName().get(i).getGivenAsSingleString();
        }

        return name;
    }

    public static myPatient searchMyPatient(Connection myCon, String id){
        myPatient result=null;
        for (int x=0 ; x <myCon.getPatientList().size() ; x++ ) {
            if (myCon.getPatientList().get(x).getId() == id) {
                result = myCon.getPatientList().get(x);
                break;
            }
        }

        return result;
    }


}
