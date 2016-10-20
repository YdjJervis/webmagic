package us.codecraft.webmagic.amazon.processor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.DiscussPipeline;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试页面是否还能获取到数据
 */
public class PostProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {
        System.out.println("result");
        System.out.println(page.getHtml());
    }

    @Override
    public Site getSite() {
        mSite.addHeader("Cookie","x-wl-uid=1t1nJpfWfKAZ2Rd6vd2mO3L0lUFEvr5PIZQVEKuFD85C1MoWBEz6efpqQiyw/i1/k5x320zyyTgw=; session-token=\\\"H7G510zT+yN6DGE1+mGGLUuOokPEHdAFWtA/fAM866QtLK6Krp1iHK3gPxvostHJ+qc6i0KuJpKuiUV6dY1kSFjAIrvrLPr9NqXMKedqSiTKar7BjtU6SOI1AOY9Zcwu0qX1X1igdZgxxcNZhqzYdRt7KIC845xTMqfYvawMtxAc2i185OBN4pIHi3jkSNpA4ug9DIPNvxYSDlbqc3qXLJ27Sg6KOStbLgCy306ByQOU+i2Yq7EdoQ==\\\"; ubid-acbcn=479-6780614-0156018; session-id-time=2082729601l; session-id=452-7198359-1949433; csm-hit=DJC069KGTJ32HW812TMS+b-659KE86ZQC7NQJV67HC8|1474359942475");
        mSite.addHeader("Origin","https://www.amazon.cn");
        mSite.addHeader("X-AUI-View","Desktop");
        mSite.addHeader("Accept-Encoding","gzip, deflate");
        mSite.addHeader("Host","www.amazon.cn");
        mSite.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        mSite.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        mSite.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8;");
        mSite.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        mSite.addHeader("Referer","https://www.amazon.cn/gp/cart/view.html/ref=nav_cart");
        mSite.addHeader("X-Requested-With","XMLHttpRequest");
        mSite.addHeader("Connection","keep-alive");
        return mSite;
    }

    public static void main(String[] args) {

        Request request = new Request("https://www.amazon.cn/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
        NameValuePair[] values = new NameValuePair[2];
        values[0] = new BasicNameValuePair("hasMoreItems", "0");
        values[1] = new BasicNameValuePair("timeStamp", "1474358326");
        values[1] = new BasicNameValuePair("token", "gBL1QH1QN6BVc9asmjbPi7sR4hWyNb45iKMdwk0AAAAJAAAAAFfg8ltyYXcAAAAA");
        values[1] = new BasicNameValuePair("requestID", "659KE86ZQC7NQJV67HC8");
        values[1] = new BasicNameValuePair("activeItems", "C1EP1O4FX4E803%7C1%7C0%7C50%7C938%7C%7C%7C0%7C%7C");
        values[1] = new BasicNameValuePair("savedItems", "S387QMDGPEJ519%7C25.8%7C0");
        values[1] = new BasicNameValuePair("savedItems", "S114SG62AUJ6UU%7C1398%7C0");
        values[1] = new BasicNameValuePair("addressId", "");
        values[1] = new BasicNameValuePair("addressZip", "");
        values[1] = new BasicNameValuePair("closeAddonUpsell", "1");
        values[1] = new BasicNameValuePair("flcExpanded", "0");
        values[1] = new BasicNameValuePair("quantity.C1EP1O4FX4E803", "60");
        values[1] = new BasicNameValuePair("pageAction", "update-quantity");
        values[1] = new BasicNameValuePair("submit.update-quantity.C1EP1O4FX4E803", "1");
        values[1] = new BasicNameValuePair("actionItemID", "C1EP1O4FX4E803");
        values[1] = new BasicNameValuePair("asin", "B01AN9V9O8");
        values[1] = new BasicNameValuePair("encodedOffering", "hxDzFfcvuTJP4MRaXnA4U6XXvVKWSsIYZupzf3waFd7wtZzM2x4P3KMK7iw%252BqTBVYd69tiDFUUoZaxScuciI6STFSgXYpPqXZSIO1EFOkqq2eQ1ix3AZhrLgJIliWe%252Bl");

        Map nameValuePair = new HashMap();
        nameValuePair.put("nameValuePair", values);

        request.setExtras(nameValuePair);
        request.setMethod(HttpConstant.Method.POST);

        Spider.create(new PostProcessorTest())
                .addPipeline(new DiscussPipeline())
                .thread(1)
//                .addUrl("https://www.amazon.cn/dp/B013SMD0PI")
                .addRequest(request)
                .start();
    }
}
