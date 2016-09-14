package us.codecraft.webmagic.netsense.tianyan.pojo;

import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
     * 1.一线城市：5个  北京，上海，广州，深圳，天津
     * 2.二线发达城市：8个  杭州，南京，济南，重庆，青岛，大连，宁波，厦门.
     * (辽宁省都没有，大连受灾)
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

    /**
     * @return 房产500强剩余的公司详情页面URL列表
     */
    public String[] getLeftList() {
        List<String> list = new ArrayList<String>();

        String FILE_PATH = "D:/archieve/runnable_jar/2016房地产500强剩余公司详情URL.txt";
        FILE_PATH = "E:\\workspace\\服务端\\spider\\webmagic\\webmagic-samples\\src\\main\\java\\us\\codecraft\\webmagic\\netsense\\tianyan\\res\\2016房地产500强剩余公司详情URL.txt";

        File file = new File(FILE_PATH);
        FileReader reader = null;
        BufferedReader br = null;

        CompanyDao dao = (CompanyDao) Context.getInstance().getBean("companyDao");
        try {
            reader = new FileReader(file);
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                if (!dao.isExist(line)) {
                    list.add(line);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] urls = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            urls[i] = list.get(i);
        }
        return urls;
    }

    public String[] getTop100Urls() {
        /*http://www.tianyancha.com/company/878129276

绿地控股股份有限公司
绿地控股集团股份有限公司

保利房地产（集团）股份有限公司

中国海外发展有限公司

融创房地产集团有限公司
天津融创置地有限公司

重庆龙湖地产发展有限公司

广州富力地产股份有限公司   */
        //前10
        String[] urls = new String[]{"http://www.tianyancha.com/company/13637692"
                , "http://www.tianyancha.com/company/878129276"
                , "http://www.tianyancha.com/company/863895721",
                "http://www.tianyancha.com/company/2350439957",
                "http://www.tianyancha.com/company/8562950",
                "http://www.tianyancha.com/company/1402539256",
                "http://www.tianyancha.com/company/1684115751"
                , "http://www.tianyancha.com/company/15213635",
                "http://www.tianyancha.com/company/24201445",
                "http://www.tianyancha.com/company/14365540"
        };

        //前20
        urls = new String[]{
                "http://www.tianyancha.com/company/1197371",
                "http://www.tianyancha.com/company/651442",
                "http://tianyancha.com/company/6955599",
                "http://www.tianyancha.com/company/4690086",
                "http://www.tianyancha.com/company/232640220",
                "http://www.tianyancha.com/company/8863903",
                "http://www.tianyancha.com/company/14365540",
                "http://www.tianyancha.com/company/1639591524",
                "http://www.tianyancha.com/company/356885",
                "http://www.tianyancha.com/company/5733344",
                "http://www.tianyancha.com/company/25953156",
                "http://www.tianyancha.com/company/1883468",
                "http://www.tianyancha.com/company/4845369"

        };
        return urls;
    }

    public static void main(String[] args) {
        /*for (String url : new SearchParamMap().getUrls()) {
            System.out.println(url);
        }*/
        /*for (String url : new SearchParamMap().getFirstSeondCityUrls()) {
            System.out.println(url);
        }*/
        for (String url : new SearchParamMap().getLeftList()) {
            System.out.println(url);
        }
    }
}
