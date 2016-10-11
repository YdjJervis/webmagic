package us.codecraft.webmagic.samples.base.pojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 基础实体类，所有有关数据表的类都必须继承这个
 * @date 2016/10/11
 */
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
