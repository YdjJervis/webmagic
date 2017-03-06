package com.eccang.amazon.processor;

import com.eccang.spider.base.util.PageUtil;
import com.eccang.spider.downloader.HttpClientRedisCacheDownloader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.Req;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试页面是否还能获取到数据
 */
public class StockProcessorTest implements PageProcessor {

    private static final Logger sLogger = Logger.getLogger(StockProcessorTest.class);

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {

        String url = page.getUrl().get();
        int type = (Integer)page.getRequest().getExtra("type");
        if (type == 0) {
            //通过产品首页url，解析放入购物车所需要的参数
            NameValuePair[] nameValuePairs = extraReqBuyBoxParam(page);
            String addToUrl = page.getHtml().xpath("//*[@id='addToCart']/@action").get();
            addToUrl = "https://www.amazon.com" + addToUrl;
            System.out.println(addToUrl);
            //组装放入购物车的请求Request对象
            Request request = new Request(addToUrl);
            Map nameValuePair = new HashMap();
            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.putExtra("type", 1);
            request.setMethod(HttpConstant.Method.POST);
            page.addTargetRequest(request);
        } else if (type == 1) {
            //放入购物车完成，并将购物车
            Request request = new Request("https://www.amazon.com/gp/cart/view.html/ref=lh_cart");
            request.putExtra("type", 2);
            request.setMethod(HttpConstant.Method.GET);
            page.addTargetRequest(request);
        } else if (type == 2) {

            //统计库存
            NameValuePair[] nameValuePairs = extraReqStockParam(page);
            Map nameValuePair = new HashMap();
            Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.putExtra("type", 3);
            request.setMethod(HttpConstant.Method.POST);
            page.addTargetRequest(request);

            //删除购物车
            NameValuePair[] nameValuePairs1 = extraReqDeleteCartParam1(page);
            Request request1 = new Request("https://www.amazon.com/gp/cart/view.html/ref=ord_cart_shr?ie=UTF8&app-nav-type=none&dc=df");
            Map nameValuePair1 = new HashMap();
            nameValuePair1.put("nameValuePair", nameValuePairs1);
            request1.setExtras(nameValuePair1);
            request1.putExtra("type", 4);
            request1.setMethod(HttpConstant.Method.POST);
            page.addTargetRequest(request1);

        } else if (type == 3) {
            //解析库存数
            sLogger.info(page.getJson().jsonPath("$..nav-cart.cartQty"));
        } else if(type == 4) {
            sLogger.info("delete cart finish.");
        }
    }

    /**
     * 通过产品首页url解析产品添加到购物车所需要的参数
     */
    private NameValuePair[] extraReqBuyBoxParam(Page page) {
        List<Selectable> selectables = page.getHtml().xpath("//form[@id='addToCart']/input[@type='hidden']").nodes();
        NameValuePair[] values = new NameValuePair[selectables.size()];
        String paramName;
        String paramValue;
        for (int i = 0; i < selectables.size(); i++) {
            paramName = selectables.get(i).xpath("//input[@type='hidden']/@name").get();
            paramValue = selectables.get(i).xpath("//input[@type='hidden']/@value").get();
            System.out.println(paramName + ":" + paramValue);
            values[i] = new BasicNameValuePair(paramName, paramValue);
        }
        return values;
    }

