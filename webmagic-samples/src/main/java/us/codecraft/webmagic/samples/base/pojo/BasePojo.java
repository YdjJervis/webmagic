package us.codecraft.webmagic.samples.base.pojo;

import java.util.Date;

public class BasePojo {

    public Integer id;
    public Date createtime;
    public Date updatetime = new Date();
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
