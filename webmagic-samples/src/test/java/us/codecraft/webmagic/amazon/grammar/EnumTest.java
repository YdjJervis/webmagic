package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;
import us.codecraft.webmagic.samples.amazon.pojo.SiteEnum;

/**
 * 枚举测试
 */
public class EnumTest extends TestCase {

    @Test
    public void testSiteEnum(){
        System.out.println(SiteEnum.CA.getValue());
        System.out.println(SiteEnum.CA);
        System.out.println(SiteEnum.CA.toString());
        String site = String.valueOf(SiteEnum.CA);
    }
}
