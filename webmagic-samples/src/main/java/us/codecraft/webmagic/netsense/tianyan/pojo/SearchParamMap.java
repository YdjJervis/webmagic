package us.codecraft.webmagic.netsense.tianyan.pojo;

import java.util.*;

/**
 * http://tianyancha.com/search?base=heb&searchCity=邯郸&cate=2800&cateName=房地产业&filterType=cate
 * <p>
 * 保存如下键值对，其中一个base对应多个searchCity：
 * base=heb
 * searchCity=邯郸
 */
public class SearchParamMap {

    public Map<String, List> spMap = new HashMap<String, List>();

    public SearchParamMap() {
        init();
    }

    public void init() {
        List gdList = Arrays.asList("深圳", "广州", "东莞", "中山", "珠海", "佛山", "惠州");
        List scList = Arrays.asList("成都");
        List jsList = Arrays.asList("苏州", "南京", "无锡");//江苏
        List ahList = Arrays.asList("合肥");//安徽
        List zjList = Arrays.asList("杭州", "宁波");//浙江
        List fjList = Arrays.asList("厦门", "福州", "泉州");//福建
        List hubList = Arrays.asList("武汉");//湖北
        List jxList = Arrays.asList("南昌");//江西
        List hunList = Arrays.asList("长沙");//湖南
        List ynList = Arrays.asList("昆明");//云南
        List henList = Arrays.asList("郑州");//河南
        List snxList = Arrays.asList("西安");//陕西
        List sdList = Arrays.asList("青岛", "济南");//山东
        List sxList = Arrays.asList("太原");//山西
        List gxList = Arrays.asList("南宁");//广西
        List hebList = Arrays.asList("保定", "石家庄", "廊坊", "张家口");//河北
        List xjList = Arrays.asList("乌鲁木齐");//新疆

        spMap.put("gd", gdList);
        spMap.put("js", jsList);
        spMap.put("sc", scList);
        spMap.put("ah", ahList);
        spMap.put("zj", zjList);
        spMap.put("fj", fjList);
        spMap.put("hub", hubList);
        spMap.put("jx", jxList);
        spMap.put("hun", hunList);
        spMap.put("yn", ynList);
        spMap.put("hen", henList);
        spMap.put("snx", snxList);
        spMap.put("sd", sdList);
        spMap.put("sx", sxList);
        spMap.put("gx", gxList);
        spMap.put("heb", hebList);
        spMap.put("xj", xjList);

    }

    public String[] getUrls() {
        //http://www.tianyancha.com/search?base=hun&searchCity=常德&cate=2800&cateName=房地产业&filterType=cate
        List<String> urlList = new ArrayList<String>();

        String base = "http://www.tianyancha.com/search?cate=2800&cateName=房地产业&filterType=cate&base=";
        for (String key : spMap.keySet()) {
            List<String> list = spMap.get(key);
            for (String city : list) {
                urlList.add(base + key + "&searchCity=" + city);
            }
        }

        String[] urls = new String[urlList.size()];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = urlList.get(i);
        }
        return urls;
    }

    /**
    1.一线城市：5个  北京，上海，广州，深圳，天津
    2.二线发达城市：8个  杭州，南京，济南，重庆，青岛，大连，宁波，厦门.
      (辽宁省都没有，大连受灾)
    */
    public String[] getFirstSeondCityUrls() {
        final String base = "http://www.tianyancha.com/search?cate=2800&cateName=房地产业&filterType=cate&base=";

        List<String> list = new ArrayList<String>() {{
            add(base + "bj");
            add(base + "sh");
            add(base + "cq");
            add(base + "tj");
            add(base + "gd&searchCity=广州");
            add(base + "gd&searchCity=深圳");
            add(base + "zj&searchCity=杭州");
            add(base + "js&searchCity=南京");
            add(base + "sd&searchCity=济南");
            add(base + "sd&searchCity=青岛");
            add(base + "zj&searchCity=宁波");
            add(base + "fj&searchCity=厦门");
        }};

        Collections.reverse(list);
        String[] urls = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            urls[i] = list.get(i);
        }

        return urls;
    }

    public static void main(String[] args) {
        /*for (String url : new SearchParamMap().getUrls()) {
            System.out.println(url);
        }*/
        for (String url : new SearchParamMap().getFirstSeondCityUrls()) {
            System.out.println(url);
        }

    }
}
