package us.codecraft.webmagic.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.extractor.product.ProductExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.Product;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 单页面保存测试，用于测试某些页面是否需要JS渲染
 * @date 2016/10/20 10:09
 */
public class ProductProcessor implements PageProcessor {
    @Override
    public void process(Page page) {

        Product product = new ProductExtractorAdapter().extract("US", "B01LW0F62Q", page);

        System.out.println(product);
    }

    public static void main(String[] args) {
        //model_1 https://www.amazon.com/dp/B01LW0F62Q
        //model_2 https://www.amazon.com/dp/B01JBYXUQ6
        //model_3 https://www.amazon.com/dp/B00RM7AAA4
        Spider.create(new ProductProcessor()).addUrl("https://www.amazon.com/dp/B01LW0F62Q")
                .start();
    }

    /**
     * 无需网络请求，测试精确提取规则
     */
    @Test
    public void testExtractPageUnit() {

        Page page = new Page();

        String htmlUnit = "<tbody>\n" +
                " <tr> \n" +
                "  <th class=\"a-color-secondary a-size-base prodDetSectionEntry\"> Product Dimensions </th> \n" +
                "  <td class=\"a-size-base\"> 6 x 3.5 x 2.1 inches </td> \n" +
                " </tr> \n" +
                " <tr> \n" +
                "  <th class=\"a-color-secondary a-size-base prodDetSectionEntry\"> Best Sellers Rank </th> \n" +
                "  <td> <span> <span>#31,628 in Cell Phones &amp; Accessories (<a href=\"https://www.amazon.com/gp/bestsellers/wireless/ref=pd_dp_ts_cps_1\">See Top 100 in Cell Phones &amp; Accessories</a>)</span> <br> <span>#836 in <a href=\"https://www.amazon.com/gp/bestsellers/wireless//ref=pd_zg_hrsr_cps_1_1\">Cell Phones &amp; Accessories</a> &gt; <a href=\"https://www.amazon.com/gp/bestsellers/wireless/2407749011/ref=pd_zg_hrsr_cps_1_2_last\">Unlocked Cell Phones</a></span> <br> </span> </td> \n" +
                " </tr> \n" +
                "</tbody>";
        page.setHtml(new Html(htmlUnit));
        System.out.println(page.getHtml());
        Selectable xpath = page.getHtml().xpath("//a[@id='a']");
        System.out.println(StringUtils.isEmpty(xpath.get()));

    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent(UserAgentUtil.getRandomUserAgent());
    }
}