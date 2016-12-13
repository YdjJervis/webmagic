package com.eccang.wsclient.samples;

import com.eccang.wsclient.api.Ec_Service;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/13 16:54
 */
public class APIWSClient {

    public static void main(String[] args) {
        String result = new Ec_Service().getEcSOAP().pushMessage("EC_001","ERP","123456789","{\n" +
                "    \"customerCode\": \"xxx\",\n" +
                "    \"platformCode\": \"xxx\",\n" +
                "    \"token\": \"xxx\",\n" +
                "    \"data\": {\n" +
                "        \"type\": \"all/update/add\",\n" +
                "\t\t\"batchNum\": \"EC20161208152146151\",\n" +
                "        \"asins\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"asin\": \"B0181YRLT4\",\n" +
                "\t\t\t\t\"reviews\": [\t\t\t\t\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"reviewId\": \"R2V7LL01LD8CLX\",\n" +
                "\t\t\t\t\t\t\"siteCode\": \"US\",\t\t\t\t\n" +
                "\t\t\t\t\t\t\"star\": \"5\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"reviewId\": \"R2V7LL01LD8CDS\",\n" +
                "\t\t\t\t\t\t\"siteCode\": \"US\",\t\t\t\t\n" +
                "\t\t\t\t\t\t\"star\": \"5\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"asin\": \"R2V7LL01LD6DSA\",\n" +
                "\t\t\t\t\"reviews\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"reviewId\": \"R2V7LL01LD6DSA\",\n" +
                "\t\t\t\t\t\t\"siteCode\": \"US\",\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\"star\": \"1\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"reviewId\": \"R2V7LL01LD6DQA\",\n" +
                "\t\t\t\t\t\t\"siteCode\": \"US\",\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\"star\": \"4\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "    }\n" +
                "}");
        System.out.println(result);
    }
}