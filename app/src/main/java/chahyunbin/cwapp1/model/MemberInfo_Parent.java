package chahyunbin.cwapp1.model;

public class MemberInfo_Parent {

    private String category_name;
    private String category;
    private String is_checked;

    public MemberInfo_Parent(String category_name, String category, String is_checked) {
        super();
        this.category_name = category_name;
        this.category = category;
        this.is_checked = is_checked;
    }

    public MemberInfo_Parent() {
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIs_checked() {
        return is_checked;
    }

    public void setIs_checked(String is_checked) {
        this.is_checked = is_checked;
    }
}
