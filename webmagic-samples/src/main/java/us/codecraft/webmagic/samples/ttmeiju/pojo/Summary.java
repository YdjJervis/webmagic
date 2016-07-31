package us.codecraft.webmagic.samples.ttmeiju.pojo;

import us.codecraft.webmagic.samples.base.entity.BasePojo;

/**
 * 排行榜实体
 */
public class Summary extends BasePojo {

    private String seri;//排行榜位置
    private String name;//名字
    private String status;//状态
    private String updateDate;//更新时间
    private String backTime;//回归时间
    private String leftTime;//倒计时

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

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    @Override
    public String toString() {
        return "Summary{" +
                ", seri='" + seri + '\'' +
                ", name='" + name + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", status='" + status + '\'' +
                ", backTime='" + backTime + '\'' +
                ", leftTime='" + leftTime + '\'' +
                '}' + super.toString();
    }
}
