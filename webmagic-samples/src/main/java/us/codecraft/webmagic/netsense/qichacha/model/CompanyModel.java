package us.codecraft.webmagic.netsense.qichacha.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 企查查
 */
public class CompanyModel {

    public String[] getUrls() {
        List<String> list = new ArrayList<String>();

        String base = "http://www.qichacha.com/search?index=0&key=";

        list.add(base + "万科企业股份有限公司");

        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i);
        }
        return strs;
    }

    public static void main(String[] args) {
        new CompanyModel().getUrls();
    }
}
