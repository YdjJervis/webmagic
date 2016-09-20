package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * 国家对应码
 */
public class Country {

    private String productUrl;
    private String discussUrl;

    public Country(String productUrl, String discussUrl) {
        this.productUrl = productUrl;
        this.discussUrl = discussUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getDiscussUrl() {
        return discussUrl;
    }

    public void setDiscussUrl(String discussUrl) {
        this.discussUrl = discussUrl;
    }

    @Override
    public String toString() {
        return "Country{" +
                "productUrl='" + productUrl + '\'' +
                ", discussUrl='" + discussUrl + '\'' +
                '}';
    }

}
