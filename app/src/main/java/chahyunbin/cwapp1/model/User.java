package chahyunbin.cwapp1.model;

public class User {
    public String id;
    private  String Name;
    private  String Phone;

    private  String BirthDay;
    private String Leader;
    public String Key;
    private String Mycell;
    public User() {

    }

    public User(String name, String phonenumber,  String birthday, String leader, String mycell) {
        Name = name;
        Phone = phonenumber;
        BirthDay = birthday;
        Leader = leader;
        Mycell = mycell;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phonenumber) {
        Phone = phonenumber;
    }



    public String getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(String birthDay) {
        BirthDay = birthDay;
    }

    public String getLeader() {
        return Leader;
    }

    public void setLeader(String leader) {
        Leader = leader;
    }
    public String getMycell(){return Mycell;}
    public void setMycell(String mycell){
        Mycell = mycell;
    }
}
