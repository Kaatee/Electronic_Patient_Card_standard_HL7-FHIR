package sample;


import javafx.scene.control.*;


public class PatientDetailsController {

    public Label textName;
    public Label textSex;
    public Label textBirth;
    public Label textAddress;
    public Label textEmail;
    public Label textTelephone;

    public void setTextStart( String name, String s,String b, String add, String em , String tel){
        this.textName.setText(name);
        this.textSex.setText(s);
        this.textBirth.setText(b);
        this.textAddress.setText(add);
        this.textEmail.setText(em);
        this.textTelephone.setText(tel);
    }

}
