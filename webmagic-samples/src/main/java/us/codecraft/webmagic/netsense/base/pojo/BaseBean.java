package us.codecraft.webmagic.netsense.base.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基础实体
 */
public class BaseBean {

    private String updatetime;

    public String getUpdatetime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
