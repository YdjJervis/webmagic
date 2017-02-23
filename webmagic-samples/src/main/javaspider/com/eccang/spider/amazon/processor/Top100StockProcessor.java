package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.amazon.pojo.top100.StockUrl;
import com.eccang.spider.amazon.service.top100.SellingProductService;
import com.eccang.spider.amazon.service.top100.StockUrlService;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/21 15:24
 */
@Service
public class Top100StockProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private StockUrlService mStockUrlService;
    @Autowired
    private SellingProductService mSellingProductService;

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {

        StockUrl stockUrl = getStockUrl(page);
        sLogger.info("process(Page page)::URL=" + page.getUrl() + " StatusCode=" + page.getStatusCode());
        page = IsNotNullAndProxyCaptcha(page);

        updateUrlStatus(page, true);

        if (isPage404(page) && stockUrl.type == R.StockCrawlUrlType.PRODUCT_URL) {
            updateSellingProduct(stockUrl);
        } else if (isPage404(page) && stockUrl.type != R.StockCrawlUrlType.PRODUCT_URL) {
            Request request = page.getRequest();
            page.addTargetRequest(request);
        } else if (mSet.contains(page.getStatusCode())) {
            sLogger.info("解析url,返回状态为异常码，不做任何操作.");
        } else if (isValidatePage(page)) {
            dealValidate(page);
        } else {
            dealOtherPage(page);
        }

    }

    @Override
    protected void dealOtherPage(Page page) {
        StockUrl stockUrl = getStockUrl(page);

        SellingProduct sellingProduct = initSellingProduct(stockUrl.batchNum, stockUrl.asin, stockUrl.siteCode);
        if (stockUrl.type == R.StockCrawlUrlType.PRODUCT_URL) {

            //通过产品首页url，解析放入购物车所需要的参数
            NameValuePair[] nameValuePairs = extraReqBuyBoxParam(page);

            /*解析加入购物车的请求url*/
            com.eccang.spider.amazon.pojo.dict.Site site = mSiteService.find(stockUrl.siteCode);
            String addToCartUrl = page.getHtml().xpath("//*[@id='addToCart']/@action").get();

            if (StringUtils.isEmpty(addToCartUrl)) {
                sLogger.info("产品（" + stockUrl.pUrl + "),不存在购物车.");
                /*更新产品库存抓取状态*/
                sellingProduct.status = R.StockCrawlStatus.FINISH;
                mSellingProductService.updateStock(sellingProduct);
                return;
            }

            addToCartUrl = site.site + addToCartUrl;

            //组装放入购物车的请求Request对象
            Request request = new Request(addToCartUrl);
            StockUrl nextStepUrl = initStockUrl(addToCartUrl, stockUrl);

            Map nameValuePair = new HashMap();
            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.setMethod(HttpConstant.Method.POST);

            request.putExtra(URL_EXTRA, nextStepUrl);
            page.addTargetRequest(request);

            sellingProduct.status = R.StockCrawlStatus.ADD_TO_CART;
        } else if (stockUrl.type == R.StockCrawlUrlType.ADD_TO_CART) {

            //放入购物车完成，并将购物车url放入请求中
            com.eccang.spider.amazon.pojo.dict.Site site = mSiteService.find(stockUrl.siteCode);
            String cartUrl = page.getHtml().xpath("//*[@id='nav-cart']/@href").get();
            Request request = new Request(cartUrl);
            StockUrl nextStepUrl = initStockUrl(cartUrl, stockUrl);
            request.putExtra(URL_EXTRA, nextStepUrl);
            request.setMethod(HttpConstant.Method.GET);

            page.addTargetRequest(request);

        } else if (stockUrl.type == R.StockCrawlUrlType.CART_URL) {

            //统计库存
            NameValuePair[] nameValuePairs = extraReqStockParam(page);
            Map nameValuePair = new HashMap();
            Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
            StockUrl nextStepUrl = initStockUrl("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60", stockUrl);

            nameValuePair.put("nameValuePair", nameValuePairs);
            request.setExtras(nameValuePair);
            request.setMethod(HttpConstant.Method.POST);

            request.putExtra(URL_EXTRA, nextStepUrl);

            page.addTargetRequest(request);

            sellingProduct.status = R.StockCrawlStatus.COUNT_STOCK;
        } else if (stockUrl.type == R.StockCrawlUrlType.COUNT_STOCK_URL) {

            //解析库存数
            String stockNum = page.getJson().jsonPath("$..nav-cart.cartQty").get();
            System.out.println(stockNum);
            sellingProduct.stock = Integer.valueOf(stockNum);
            sellingProduct.status = R.StockCrawlStatus.FINISH;
        }
        mSellingProductService.updateStock(sellingProduct);
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

    private StockUrl getStockUrl(Page page) {
        return (StockUrl) page.getRequest().getExtra(URL_EXTRA);
    }

    @Override
    void updateUrlStatus(Page page, boolean needTimeAdd) {
        StockUrl stockUrl = getStockUrl(page);

        if (needUpdateStatus()) {
            int statusCode = page.getStatusCode();

            sLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);
            stockUrl.status = statusCode;
        }

        if(stockUrl.type == R.StockCrawlUrlType.COUNT_STOCK_URL) {
            stockUrl.crawling = 0;
        }

        stockUrl.times++;

        sLogger.info("改变状态后的Url对象：" + stockUrl);

        mStockUrlService.updateById(stockUrl);
    }

    private void dealValidate(Page page) {

        String validateUrl = getValidateImgUrl(page);

        if (StringUtils.isNotEmpty(validateUrl)) {
            if (page.getRequest() != null && page.getRequest().getExtra("ipsType") != null && page.getRequest().getExtra("host") != null) {
                mIpsInfoManageService.switchIp((String) page.getRequest().getExtra("ipsType"), (String) page.getRequest().getExtra("host"), -1);
            }
        }

        sLogger.warn("出现验证码：" + page.getUrl() + "  " + page.getStatusCode());

        /*保存图片验证码*/
        //PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");

        Request request;
        StockUrl url = (StockUrl) page.getRequest().getExtra(URL_EXTRA);

        /*
        * 请求表单的Url，调用验证码识别接口
        */
        if (page.getRequest().getExtra("ipsType") == null) {
            request = new Request(getValidatedUrl(page));
            request.putExtra(URL_EXTRA, url);
            page.addTargetRequest(request);
        }
        /* 当需要验证码时，page的状态码还是200，不符合我们的逻辑，所以修改一下 */
        page.setStatusCode(0);
        updateUrlStatus(page, false);
    }

    /**
     * 更新top100产品抓取库存状态
     */
    private void updateSellingProduct(StockUrl stockUrl) {

        SellingProduct sellingProduct = new SellingProduct();
        sellingProduct.batchNum = stockUrl.batchNum;
        sellingProduct.asin = stockUrl.asin;
        sellingProduct.siteCode = stockUrl.siteCode;
        sellingProduct.status = R.StockCrawlStatus.OFF_SHELF;
        mSellingProductService.updateByObj(sellingProduct);
    }

    private StockUrl initStockUrl(String url, StockUrl parentUrl) {

        StockUrl stockUrl = new StockUrl();
        stockUrl.batchNum = parentUrl.batchNum;
        stockUrl.url = url;
        stockUrl.times = parentUrl.times;
        stockUrl.urlMD5 = UrlUtils.md5(url);
        stockUrl.type = parentUrl.type + 1;
        stockUrl.id = parentUrl.id;

        if (parentUrl.type == R.StockCrawlUrlType.PRODUCT_URL) {
            stockUrl.pUrl = parentUrl.url;
        } else {
            stockUrl.pUrl = parentUrl.pUrl;
        }

        stockUrl.asin = parentUrl.asin;
        stockUrl.siteCode = parentUrl.siteCode;
        return stockUrl;
    }

    private SellingProduct initSellingProduct(String batchNum, String asin, String siteCode) {

        SellingProduct sellingProduct = new SellingProduct();
        sellingProduct.batchNum = batchNum;
        sellingProduct.asin = asin;
        sellingProduct.siteCode = siteCode;
        return sellingProduct;
    }

    private boolean isPage404(Page page) {
        return page.getStatusCode() == 404 || page.getHtml().get().contains("404.html");
    }


    @Override
    public void execute() {
        List<StockUrl> stockUrls = mStockUrlService.findNeed2ParseUrl(1);
        startToTask(stockUrls);
    }


    @Override
    public Site getSite() {
        mSite.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        mSite.addHeader("Accept-Encoding","gzip, deflate, sdch, br");
        mSite.addHeader("Accept-Language", "zh-CN,zh;q=0.8");

        mSite.addHeader("Connection", "keep-alive");
        mSite.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8;");

        mSite.addHeader("Cookie", "skin=noskin; JSESSIONID=DB05F4D8C96889AEC0B9719AD5FC156F; session-token=\"P/R/tKjAhzKIUN/+bKQY2mlv+EMNtdz6LCVxwYGfvU5fKryaFlakSEzm66a+wteWVcmaQ0XEPjmlmKBi02W5p15kI830CIKxUJ6b2ZE6r9Y5n5wqzCYR4PAFLFQlZydwsyBGkM3BYN20d57D3I8GLUnmguwl6Is7wBSu/GLlxBVFjdEyNQXPUFzkiKMfmUioxHb4ILBI/fGbadQgmymLWYZiPUnTLpqj6Di833sX0YiC1morDkZEQcNiK2Bf8hxc7XWt0H53owbVlwFUF8ze5w==\"; x-main=RDA19prtSRN2RewvMfr97vc44ABzZa2iq5d0dv9k24kLRTUqM4ur1jEBORgArAH7; at-main=Atza|IwEBIDKra_msTzik4A-4iZrZQ67YH7OMUHFrQlLquGRjTqe4perMQvzDatwk8ZFFlkCNgwq8F2hRaQM-3vgaCz3-DiDsZioqzWpuBF-OLK0stSI5vxiuWBpPdEIQMW07tKOS1w2SCK5wdbO17ax_hm7MmwSN7oRXTK7N-eh6Ufi7zCcyNLyFsKlwBhF2uJQp6PSTNNmb7ZXbFSxe3jKzvy337OzHykuDgFTfy508d06Cp4ACB-bh3K9SR59952CWXnL4DlNVQJEu5Ukd56d4m3dZx-YGXLirf5itmOUIEAP13IMhiuq93gxRDW65uCd2zbLF8_dqG6L9fm-bFmUQinI1rWUpATrjWQd9V3ggnW9v0X76FM9P9XZa5PNMG_1Qy8C05BRbDKpsr61aGa8s3IfKoqVTZ33wjXLulnj73zyfCh6aKQ; sess-at-main=\"NaGiOgG2TVI5pzlpfGI1z8+Wyy7jifJvC+O7iX3SvQQ=\"; lc-main=en_US; x-wl-uid=1A7mJEhIOz2Ii5UlfqXg27YDkS+2UBCkAxtvbn0GIiUZ2ooNZw+eBwGYqAXzvrqGBf4Idc217C8OXw8f844cug2uo2WgNzqxdE+Gs4o795yJGFl2lpYB4iKgYHfun47k+B00W7iAIWO4=; csm-hit=BFNHJNQF27DE58K31K7R+b-7QNMX335ET5B179K3PCX|1487643282453; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=167-8229845-7101626");

        mSite.addHeader("Host", "www.amazon.com");
        mSite.addHeader("Origin", "https://www.amazon.com");

        mSite.addHeader("X-AUI-View", "Desktop");
        mSite.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");

        mSite.addHeader("X-Requested-With", "XMLHttpRequest");
        return mSite;
    }

    private void startToTask(List<StockUrl> urlList) {
        sLogger.info("找到状态码不为200的Url个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {

            Spider mSpider = Spider.create(this)
                    .setDownloader(mHttpClientRedisCacheDownloader)
                    .thread(1);

            for (StockUrl stockUrl : urlList) {
                Request request = new Request(stockUrl.url);
                request.putExtra(URL_EXTRA, stockUrl);
                mSpider.addRequest(request);

                stockUrl.crawling = 1;
                mStockUrlService.update(stockUrl);
            }

            sLogger.info("开始爬取库存...");
            mSpider.start();
        }
    }
}