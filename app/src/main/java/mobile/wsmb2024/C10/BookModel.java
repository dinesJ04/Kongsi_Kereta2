package mobile.wsmb2024.C10;

public class BookModel {
    public String icnum;
    public String date;
    public String time;
    public String origin;
    public String destination;
    public String fare;
    public String dName;
    public String dPhone;
    public String cModel;
    public String cNum;
    public String status;

    public BookModel(){

    }

    public BookModel(String icnum, String date, String time, String origin, String destination, String fare, String dName, String dPhone, String cModel, String cNum, String status) {
        this.icnum = icnum;
        this.date = date;
        this.time = time;
        this.origin = origin;
        this.destination = destination;
        this.fare = fare;
        this.dName = dName;
        this.dPhone = dPhone;
        this.cModel = cModel;
        this.cNum = cNum;
        this.status = status;
    }

}
