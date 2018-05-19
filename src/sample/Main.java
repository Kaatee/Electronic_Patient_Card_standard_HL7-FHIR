package sample;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main{ //extends Application {

    /*
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }*/

    private static void addInitialUrlsToSet(Bundle theBundle, Set<Patient> theResourcesAlreadyAdded) {
//        for (Bundle.Entry entry : theBundle.getEntry()) {
//            theResourcesAlreadyAdded.add(entry.getResource());
//        }

        for (int i=0 ; i<theBundle.getEntry().size(); i++){
            theResourcesAlreadyAdded.add((Patient) theBundle.getEntry().get(i).getResource());
        }
    }

    private static void addAnyResourcesNotAlreadyPresentToBundle( Bundle thePartialBundle, Set<Patient> theResourcesAlreadyAdded) { //Bundle theAggregatedBundle,
//        for (Bundle.Entry entry : thePartialBundle.getEntry()) {
//            if (!theResourcesAlreadyAdded.contains(entry.getFullUrl())) {
//                theResourcesAlreadyAdded.add(entry.getFullUrl());
//                theAggregatedBundle.getEntry().add(entry);
//            }
//        }

        for (int i=0 ; i<thePartialBundle.getEntry().size(); i++){
            //theResourcesAlreadyAdded.add((Patient) theBundle.getEntry().get(i).getResource());
            if (!theResourcesAlreadyAdded.contains((Patient) thePartialBundle.getEntry().get(i).getResource())) {
                theResourcesAlreadyAdded.add((Patient) thePartialBundle.getEntry().get(i).getResource());
              // theAggregatedBundle.getEntry().add((Patient) thePartialBundle.getEntry());
           }
           }
    }
    public static void main(String[] args) {
        // launch(args);
        // We're connecting to a DSTU1 compliant server in this example
        FhirContext ctx = FhirContext.forDstu2();
        String serverBase = "http://fhirtest.uhn.ca/baseDstu2";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        Bundle results = client
                .search()
                .forResource(Patient.class)
                .where(Patient.FAMILY.matches().value("Smith"))
                .returnBundle(Bundle.class)
                .execute();
        System.out.println("Found " + results.getEntry().size()+ " patients named 'Smith'");

        Set<Patient> resourcesAlreadyAdded = new HashSet<Patient>();
        addInitialUrlsToSet(results, resourcesAlreadyAdded);
        Bundle partialBundle = results;
        for (;;) {
            if (partialBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                partialBundle = client.loadPage().next(partialBundle).execute();
                addAnyResourcesNotAlreadyPresentToBundle( partialBundle, resourcesAlreadyAdded);
            } else {
                break;
            }
        }

        System.out.println(resourcesAlreadyAdded.size());


        for (Iterator<Patient> it = resourcesAlreadyAdded.iterator(); it.hasNext(); ) {
            Patient f = it.next();
            System.out.println(f.getName());
            System.out.println(f.getId());
        }

    }
}
