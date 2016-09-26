package us.codecraft.webmagic.samples.base.entity;

/**
 * 数据库URL基类，定义一些通用字段
 */
public class BaseUrl {

    /**URL的ID，自增长*/
    public Integer id;
    /**当前爬取的URL*/
    public String url;
    /**当前爬取URL的父级URL*/
    public String parentUrl;
    /**爬取URL返回的状态码*/
    public int status;
    /**站点*/
    public String site;
    /**此URL创建时间*/
    public String createTime;
    /**json格式，该URL携带的POST类型参数*/
    public String postParams;
    /**JSON格式，URL请求头信息*/
    public String headers;
    /**SON格式，网站cookie信息*/
    public String cookies;
    /**SON格式，需要由此页面带到下一个页面的参数类容*/
    public String otherParams;

}
