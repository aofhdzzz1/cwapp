package chahyunbin.cwapp1.model;

public class User {
    public String id;
    private  String Name;
    private  String Phone;
    private  String Age;
    private  String Month;
    private  String Day;
    private String Leader;
    public String key;

    public User() {

    }

    public User(String name, String phonenumber, String age, String month, String day, String leader) {
        Name = name;
        Phone = phonenumber;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phonenumber) {
        Phone = phonenumber;
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
