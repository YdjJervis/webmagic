package us.codecraft.webmagic.netsense.qichacha.pojo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索结果集合
 */
public class SearchResult {

    private String name;
    private String url;
    private String uniqueCode;
    private int layer;

    public SearchResult(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUniqueCode() {
        // firm_6bc7e7ccdb755391651316a0227c059b.shtml
        Pattern pattern = Pattern.compile("firm_.*?([0-9a-zA-Z]+).shtml");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            uniqueCode = matcher.group(1);
        }
        return uniqueCode;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", uniqueCode='" + uniqueCode + '\'' +
                ", layer=" + layer +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new SearchResult("BB","http://www.qichacha.com/firm_GD_fd0f17aefe35780f82dc09074df308c8.shtml"));
    }
}
