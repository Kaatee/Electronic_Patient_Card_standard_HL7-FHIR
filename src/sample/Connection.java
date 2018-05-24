package sample;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.r4.model.api.IBaseBundle;
import java.util.ArrayList;

public class Connection {//implements Runnable {

    private String serverBase;
    private IGenericClient client;
    private FhirContext ctx;
    private ArrayList<myPatient> patientList;

    public Connection()
    {
        this.ctx = FhirContext.forDstu3();
        this.serverBase="http://fhirtest.uhn.ca/baseDstu3";
        this.client = ctx.newRestfulGenericClient(serverBase);
        this.patientList  = new ArrayList<>();
    }

    private Bundle requestForServer (){
        //zapytanie
        Bundle responseFromServer = new Bundle();
        responseFromServer = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return responseFromServer;
    }

    private Bundle requestForServerSearch (String param){
        //zapytanie
        Bundle responseFromServer = new Bundle();
        responseFromServer = client
                .search()
                .forResource(Patient.class)
                .where(Patient.NAME.matches().value(param))
                .returnBundle(Bundle.class)
                .execute();

        return responseFromServer;
    }


    private static void addInitial(Bundle theBundle, ArrayList<myPatient> thepatientList) {
        for (int i=0 ; i<theBundle.getEntry().size(); i++){
            Patient thePatient = (Patient) theBundle.getEntry().get(i).getResource();
            String checkName=myParser.myGetName(thePatient);
            if(checkName.length()>2) {
                thepatientList.add(new myPatient(thePatient,checkName,thePatient.getId().toString()));
            }
            }
    }

    private static void addAnyResourcesNotAlreadyPresent( Bundle thePartialBundle, ArrayList<myPatient> thepatientList) {
        for (int i=0 ; i<thePartialBundle.getEntry().size(); i++){
            Patient thePatient = (Patient) thePartialBundle.getEntry().get(i).getResource();
            String checkName=myParser.myGetName(thePatient);
            if(checkName.length()>2) {

                myPatient my =new myPatient(thePatient,checkName,thePatient.getId().toString());
                if (! thepatientList.contains(my)) {
                    thepatientList.add(my);
                }
                }
                }
    }


    public void makeResult () {

         Bundle  my=  requestForServer();
        addInitial(my, patientList);
        Bundle partialBundle = my;
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


    public void makeResult (String param) {

        Bundle my=requestForServerSearch(param);
        addInitial(my, patientList);
        Bundle partialBundle = my;
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

    public IGenericClient getClient() {
        return client;
    }
}