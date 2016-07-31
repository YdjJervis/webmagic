package us.codecraft.webmagic.samples.base.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasePojo {

    private String url = "";
    private String updatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "BasePojo{" +
                "url='" + url + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
