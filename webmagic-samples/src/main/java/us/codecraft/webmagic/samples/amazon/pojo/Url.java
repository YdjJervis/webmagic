package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * URL实体
 */
public class Url extends BasePojo {

    public String siteCode;
    /**爬取URL返回的状态码*/
    public int status;
    /**当前爬取的URL*/
    public String url;
    /**当前爬取URL的父级URL*/
    public String parentUrl;
    /**json格式，该URL携带的POST类型参数*/
    public String postParams;
    /**JSON格式，URL请求头信息*/
    public String headers;
    /**SON格式，网站cookie信息*/
    public String cookies;
    /**SON格式，需要由此页面带到下一个页面的参数类容*/
    public String otherParams;

    public Asin asin;

    @Override
    public String toString() {
        return super.toString() + "BaseUrl{" +
                "siteCode='" + siteCode + '\'' +
                ", status=" + status +
                ", url='" + url + '\'' +
                ", parentUrl='" + parentUrl + '\'' +
                ", postParams='" + postParams + '\'' +
                ", headers='" + headers + '\'' +
                ", cookies='" + cookies + '\'' +
                ", otherParams='" + otherParams + '\'' +
                '}';
    }
}
