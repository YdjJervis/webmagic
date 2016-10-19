package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 请求情况统计
 * @date 2016/10/17 18:30
 */
public class RequestStat extends BasePojo {
    /**
     * 测试条件
     */
    public String conditions;
    /**
     * 测试条件生成MD5
     */
    public String conditionsCode;
    /**
     * 第一次页面响应的时间
     */
    public Date firstPageTime;
    /**
     * 第一次出现验证的时间
     */
    public Date firstValidateTime;
    public int successCountBeforeValidate;
    /**
     * 出现验证后成功的次数
     */
    public int successCount;
    /**
     * 出现验证后需要验证码的次数
     */
    public int validateCount;
    /**
     * 总的请求次数
     */
    public int totalRequestCount;

    @Override
    public String toString() {
        return super.toString() + "RequestStat{" +
                "conditions='" + conditions + '\'' +
                ", conditionsCode='" + conditionsCode + '\'' +
                ", firstPageTime=" + firstPageTime +
                ", firstValidateTime=" + firstValidateTime +
                ", successCountBeforeValidate=" + successCountBeforeValidate +
                ", successCount=" + successCount +
                ", validateCount=" + validateCount +
                ", totalRequestCount=" + totalRequestCount +
                '}';
    }
}