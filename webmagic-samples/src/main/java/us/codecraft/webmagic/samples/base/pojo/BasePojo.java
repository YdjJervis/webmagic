package us.codecraft.webmagic.samples.base.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasePojo {

    private Integer id;
    private String url = "";
    private String createtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private String updatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

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
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                '}';
    }
}
