package sample;

import java.util.Date;

public class myObservation {
    private Date date;
    private String status;

    public myObservation(Date date, String status){
        this.date=date;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void printIt(){
        System.out.println("Observation: Data: " + getDate().toString() + " status: " + getStatus());
    }
}
