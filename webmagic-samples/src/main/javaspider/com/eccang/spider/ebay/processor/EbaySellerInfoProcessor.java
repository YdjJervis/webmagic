package com.eccang.spider.ebay.processor;

import com.eccang.spider.amazon.R;
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
        EbayUrl ebayUrl = getUrl(page);
        SellerInfo sellerInfo = extractSellerInfo(page, ebayUrl);
        if (sellerInfo != null) {
            if (mSellerInfoService.isExistSeller(sellerInfo.sellerName, ebayUrl.siteCode)) {
                sLogger.info("database has existed sellerName:" + sellerInfo.sellerName);
            } else {
                mSellerInfoService.add(sellerInfo);
                sLogger.info("add sellerName(" + sellerInfo.sellerName + ") success.");
            }
        } else {
            sLogger.info("产品url:" + getUrl(page).url + "中，不存在卖家信息.");
        }
    }

    private SellerInfo extractSellerInfo(Page page, EbayUrl ebayUrl) {
        SellerInfo sellerInfo = new SellerInfo();

        if (page.getHtml().xpath("//*[@id='bsi-c']").nodes().size() == 0) {
            return null;
        }
        sellerInfo.categoryName = ebayUrl.categoryName;
        sellerInfo.url = ebayUrl.url;
        sellerInfo.siteCode = ebayUrl.siteCode;
        sellerInfo.sellerName = page.getHtml().xpath("//*[@id='mbgLink']/span/text()").get();
        List<Selectable> selectables = page.getHtml().xpath("//*[@class='bscd']/div[@class='bsi-c1']/div").nodes();
        if (CollectionUtils.isNotEmpty(selectables)) {
            StringBuffer sb = new StringBuffer();
            String info = page.getHtml().xpath("//*[@id='bsi-c']/div[@class='bsi-cnt']/div[@class='bsi-bn']/text()").get();

            if (StringUtils.isNotEmpty(info) && info.equalsIgnoreCase("null")) {
                sb.append(page.getHtml().xpath("//*[@id='bsi-c']/div[@class='bsi-cnt']/div[@class='bsi-bn']/text()").get() + ";");
            }

            for (Selectable selectable : selectables) {
                sb.append(selectable.xpath("/div/text()").get() + ";");
            }
            sellerInfo.address = sb.toString();
        }

        List<Selectable> selects = page.getHtml().xpath("//*[@class='bscd']/div[@class='bsi-c2']/div").nodes();
        String contactName;
        String contactValue;
        if (CollectionUtils.isNotEmpty(selects)) {
            for (Selectable select : selects) {
                contactName = select.xpath("/div/span").nodes().get(0).xpath("/span/text()").get();
                contactValue = select.xpath("/div/span").nodes().get(1).xpath("/span/text()").get();
                extractContact(sellerInfo, contactName, contactValue, ebayUrl.siteCode);

            }
        }
        return sellerInfo;
    }

    /**
     * 解析联系方式（手机号，邮箱，传真）
     */
    private void extractContact(SellerInfo sellerInfo, String contactName, String contactValue, String siteCode) {
        if (StringUtils.isNotEmpty(contactName) && StringUtils.isNotEmpty(contactValue)) {
            if (siteCode.equalsIgnoreCase(R.SiteCode.US)) {
                if (contactName.toLowerCase().contains("phone")) {
                    sellerInfo.phone = contactValue;
                } else if (contactName.toLowerCase().contains("email")) {
                    sellerInfo.email = contactValue;
                } else if (contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            } else if (siteCode.equalsIgnoreCase(R.SiteCode.DE)) {
                if (contactName.toLowerCase().contains("telefon")) {
                    sellerInfo.phone = contactValue;
                } else if (contactName.toLowerCase().contains("e-mail")) {
                    sellerInfo.email = contactValue;
                } else if (contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            } else if (siteCode.equalsIgnoreCase(R.SiteCode.FR)) {
                if (contactName.toLowerCase().contains("téléphone")) {
                    sellerInfo.phone = contactValue;
                } else if (contactName.toLowerCase().contains("e-mail")) {
                    sellerInfo.email = contactValue;
                } else if (contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            } else if (siteCode.equalsIgnoreCase(R.SiteCode.IT)) {
                if (contactName.toLowerCase().contains("telefono")) {
                    sellerInfo.phone = contactValue;
                } else if (contactName.toLowerCase().contains("email")) {
                    sellerInfo.email = contactValue;
                } else if (contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            } else if (siteCode.equalsIgnoreCase(R.SiteCode.ES)) {
                if (contactName.toLowerCase().contains("teléfono")) {
                    sellerInfo.phone = contactValue;
                } else if (contactName.toLowerCase().contains("correo electrónico")) {
                    sellerInfo.email = contactValue;
                } else if (contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            } else if (siteCode.equalsIgnoreCase(R.SiteCode.CA)) {
                if(contactName.toLowerCase().contains("phone")) {
                    sellerInfo.phone = contactValue;
                } else if(contactName.toLowerCase().contains("email")) {
                    sellerInfo.email = contactValue;
                } else if(contactName.toLowerCase().contains("fax")) {
                    sellerInfo.fax = contactValue;
                }
            }
        }
    }


    @Override
    public void execute() {
        sLogger.info("开始执行 卖家信息 爬取任务...");
        List<EbayUrl> urlList = mEbayUrlService.findProductUrl(100);
        startToCrawl(urlList);
    }
}