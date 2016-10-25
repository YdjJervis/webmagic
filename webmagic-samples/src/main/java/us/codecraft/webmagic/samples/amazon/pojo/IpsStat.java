package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/24 11:20
 */
public class IpsStat {

    public Integer ipsStatId;
    public String ipsStatCondition;
    public String ipsStatStatus;
    public String ipsChangRecord;
    public Date ipsStatCreateDate;
    public Date ipsStatUpdateDate;

    public Integer getIpsStatId() {
        return ipsStatId;
    }

    public void setIpsStatId(Integer ipsStatId) {
        this.ipsStatId = ipsStatId;
    }

    public String getIpsStatCondition() {
        return ipsStatCondition;
    }

    public void setIpsStatCondition(String ipsStatCondition) {
        this.ipsStatCondition = ipsStatCondition;
    }

    public String getIpsStatStatus() {
        return ipsStatStatus;
    }

    public void setIpsStatStatus(String ipsStatStatus) {
        this.ipsStatStatus = ipsStatStatus;
    }

    public String getIpsChangRecord() {
        return ipsChangRecord;
    }

    public void setIpsChangRecord(String ipsChangRecord) {
        this.ipsChangRecord = ipsChangRecord;
    }

    public Date getIpsStatCreateDate() {
        return ipsStatCreateDate;
    }

    public void setIpsStatCreateDate(Date ipsStatCreateDate) {
        this.ipsStatCreateDate = ipsStatCreateDate;
    }

    public Date getIpsStatUpdateDate() {
        return ipsStatUpdateDate;
    }

    public void setIpsStatUpdateDate(Date ipsStatUpdateDate) {
        this.ipsStatUpdateDate = ipsStatUpdateDate;
    }
}