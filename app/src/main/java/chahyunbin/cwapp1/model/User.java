package chahyunbin.cwapp1.model;

public class User {
    private  String Name;
    private  String Phonenumber;
    private  String Age;
    private  String Month;
    private  String Day;
    private String Leader;
    public String key;

    public User() {

    }

    public User(String name, String phonenumber, String age, String month, String day, String leader) {
        Name = name;
        Phonenumber = phonenumber;
        Age = age;
        Month = month;
        Day = day;
        Leader = leader;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getLeader() {
        return Leader;
    }

    public void setLeader(String leader) {
        Leader = leader;
    }
}
