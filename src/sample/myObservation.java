package sample;

import java.util.Date;

public class myObservation {
    private Date date;
    private String status;
    private String name;
    private String result;


    public myObservation(Date date, String status, String name, int i, String result){
        if(date==null){
            this.date=new Date(System.currentTimeMillis() + i*1000L);
        }
        else
            this.date=date;
        this.status = status;
        this.name=name;
        this.result=result;
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

    public String getResult() {
        return result;
    }

    public String printIt(){
        //     System.out.println("Observation: Data: " + getDate().toString() + " status: " + getStatus());
        String name;

        name ="Observation:\nDate: " + getDate().toString() +"\nName: "+getName()  + "\nResult: "+getResult() + "\nStatus: " + getStatus()  ;
        return name;
    }
}
