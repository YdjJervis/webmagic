package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * 商品
 */
public class Product {

    private String asin;
    private String store;
    private String inventory;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "asin='" + asin + '\'' +
                ", store='" + store + '\'' +
                ", inventory='" + inventory + '\'' +
                '}';
    }
}
