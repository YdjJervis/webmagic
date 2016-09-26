package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * URL前缀
 */
public class UrlPrefix {

    private static final Map<String,Country> countryList = new HashMap<String, Country>() {
        {
            put("cn",new Country("https://www.amazon.cn/dp/","https://www.amazon.cn/product-reviews/"));
            put("us",new Country("https://www.amazon.com/dp/","https://www.amazon.com/product-reviews/"));
            put("de",new Country("https://www.amazon.de/dp/","https://www.amazon.de/product-reviews/"));
            put("jp",new Country("https://www.amazon.jp/dp/","https://www.amazon.jp/product-reviews/"));
            put("es",new Country("https://www.amazon.es/dp/","https://www.amazon.es/product-reviews/"));
        }
    };

    /**
     * @param code 如：cn us de等
     * @return
     */
    public static Country getCountry(String code){
        return countryList.get(code);
    }
}
