package sample;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import java.util.ArrayList;

public class Connection {//implements Runnable {

    private String serverBase;
    private IGenericClient client;
    private FhirContext ctx;
    private ArrayList<myPatient> patientList;
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
                //.where(Patient.NAME.matchesExactly().value("Andersson"))
                .sort().ascending(Patient.FAMILY)
                .returnBundle(Bundle.class)
                .execute();
    }

    private static void addInitial(Bundle theBundle, ArrayList<myPatient> thepatientList) {
        for (int i=0 ; i<theBundle.getEntry().size(); i++){
           // thepatientList.add((Patient) theBundle.getEntry().get(i).getResource());
           Patient thePatient = (Patient) theBundle.getEntry().get(i).getResource();
            String checkName=myParser.myGetName(thePatient);
            if(checkName.length()>1) {
              thepatientList.add(new myPatient(thePatient,checkName,thePatient.getId().toString()));
            }

        }
    }

    private static void addAnyResourcesNotAlreadyPresent( Bundle thePartialBundle, ArrayList<myPatient> thepatientList) {
        for (int i=0 ; i<thePartialBundle.getEntry().size(); i++){
          //  if (!thepatientList.contains((Patient) thePartialBundle.getEntry().get(i).getResource())) {
            //    thepatientList.add((Patient) thePartialBundle.getEntry().get(i).getResource());
            //}
            Patient thePatient = (Patient) thePartialBundle.getEntry().get(i).getResource();
            String checkName=myParser.myGetName(thePatient);
            if(checkName.length()>1) {
                //thepatientList.add(new myPatient(thePatient,checkName,thePatient.getId().toString()));
                myPatient my =new myPatient(thePatient,checkName,thePatient.getId().toString());
                    if (! thepatientList.contains(my)) {
                        thepatientList.add(my);
                    }

            }

        }
    }


    public void makeResult () {

        requestForServer();

        addInitial(responseFromServer, patientList);
        Bundle partialBundle = responseFromServer;
        for (; ; ) {
            if (partialBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                partialBundle = client.loadPage().next(partialBundle).execute();
                addAnyResourcesNotAlreadyPresent(partialBundle, patientList);
            } else {
                break;
            }
        }

        System.out.println("Pacjentow po wyszukiwaniu: " + patientList.size());

    }


    public ArrayList<myPatient> getPatientList() {
        return patientList;
    }
}
