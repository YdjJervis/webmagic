package us.codecraft.webmagic.netsense.tianyan.pojo;

/**
 * 新房
 */
public class Building{

    //SELECT developer,mgrcompany FROM t_ori_building LIMIT 1000
    private String developer;
    private String mgrcompany;

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getMgrcompany() {
        return mgrcompany;
    }

    public void setMgrcompany(String mgrcompany) {
        this.mgrcompany = mgrcompany;
    }

    @Override
    public String toString() {
        return "Building{" +
                "developer='" + developer + '\'' +
                ", mgrcompany='" + mgrcompany + '\'' +
                '}';
    }
}
