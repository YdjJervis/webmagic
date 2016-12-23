package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:30
 */
public class CustomerBusiness {
    private int id;
    private String customerCode;
    private String businessCode;
    private int maxData;
    private int useData;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public int getMaxData() {
        return maxData;
    }

    public void setMaxData(int maxData) {
        this.maxData = maxData;
    }

    public int getUseData() {
        return useData;
    }

    public void setUseData(int useData) {
        this.useData = useData;
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