    /**
     * 通过购物车url解析请求获取库存所需要的参数
     */
    private NameValuePair[] extraReqStockParam(Page page) {
        String timeStamp = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='timeStamp']/@value").get();
        String token = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='token']/@value").get();
        String requestID = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='requestID']/@value").get();


        List<Selectable> selectables = page.getHtml().xpath("//div[@class='sc-list-body']/div").nodes();
        String asin = "";
        String actionItemID = "";
        String encodedOffering = "";
        if (CollectionUtils.isNotEmpty(selectables)) {

            asin = selectables.get(0).xpath("/div/@data-asin").get();
            actionItemID = selectables.get(0).xpath("/div/@data-itemid").get();
            encodedOffering = selectables.get(0).xpath("/div/@data-itemid").get();
        }

        return new NameValuePair[]{
                new BasicNameValuePair("hasMoreItems", "0"),
                new BasicNameValuePair("timeStamp", timeStamp),
                new BasicNameValuePair("token", token),
                new BasicNameValuePair("requestID", requestID),
                new BasicNameValuePair("addressId", "new"),
                new BasicNameValuePair("addressZip", ""),
                new BasicNameValuePair("closeAddonUpsell", "1"),
                new BasicNameValuePair("flcExpanded", "0"),
                new BasicNameValuePair("quantity." + actionItemID, "999"),
                new BasicNameValuePair("pageAction", "update-quantity"),
                new BasicNameValuePair("submit.update-quantity." + actionItemID, "1"),
                new BasicNameValuePair("actionItemID", actionItemID),
                new BasicNameValuePair("asin", asin),
                new BasicNameValuePair("encodedOffering", encodedOffering)
        };
    }

    /**
     * 删除购物车中的产品所需要的参数
     */
    private NameValuePair[] extraReqDeleteCartParam(Page page) {
        String timeStamp = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='timeStamp']/@value").get();
        String token = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='token']/@value").get();
        String requestID = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='requestID']/@value").get();


        List<Selectable> selectables = page.getHtml().xpath("//div[@class='sc-list-body']/div").nodes();
        String asin = "";
        String actionItemID = "";
        String encodedOffering = "";
        if (CollectionUtils.isNotEmpty(selectables)) {

            asin = selectables.get(0).xpath("/div/@data-asin").get();
            actionItemID = selectables.get(0).xpath("/div/@data-itemid").get();
            encodedOffering = selectables.get(0).xpath("/div/@data-itemid").get();
        }

        return new NameValuePair[]{
                new BasicNameValuePair("hasMoreItems", "0"),
                new BasicNameValuePair("timeStamp", timeStamp),
                new BasicNameValuePair("token", token),
                new BasicNameValuePair("requestID", requestID),
                new BasicNameValuePair("addressId", "new"),
                new BasicNameValuePair("addressZip", ""),
                new BasicNameValuePair("closeAddonUpsell", "1"),
                new BasicNameValuePair("flcExpanded", "0"),
                new BasicNameValuePair("submit.delete." + actionItemID, "999"),
                new BasicNameValuePair("pageAction", "delete-active"),
                new BasicNameValuePair("submit.update-quantity." + actionItemID, "1"),
                new BasicNameValuePair("actionItemID", actionItemID),
                new BasicNameValuePair("asin", asin),
                new BasicNameValuePair("encodedOffering", encodedOffering)
        };
    }

    private NameValuePair[] extraReqDeleteCartParam1(Page page) {
        String timeStamp = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='timeStamp']/@value").get();
        String token = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='token']/@value").get();
        String requestID = page.getHtml().xpath("//form[@id='activeCartViewForm']/input[@name='requestID']/@value").get();


        List<Selectable> selectables = page.getHtml().xpath("//div[@class='sc-list-body']/div").nodes();
        String asin = "";
        String actionItemID = "";
        String encodedOffering = "";
        if (CollectionUtils.isNotEmpty(selectables)) {

            asin = selectables.get(0).xpath("/div/@data-asin").get();
            actionItemID = selectables.get(0).xpath("/div/@data-itemid").get();
            encodedOffering = selectables.get(0).xpath("/div/@data-itemid").get();
        }

        return new NameValuePair[]{
                new BasicNameValuePair("fromAUI", "1"),
                new BasicNameValuePair("timeStamp", timeStamp),
                new BasicNameValuePair("token", token),
                new BasicNameValuePair("requestID", requestID),
                new BasicNameValuePair("activePage", "0"),
                new BasicNameValuePair("submit.delete." + actionItemID, "Delete"),
                new BasicNameValuePair("quantity", "1"),
                new BasicNameValuePair("quantityBox", "")
        };
    }

    @Override
    public Site getSite() {

        mSite.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        mSite.addHeader("Accept-Encoding","gzip, deflate, sdch, br");
        mSite.addHeader("Accept-Language", "zh-CN,zh;q=0.8");

        mSite.addHeader("Connection", "keep-alive");
        mSite.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8;");

        mSite.addHeader("Cookie", "session-token=OP4xb2u+G5tNbRXIxdM7lM24+W2jSWlGXP9OUZdwsjLzSdYpgHz1jGB4jvEYj+B7kpTiVfQQZF4JLFwmX39IxWMAK3lYuggT+7x2VI9yTHwnmR8T3OwFhxt4EG457jA838QVMLbIuKeAwOSnOtia2zTUUwL9q2Je5LjeNM9HIzMTGUExKjUwyeh7IuonmwWyzBxwfVJ+bcTluROTAS0ex8B6V/+XHnHofnbC6QKdU0+x+pZAc5vhrC3sFlgnFUVj; s_fid=0B7187A1EA88CDC2-3AE38042761E5C68; s_nr=1487670500719-Repeat; s_vnum=1488297600239%26vn%3D2; x-wl-uid=1tPBvr7FZaupy/lTveC3m7SDmAY3jaCyIsZSxxoGYxbOlltSSyPywF3VCD7TdKqtfKeqyIxqGXf/SerCKX5kezw==; Hm_lvt_407473d433e871de861cf818aa1405a1=1487832499,1487902952; Hm_lpvt_407473d433e871de861cf818aa1405a1=1487927201; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=163-4536560-8908815; csm-hit=YVDST007GTXG5WPA073P+s-XAH9R2NJBPMCQJVQF05X|1487986996064");

        mSite.addHeader("Host", "www.amazon.com");
        mSite.addHeader("Origin", "https://www.amazon.com");

        mSite.addHeader("X-AUI-View", "Desktop");

        mSite.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        mSite.addHeader("X-Requested-With", "XMLHttpRequest");

        return mSite;
    }

    public static void main(String[] args) {
        //统计库存
//        Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
        //放入购物车
//        Request request = new Request("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance");

        //https://www.amazon.com/WT671-Womens-Handkerchief-Tank-Tunic/dp/B01FIBPY42/ref=pd_cart_vw_1_2?_encoding=UTF8&psc=1&refRID=MGT7P7Z2ZAPDWPD02GT2
        Request request = new Request("https://www.amazon.com/WT671-Womens-Handkerchief-Tank-Tunic/dp/B01FIBPY42/ref=pd_cart_vw_1_2?_encoding=UTF8&psc=1&refRID=AP45716ER0HCVCC2BF5W");
        request.putExtra("type", 0);
        Spider.create(new StockProcessorTest())
                .thread(1)
                .addRequest(request)
                .start();
    }
}
