package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * 首页轮播图
 */
public class Banner {

    private String sort;
    private String price;
    private String imgUrl;
    private String site;

    public Banner() {}

    public Banner(String site) {
        this.site = site;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "sort='" + sort + '\'' +
                ", price='" + price + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", site='" + site + '\'' +
                '}';
    }
}
