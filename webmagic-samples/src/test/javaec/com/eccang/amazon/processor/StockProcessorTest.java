package com.eccang.amazon.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试页面是否还能获取到数据
 */
public class StockProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        if(url.matches(".*/dp/[0-9A-Za-z]*.*")) {
            //通过产品首页url，解析放入购物车所需要的参数
            NameValuePair[] nameValuePairs = extraReqBuyBoxParam(page);

            //组装放入购物车的请求Request对象
            Request request = new Request("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance");
            Map nameValuePair = new HashMap();
            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.setMethod(HttpConstant.Method.POST);
            page.addTargetRequest(request);
        }

        if(url.contains("handle-buy-box")) {
            //放入购物车完成，并将购物车
            Request request = new Request("https://www.amazon.com/gp/cart/view.html/ref=lh_cart");
            request.setMethod(HttpConstant.Method.GET);
            page.addTargetRequest(request);
        }

        if(url.matches(".*/gp/cart/view.html/.*")) {
            //统计库存
            NameValuePair[] nameValuePairs = extraReqStockParam(page);
            Map nameValuePair = new HashMap();            Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");

            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.setMethod(HttpConstant.Method.POST);
            page.addTargetRequest(request);
        }

        if(url.matches(".*/gp/cart/ajax-update.html/.*")) {
            //解析库存数
            System.out.println(page.getHtml());
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
        if(CollectionUtils.isNotEmpty(selectables)) {

            asin = selectables.get(0).xpath("/div/@data-asin").get();
            actionItemID = selectables.get(0).xpath("/div/@data-itemid").get();
            encodedOffering = selectables.get(0).xpath("/div/@data-itemid").get();
        }

        return new NameValuePair[] {
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

    @Override
    public Site getSite() {
        mSite.addHeader("Cookie","x-wl-uid=1S3UB0PWRfWVtoMS0L5JeBl+mARwPVccIThGcklbaHfdKvGRhYYSWH+vQ/sXY9tB9H52mQdkhbcY=; session-token=YqXmKsodz92qG5UBbyIdxAVzRJoVfkvECfv26X8QOXchNrjqG+mtFeaLc96hZEZOkQ7QmY4fqVhvSUz8fyfrB+ZnlUYl28vSsP012zrXe/wlC0LgNiThhcAvQzTrbDaHR9TE2H3WssXdRC1l+7SltTwMHDruu6eCzZCVVeU9sVEaNc5VXDq4K4hojoOADmNpTrSHFhq3A11/IrOgCVe3wud6JXohsHz83N7wLzuUBIPC6fgxuRVXN0A+DssiyqbR; skin=noskin; JSESSIONID=9B1CF5EDD0F931A2D9B21E0F9D436F7B; csm-hit=NKSR5YFXBGR2H0Z3JA49+sa-G7FF4DS97V3WDAMEENW0-E3ETKH5D4EA3ABTKBK65|1483928446132; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=167-8229845-7101626");
        mSite.addHeader("Origin","https://www.amazon.cn");
        mSite.addHeader("X-AUI-View","Desktop");
        mSite.addHeader("Accept-Encoding","gzip, deflate, sdch, br");
        mSite.addHeader("Host","www.amazon.com");
        mSite.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        mSite.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
        mSite.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8;");
        mSite.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        mSite.addHeader("Referer","https://www.amazon.com/gp/cart/view.html/ref=lh_cart_vc_btn");
        mSite.addHeader("X-Requested-With","XMLHttpRequest");
        mSite.addHeader("Connection","keep-alive");
        mSite.addHeader("Cookie","x-wl-uid=1S3UB0PWRfWVtoMS0L5JeBl+mARwPVccIThGcklbaHfdKvGRhYYSWH+vQ/sXY9tB9H52mQdkhbcY=; session-token=YqXmKsodz92qG5UBbyIdxAVzRJoVfkvECfv26X8QOXchNrjqG+mtFeaLc96hZEZOkQ7QmY4fqVhvSUz8fyfrB+ZnlUYl28vSsP012zrXe/wlC0LgNiThhcAvQzTrbDaHR9TE2H3WssXdRC1l+7SltTwMHDruu6eCzZCVVeU9sVEaNc5VXDq4K4hojoOADmNpTrSHFhq3A11/IrOgCVe3wud6JXohsHz83N7wLzuUBIPC6fgxuRVXN0A+DssiyqbR; skin=noskin; JSESSIONID=82B90305ECF6ED7C7F70591986966752; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=167-8229845-7101626; csm-hit=JWXD2JR17EQH442H6EVX+s-G0STFW7E5VST2XMTBBEQ|1483944812002");

        return mSite;
    }

    public static void main(String[] args) {
        //统计库存
//        Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
        //放入购物车
//        Request request = new Request("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance");

        Request request = new Request("https://www.amazon.com/dp/B01DFKC2SO?psc=1");

        Spider.create(new StockProcessorTest())
                .thread(1)
                .addRequest(request)
                .start();
    }
}
