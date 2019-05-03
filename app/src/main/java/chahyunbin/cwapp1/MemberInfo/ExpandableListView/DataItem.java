package chahyunbin.cwapp1.MemberInfo.ExpandableListView;

import java.util.List;

public class DataItem {
    private String categoryId;
    private String categoryName;
    private String isChecked = "YES";
    private List<SubCategoryItem> subCategory;

    public DataItem() {
    }

    public DataItem(String categoryId, String categoryName, String isChecked){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isChecked = isChecked;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public List<SubCategoryItem> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategoryItem> subCategory) {
        this.subCategory = subCategory;
    }
}
