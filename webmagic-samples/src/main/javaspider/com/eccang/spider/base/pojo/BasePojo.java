package com.eccang.spider.base.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 基础实体类，所有有关数据表的类都必须继承这个
 * @date 2016/10/11
 */
public class BasePojo implements Serializable{

    public Integer id;
    public Date createTime;
    public Date updateTime;
    public String extra;

    @Override
    public String toString() {
        return "BasePojo{" +
                "id=" + id +
                ", createtime='" + createTime + '\'' +
                ", updatetime='" + updateTime + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
