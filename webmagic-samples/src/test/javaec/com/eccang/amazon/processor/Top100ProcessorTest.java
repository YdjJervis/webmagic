package com.eccang.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试页面是否还能获取到数据
 */
public class Top100ProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_centerListWrapper']/div[@class='zg_itemImmersion']").nodes();
        for (Selectable node : nodes) {
            String selectable = node.xpath("//div[@class='zg_itemWrapper']/div[contains(@class, 'p13n-asin')]/@data-p13n-asin-metadata").get();
            String asin = new Json(selectable).jsonPath("$.asin").get();
            System.out.println(asin);

        }
    }

    @Override
    public Site getSite() {
        mSite.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        mSite.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        mSite.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        mSite.addHeader("Cache-Control", "max-age=0");
        mSite.addHeader("Connection", "keep-alive");
        mSite.addHeader("Cookie", "x-wl-uid=1S3UB0PWRfWVtoMS0L5JeBl+mARwPVccIThGcklbaHfdKvGRhYYSWH+vQ/sXY9tB9H52mQdkhbcY=; session-token=YqXmKsodz92qG5UBbyIdxAVzRJoVfkvECfv26X8QOXchNrjqG+mtFeaLc96hZEZOkQ7QmY4fqVhvSUz8fyfrB+ZnlUYl28vSsP012zrXe/wlC0LgNiThhcAvQzTrbDaHR9TE2H3WssXdRC1l+7SltTwMHDruu6eCzZCVVeU9sVEaNc5VXDq4K4hojoOADmNpTrSHFhq3A11/IrOgCVe3wud6JXohsHz83N7wLzuUBIPC6fgxuRVXN0A+DssiyqbR; skin=noskin; JSESSIONID=7B64AC7E1F8D360029513E7971C6E15C; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=167-8229845-7101626; csm-hit=112NPSAWX27YV622TV18+s-68BSFQSS9VHVSGWQZ6QG|1484532663381");
        mSite.addHeader("Host", "www.amazon.com");
        mSite.addHeader("Upgrade-Insecure-Requests", "1");
        mSite.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");

        return mSite;
    }


    private void extractTop100ProductInfo(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_centerListWrapper']/div[@class='zg_itemImmersion']").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (Selectable node : nodes) {
                String rankNumStr = node.xpath("//div[@class='zg_rankDiv']/span/text()").get();
                int rankNum = 0;
                if (StringUtils.isNotEmpty(rankNumStr)) {
                    rankNum = Integer.valueOf(rankNumStr.trim().replace(".", ""));
                }
                String productName = node.xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncate']/@title").get();
                if (StringUtils.isEmpty(productName)) {
                    productName = node.xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncate']/text()").get();
                }
                String productImgUrl = node.xpath("//div[@class='zg_itemWrapper']/div//img/@src").get();
                String productUrl = node.xpath("//div[@class='zg_itemWrapper']/div/a/@href").get();
                String reviewStar = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]//span[@class='a-icon-alt']/text()").get();
                String reviewNum = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]/a[contains(@class,'a-size-small')]/text()").get();
                String productPrice = node.xpath("//*[@class='p13n-sc-price']/text()").get();
                String amazonDelivery = node.xpath("//*[@class='a-icon-prime']/span[@class='a-icon-alt']/text()").get();
                if (StringUtils.isEmpty(amazonDelivery)) {
                    amazonDelivery = "no prime";
                }

                System.out.println("rankNum:" + rankNum);
                System.out.println("productName:" + productName);
                System.out.println("productImgUrl:" + productImgUrl);
                System.out.println("productUrl:" + productUrl);
                System.out.println("reviewStar:" + reviewStar);
                System.out.println("reviewNum:" + reviewNum);
                System.out.println("productPrice:" + productPrice);
                System.out.println("amazonDelivery:" + amazonDelivery);
            }
        }
    }

    /**
     * 解析品类名称与url
     */
    private List<Department> extractDepartment(Page page) {
        List<Department> departments = new ArrayList<>();
//        List<Selectable> depNodes = page.getHtml().xpath("//*[@id='zg_browseRoot']//span[@class='zg_selected']/parent::li").nodes();

        try {
            TagNode tagNode = new HtmlCleaner().clean(page.getHtml().get());
            Document document = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.evaluate("//*[@id='zg_browseRoot']//*[@class='zg_selected']/parent::li/parent::ul/ul/li/a", document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println(node.getTextContent() + ":" + xPath.evaluate("@href", node,
                        XPathConstants.STRING));
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

//        if (CollectionUtils.isNotEmpty(depNodes)) {
//            Department department;
//            String url;
//            for (Selectable depNode : depNodes) {
//                department = new Department();
//                department.depName = depNode.xpath("/a/text()").get();
//                url = depNode.xpath("/a/@href").get();
//                department.depUrl = url;
//                department.urlMD5 = UrlUtils.md5(url);
//                departments.add(department);
//            }
//        }
//
        return departments;
    }

    private List<Url> extractTop100ProductListing(Page page) {
        List<Url> urls = new ArrayList<>();
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_paginationWrapper']/ol/li").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            Url url;
            for (int i = 1; i < nodes.size(); i++) {
                url = new Url();
                String urlStr = nodes.get(i).xpath("/li/a/@href").get();
                url.urlMD5 = UrlUtils.md5(urlStr);
                url.url = urlStr;
                url.type = R.CrawlType.TOP_100_PRODUCT;
                urls.add(url);
            }
        }
        return urls;
    }

    public static void main(String[] args) {
        //统计库存
//        Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
        //放入购物车
//        Request request = new Request("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance");

//        Request request = new Request("https://www.amazon.com/bestsellers");
        Request request = new Request("https://www.amazon.co.uk/Best-Sellers-Appstore-Android-Travel-Apps/zgbs/mobile-apps/1710384031/ref=zg_bs?_encoding=UTF8&tf=1#2");
        Spider.create(new Top100ProcessorTest())
                .thread(1)
                .addRequest(request)
                .start();
    }


}
