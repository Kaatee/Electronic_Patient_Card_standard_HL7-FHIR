package sample;

import java.util.Date;

public class myObservation {
    private Date date;
    private String status;
    private String name;


    public myObservation(Date date, String status, String name){
        this.date=date;
        this.status = status;
        this.name=name;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
    public String getName() {
        return name;
    }

    public String printIt(){
        //     System.out.println("Observation: Data: " + getDate().toString() + " status: " + getStatus());
        String name;

        name ="Observation:\nData: " + getDate().toString() +"\nnazwa: "+getName()  + "\nstatus: " + getStatus() ;
        return name;
    }
}
