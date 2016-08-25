package us.codecraft.webmagic.netsense.tianyan.pojo;

import us.codecraft.webmagic.netsense.base.pojo.BaseBean;

/**
 * 所有公司
 */
public class Company extends BaseBean {

    private String id = "";//公司ID，天眼查属性

    private String name;//公司名

    private String type = "";//类型，天眼查属性

    private String sort = "";//公司分类

    private int level = 0;//房地产企业排名

    private String area = "";//所在地区

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", sort='" + sort + '\'' +
                ", level=" + level +
                ", area='" + area + '\'' +
                '}';
    }
}
