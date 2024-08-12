package mobile.wsmb2024.C10;

public class UserModel {
    public String name;
    public String icnum;
    public String gender;
    public String phone;
    public String email;
    public String address;

    public String password;

    public UserModel(){

    }

    public UserModel(String name, String icnum, String gender, String phone, String email, String address,String password) {
        this.name = name;
        this.icnum = icnum;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
    }

}
