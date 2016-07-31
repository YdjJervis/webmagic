package us.codecraft.webmagic.samples.ttmeiju.pojo;

import java.util.UUID;

/**
 * 排行榜实体
 */
public class Summary {

    private String id;//剧名ID，唯一索引
    private String seri;//排行榜位置
    private String name;//名字
    private String status;//状态
    private String updateTime;//更新时间
    private String backTime;//回归时间
    private String leftTime;//倒计时
    private String url;

    public String getId() {
        return String.valueOf(UUID.randomUUID());
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(String leftTime) {
        this.leftTime = leftTime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id='" + id + '\'' +
                ", seri='" + seri + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", backTime='" + backTime + '\'' +
                ", leftTime='" + leftTime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
