package us.codecraft.webmagic.samples.base.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasePojo {

    public Integer id;
    public String createtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    public String updatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    public String extra;

    @Override
    public String toString() {
        return "BasePojo{" +
                "id=" + id +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
