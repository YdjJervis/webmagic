package us.codecraft.webmagic.netsense.tianyan.pojo;

/**
 * 公司搜索结果列表
 */
public class CompanyResult {


    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CompanySearchResult{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
