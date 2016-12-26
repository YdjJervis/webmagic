package us.codecraft.webmagic.samples.amazon.pojo.dict;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 10:16
 */
public class IpsInfoManage {
    private int id;
    private int ipInfoId;
    private String ipDomain;
    private String basCode;
    private String ipHost;
    private String ipPort;
    private String ipVerifyUserName;
    private String ipVerifyPassword;
    private String urlHost;

    /*IP是否出现验证码*/
    private int isBlocked;

    private int isUsing;
    private int usedCount;
    private Date switchDate;
    private Date lastUsedDate;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIpInfoId() {
        return ipInfoId;
    }

    public void setIpInfoId(int ipInfoId) {
        this.ipInfoId = ipInfoId;
    }

    public String getIpDomain() {
        return ipDomain;
    }

    public void setIpDomain(String ipDomain) {
        this.ipDomain = ipDomain;
    }

    public String getBasCode() {
        return basCode;
    }

    public void setBasCode(String basCode) {
        this.basCode = basCode;
    }

    public String getUrlHost() {
        return urlHost;
    }

    public String getIpHost() {
        return ipHost;
    }

    public void setIpHost(String ipHost) {
        this.ipHost = ipHost;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getIpVerifyUserName() {
        return ipVerifyUserName;
    }

    public void setIpVerifyUserName(String ipVerifyUserName) {
        this.ipVerifyUserName = ipVerifyUserName;
    }

    public String getIpVerifyPassword() {
        return ipVerifyPassword;
    }

    public void setIpVerifyPassword(String ipVerifyPassword) {
        this.ipVerifyPassword = ipVerifyPassword;
    }

    public void setUrlHost(String urlHost) {
        this.urlHost = urlHost;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(int isUsing) {
        this.isUsing = isUsing;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public Date getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(Date switchDate) {
        this.switchDate = switchDate;
    }

    public Date getLastUsedDate() {
        return lastUsedDate;
    }

    public void setLastUsedDate(Date lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}