package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户表实体
 * @date 2016/11/8 17:30
 */
public class Customer extends BasePojo {

    public String code;
    public int status;
    public String platformCode;
    public String name;
    public String address;
    public String phone;
    public String email;
    public String legalPerson;
    public String introduce;

    @Override
    public String toString() {
        return "Customer{" +
                "code='" + code + '\'' +
                ", status=" + status +
                ", platformCode='" + platformCode + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}