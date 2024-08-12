package mobile.wsmb2024.C10;

public class RideModel {
    public String icnum;
    public String date;
    public String time;
    public String origin;
    public String destination;
    public String fare;

    public RideModel(){

    }

    public RideModel(String icnum, String date, String time, String origin, String destination, String fare) {
        this.icnum = icnum;
        this.date = date;
        this.time = time;
        this.origin = origin;
        this.destination = destination;
        this.fare = fare;
    }


}
