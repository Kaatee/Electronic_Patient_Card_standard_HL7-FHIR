package sample;

import org.hl7.fhir.dstu3.model.Patient;

public class myParser {

    public static String myGetName(Patient patient){
        String name = "";


            for (int i = 0; i < patient.getName().size(); i++) {
                if (patient.getName().get(i).getFamily() !=null)
                    name = name + " " + patient.getName().get(i).getFamily();
            }
            name += " ";
            for (int i = 0; i < patient.getName().size(); i++) {
                if (patient.getName().get(i).getGivenAsSingleString()!=null)
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
