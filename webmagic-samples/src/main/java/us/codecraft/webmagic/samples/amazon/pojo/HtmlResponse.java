package us.codecraft.webmagic.samples.amazon.pojo;

import java.io.InputStream;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/26 16:20
 */
public class HtmlResponse {

    public InputStream mInputStream;
    public Integer statusCode;

    public InputStream getInputStream() {
        return mInputStream;
    }

    public void setInputStream(InputStream inputStream) {
        mInputStream = inputStream;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}