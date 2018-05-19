package sample;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import java.util.ArrayList;

public class Connection {

    private String serverBase;
    private IGenericClient client;
    private FhirContext ctx;
    private ArrayList<Patient> patientList;
    private Bundle responseFromServer;

    public Connection()
    {
        this.ctx = FhirContext.forDstu2();
        this.serverBase="http://fhirtest.uhn.ca/baseDstu2";
        this.client = ctx.newRestfulGenericClient(serverBase);
        this.patientList  = new ArrayList<>();
    }

    private void requestForServer (){
        //zapytanie
        responseFromServer = client
                .search()
                .forResource(Patient.class)
               // .where(Patient.FAMILY.matches().value("Smith"))
                .returnBundle(Bundle.class)
                .execute();
    }

    private static void addInitialUrlsToSet(Bundle theBundle, ArrayList<Patient> thepatientList) {
        for (int i=0 ; i<theBundle.getEntry().size(); i++){
            thepatientList.add((Patient) theBundle.getEntry().get(i).getResource());
        }
    }

    private static void addAnyResourcesNotAlreadyPresentToBundle( Bundle thePartialBundle, ArrayList<Patient> thepatientList) {
        for (int i=0 ; i<thePartialBundle.getEntry().size(); i++){
            if (!thepatientList.contains((Patient) thePartialBundle.getEntry().get(i).getResource())) {
                thepatientList.add((Patient) thePartialBundle.getEntry().get(i).getResource());
            }
        }
    }


    public void makeResult () {

        requestForServer();

        addInitialUrlsToSet(responseFromServer, patientList);
        Bundle partialBundle = responseFromServer;
        for (; ; ) {
            if (partialBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                partialBundle = client.loadPage().next(partialBundle).execute();
                addAnyResourcesNotAlreadyPresentToBundle(partialBundle, patientList);
            } else {
                break;
            }
        }

        System.out.println("Pacjentow po wyszukiwaniu: " + patientList.size());
//        for (Iterator<Patient> it = patientList.iterator(); it.hasNext(); ) {
//            Patient f = it.next();
//         ;
//            System.out.println(f.getName());
//
//        }
//        patientList.forEach((myPatient)->{   System.out.println("--- PATIENT  ---");
//            System.out.println(myPatient.getName());});




    }


    public ArrayList<Patient> getPatientList() {
        return patientList;
    }
}
