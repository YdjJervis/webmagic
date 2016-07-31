package us.codecraft.webmagic.samples.base.entity;

public class BasePojo {

    private String updatetime;
    private String url="";

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BasePojo{" +
                "updatetime='" + updatetime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
