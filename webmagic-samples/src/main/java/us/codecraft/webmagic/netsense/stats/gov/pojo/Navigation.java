package us.codecraft.webmagic.netsense.stats.gov.pojo;

public class Navigation {
    /**
     * dbcode : hgyd
     * id : A01
     * isParent : true
     * name : 价格指数
     * pid :
     * wdcode : zb
     */

    private String dbcode;
    private String id;
    private boolean isParent;
    private String name;
    private String pid;
    private String wdcode;

    public String getDbcode() {
        return dbcode;
    }

    public void setDbcode(String dbcode) {
        this.dbcode = dbcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getWdcode() {
        return wdcode;
    }

    public void setWdcode(String wdcode) {
        this.wdcode = wdcode;
    }

    @Override
    public String toString() {
        return "Navigation{" +
                "dbcode='" + dbcode + '\'' +
                ", id='" + id + '\'' +
                ", isParent=" + isParent +
                ", name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                ", wdcode='" + wdcode + '\'' +
                '}';
    }
}
