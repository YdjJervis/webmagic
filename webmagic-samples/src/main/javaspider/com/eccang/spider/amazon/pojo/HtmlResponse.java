package com.eccang.spider.amazon.pojo;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/26 16:20
 */
public class HtmlResponse {

    private String htmlContent;
    private Integer statusCode;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}