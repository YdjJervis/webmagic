package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 推送队列
 * @date 2016/12/5 11:00
 */
public class PushQueue extends BasePojo{

    public String batchNum;
    /**
     * 推送状态。0-未推送/1-推送中/2-推送成功/3-推送失败
     */
    public int status;
    /**
     * 推送次数
     */
    public int times;


}