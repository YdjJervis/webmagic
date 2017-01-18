package com.eccang.spider.ebay.processor;

import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.ebay.pojo.SellerInfo;
import com.eccang.spider.ebay.pojo.EbayUrl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 9:14
 */
@Service
public class EbaySellerInfoProcessor extends EbayProcessor implements ScheduledTask {

    @Override
    protected void dealOtherPage(Page page) {
        SellerInfo sellerInfo = extractSellerInfo(page);
        if(sellerInfo != null) {
            if (mSellerInfoService.isExistSeller(sellerInfo.sellerName)) {
                sLogger.info("database has existed sellerName:" + sellerInfo.sellerName);
            } else {
                mSellerInfoService.add(sellerInfo);
                sLogger.info("add sellerName(" + sellerInfo.sellerName + ") success.");
            }
        } else {
            sLogger.info("产品url:" + getUrl(page).url + "中，不存在卖家信息.");
        }
    }

    private SellerInfo extractSellerInfo(Page page) {
        SellerInfo sellerInfo = new SellerInfo();

        if(page.getHtml().xpath("//*[@id='bsi-c']").nodes().size() == 0) {
            return null;
        }
        EbayUrl url = getUrl(page);
        sellerInfo.categoryName = url.categoryName;
        sellerInfo.url = url.url;
        sellerInfo.sellerName = page.getHtml().xpath("//*[@id='mbgLink']/span/text()").get();
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='e12']//div[@class='bsi-c1']/div").nodes();

        if (CollectionUtils.isNotEmpty(selectables)) {
            StringBuffer sb = new StringBuffer();
            String info = page.getHtml().xpath("//*[@id='bsi-c']/div[@class='bsi-cnt']/div[@class='bsi-bn']/text()").get();

            if(StringUtils.isNotEmpty(info) && info.equalsIgnoreCase("null")) {
                sb.append(page.getHtml().xpath("//*[@id='bsi-c']/div[@class='bsi-cnt']/div[@class='bsi-bn']/text()").get() + ";");
            }

            for (Selectable selectable : selectables) {
                sb.append(selectable.xpath("/div/text()").get() + ";");
            }
            sellerInfo.address = sb.toString();
        }

        List<Selectable> selects = page.getHtml().xpath("//*[@id='e12']//div[@class='bsi-c2']/div").nodes();
        if (CollectionUtils.isNotEmpty(selects)) {
            for (Selectable select : selects) {

                String contactName = select.xpath("/div/span").nodes().get(0).xpath("/span/text()").get();
                String contactValue = select.xpath("/div/span").nodes().get(1).xpath("/span/text()").get();
                if (StringUtils.isNotEmpty(contactName) && StringUtils.isNotEmpty(contactValue)) {
                    if (contactName.toLowerCase().contains("phone")) {
                        sellerInfo.phone = contactValue;
                    } else if (contactName.toLowerCase().contains("email")) {
                        sellerInfo.email = contactValue;
                    } else if (contactName.toLowerCase().contains("fax")) {
                        sellerInfo.fax = contactValue;
                    }
                }
            }
        }

        return sellerInfo;
    }

    @Override
    public void execute() {
        sLogger.info("开始执行 卖家信息 爬取任务...");
        List<EbayUrl> urlList = mEbayUrlService.findProductUrl(15);
        startToCrawl(urlList);
    }
}