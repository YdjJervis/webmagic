package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * Asin
 */
public class Asin {

    /**
     * asin码
     */
    public String code;
    /**
     * 爬取状态，转换后进了URL表，这个状态就应该修改为已经爬取过
     */
    public int status;
    /**
     * 各个国家的站点名
     */
    public String site;
    /**
     * 优先级
     */
    public int priority;
}
