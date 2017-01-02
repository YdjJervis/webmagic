package com.eccang.spider.amazon.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 商品排名
 * @date 2016/12/1 10:24
 */
public class ProductRank {

    public String rank;
    public List<Category> categoryList = new ArrayList<Category>();

    public class Category {
        public String category;
        public String url;

        @Override
        public String toString() {
            return "Category{" +
                    "category='" + category + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProductRank{" +
                "rank='" + rank + '\'' +
                ", categoryList=" + categoryList +
                '}';
    }
}