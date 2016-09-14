package us.codecraft.webmagic.netsense.tianyan.pojo;

/**
 * 天眼查爬取失败的URL
 */
public class CompanyFailure {

    private String url;
    private String status;
    private String layer;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return "CompanyFailure{" +
                "url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", layer='" + layer + '\'' +
                '}';
    }
}
